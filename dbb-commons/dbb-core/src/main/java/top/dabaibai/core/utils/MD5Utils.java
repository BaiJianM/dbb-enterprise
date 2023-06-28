package top.dabaibai.core.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

/**
 * @description: MD5工具类
 * @author: 白剑民
 * @dateTime: 2022/11/3 16:01
 */
@Slf4j
public class MD5Utils {

    /**
     * 自定义MD5加密私钥
     */
    private final static String DES_KEY = "zmj/6+ZHi6qiWohfZGyHmQS50+ggiFg";

    /**
     * 向量(同时拥有向量和密匙才能解密)，此向量必须是8byte
     */
    private final static byte[] DES_IV = new byte[]{0x22, 0x54, 0x36, 110, 0x40, (byte) 0xac, (byte) 0xad, (byte) 0xdf};
    /**
     * 密钥对象
     */
    private static Key key;
    /**
     * 规范加密参数
     */
    private static AlgorithmParameterSpec iv;

    static {
        try {
            // 设置密钥参数
            DESKeySpec keySpec = new DESKeySpec(DES_KEY.getBytes(StandardCharsets.UTF_8));
            // 规范加密参数接口
            iv = new IvParameterSpec(DES_IV);
            // 初始化密钥工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // 初始化密钥对象
            key = keyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("MD5工具初始化错误信息: {}", e.getMessage());
        }
    }

    /**
     * @param data 待加密字符串
     * @description: 加密
     * @author: 白剑民
     * @date: 2022-11-03 16:12:38
     * @return: java.lang.String
     * @version: 1.0
     */
    @SneakyThrows
    public static String encrypt(String data) {
        // 得到加密对象Cipher
        Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        // 设置工作模式为加密模式，给出密钥和向量
        enCipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] pasByte = enCipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.getEncoder().encode(pasByte), StandardCharsets.UTF_8);
    }

    /**
     * @param sourceStr 目标字符串
     * @description: 获取MD5的值，可用于对比校验
     * @author: 白剑民
     * @date: 2022-11-03 16:11:25
     * @return: java.lang.String
     * @version: 1.0
     */
    public static String getMd5Value(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte[] b = md.digest();
            int i;
            StringBuilder buf = new StringBuilder();
            for (byte value : b) {
                i = value;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException ignored) {
        }
        return result;
    }

}
