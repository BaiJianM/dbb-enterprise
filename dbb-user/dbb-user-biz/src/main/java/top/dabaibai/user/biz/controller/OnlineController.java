package top.dabaibai.user.biz.controller;

import top.dabaibai.user.api.pojo.dto.OnlineSearchDTO;
import top.dabaibai.user.api.pojo.vo.OnlineSearchResultVO;
import top.dabaibai.user.biz.service.OnlineService;
import top.dabaibai.web.commons.http.DbbResponse;
import top.dabaibai.web.commons.model.PageResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @description: 在线用户信息控制层
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:32
 */
@RestController
@RequestMapping("/online")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "在线用户信息相关接口")
@Validated
@Slf4j
public class OnlineController {

    private final OnlineService onlineService;

    /**
     * @param dto 获取在线用户分页入参
     * @description: 获在线取用户分页
     * @author: 白剑民
     * @date: 2023-05-24 10:01:22
     * @return: DbbResponse<PageResultVO < UserInfoVO>>
     * @version: 1.0
     */
    @Operation(summary = "获取在线用户分页")
    @PostMapping("/page")
    public DbbResponse<PageResultVO<OnlineSearchResultVO>> page(@Validated @RequestBody OnlineSearchDTO dto) {
        return DbbResponse.success(onlineService.page(dto));
    }

    /**
     * @param onlineIds 在线信息id列表
     * @description: 删除在线用户信息
     * @author: 白剑民
     * @date: 2022-10-28 15:21:33
     * @return: top.dabaibai.global.response.DbbResponse<java.lang.Void>
     * @version: 1.0
     */
    @Operation(summary = "删除在线用户信息")
    @Parameter(name = "onlineIds", description = "在线信息id列表", required = true)
    @DeleteMapping
    public DbbResponse<Void> delete(
            @NotEmpty(message = "在线信息id列表，onlineIds不能为null且数组列表长度必须大于0")
            @RequestParam List<Long> onlineIds) {
        onlineService.delete(onlineIds);
        return DbbResponse.success();
    }
}
