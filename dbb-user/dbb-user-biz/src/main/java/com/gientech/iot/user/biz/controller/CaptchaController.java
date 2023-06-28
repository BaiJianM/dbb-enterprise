package com.gientech.iot.user.biz.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.UUID;
import com.gientech.iot.redis.utils.RedisUtils;
import com.gientech.iot.user.api.pojo.UserConstants;
import com.gientech.iot.user.api.pojo.vo.AuthCodeResultVO;
import com.gientech.iot.user.biz.enums.CustomErrorCodeEnum;
import com.gientech.iot.web.commons.http.GientechException;
import com.gientech.iot.web.commons.http.GientechResponse;
import com.gientech.iot.web.configuration.authcode.AuthCodeTypeEnum;
import com.gientech.iot.web.configuration.authcode.KaptchaProperties;
import com.google.code.kaptcha.Producer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.validation.constraints.NotBlank;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @description: 验证码操作处理
 * @author: 白剑民
 * @dateTime: 2023/04/21 13:41
 */
@Slf4j
@Tag(name = "图形验证码相关接口")
@RestController
@RequestMapping("/authCode")
public class CaptchaController {

    /**
     * 注入字符类型的验证码生成器
     */
    @Resource(name = "captchaProducerText")
    private Producer captchaProducer;

    /**
     * 注入数字类型的验证码生成器
     */
    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    private final RedisUtils redisUtils;

    private final KaptchaProperties properties;

    public CaptchaController(RedisUtils redisUtils, KaptchaProperties properties) {
        this.redisUtils = redisUtils;
        this.properties = properties;
    }

    /**
     * @description: 获取图形验证码，返回图片base64字符
     * @author: 白剑民
     * @date: 2023-04-24 10:15:58
     * @return: com.gientech.iot.core.global.response.GientechResponse<com.gientech.iot.user.api.entity.vo.AuthCodeResultVO>
     * @version: 1.0
     */
    // TODO 不可无限调用，需后端配合前端增加调用三次后锁住1分钟的逻辑，后端锁住接口，前端增加解锁倒计时
    @Operation(summary = "获取图形验证码")
    @GetMapping
    public GientechResponse<AuthCodeResultVO> getCode() {
        AuthCodeResultVO result = new AuthCodeResultVO();
        boolean captchaEnabled = properties.getIsEnable();
        result.setCaptchaEnabled(captchaEnabled);
        if (!captchaEnabled) {
            return GientechResponse.success(result);
        }
        // 保存验证码信息
        String uuid = UUID.fastUUID().toString(true);
        String verifyKey = UserConstants.RedisCache.CAPTCHA_CODE_KEY + uuid;

        String capStr, code = null;
        BufferedImage image = null;
        // 生成验证码
        AuthCodeTypeEnum captchaType = properties.getAuthCodeType();
        if (captchaType == AuthCodeTypeEnum.MATH) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if (captchaType == AuthCodeTypeEnum.CHAR) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }
        redisUtils.setEx(verifyKey, code, properties.getTimeout(), TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            assert image != null;
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            throw new GientechException(CustomErrorCodeEnum.AUTH_CODE_ERROR);
        }
        result.setUuid(uuid);
        result.setImg(Base64.encode(os.toByteArray()));
        return GientechResponse.success(result);
    }

    /**
     * @param authCode 验证码
     * @param uuid     验证码缓存key
     * @description: 校验图片验证码
     * @author: 白剑民
     * @date: 2023-04-24 10:18:22
     * @return: boolean
     * @version: 1.0
     */
    @Operation(summary = "校验图片验证码")
    @Parameters({
            @Parameter(name = "authCode", description = "验证码", required = true),
            @Parameter(name = "uuid", description = "验证唯一识别码", required = true),
    })
    @GetMapping("/verify")
    public boolean verify(
            @NotBlank(message = "验证码，authCode不能为null且字符串长度必须大于0")
            @RequestParam("authCode") String authCode,
            @NotBlank(message = "验证码，uuid不能为null且字符串长度必须大于0")
            @RequestParam("uuid") String uuid) {
        boolean captchaEnabled = properties.getIsEnable();
        String verifyKey = UserConstants.RedisCache.CAPTCHA_CODE_KEY + uuid;
        Object code = redisUtils.get(verifyKey).orElse("");
        redisUtils.delete(verifyKey);
        return !captchaEnabled || code.equals(authCode);
    }
}
