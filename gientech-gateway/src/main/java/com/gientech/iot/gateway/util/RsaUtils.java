package com.gientech.iot.gateway.util;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @description: RSA加密工具类
 * @author: 白剑民
 * @dateTime: 2022-10-17 16:08:52
 */
public class RsaUtils {
    
    /**
     * @description: PrivateKey 私钥
     * @author: 白剑民
     * @date: 2022-10-17 22:47:58
     * @return: PrivateKey
     * @version: 1.0
     */
    public static PrivateKey getPrivateKey() {
        try {
            String privateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDW7X2+WJ/ZSNJn" + 
                    "jYDWgXPUBYavj6sf1cpISxRq6Co87XR/V3rDf2in0QnKpGqW6rs1adwO6b8/HhzJ" + 
                    "hFVVyWERDe+jaaqDBpcGFBv/uXHKgUdaQqKbO6pPznJUHDl415ICA24qQhbtZrij" + 
                    "iI/DbR5dK/7p9NWNi+B+yaHgbq0B45H5hYmAju3b2Eb4MCoV0sWDa4EtKewg+bNn" + 
                    "QfsRo7Udc0n/94SGAOVOl6dULpPKQwQS5JXbVsSHP9bz0C0ocGuLNHOr7LAELP6Y" + 
                    "Kp0TtGjbZkw8MtZlrLQrjKJrxrHr+OVfAibgiJdh2/9ufQomTUpU61G03Z9qiu+y" + 
                    "ZpCyL0fvAgMBAAECggEBAI4HKs0aa6d+w+vCSp11tkEp1bM2UXMcKCxV0QxEl4Tj" + 
                    "tdRgzrNaAqLDNKFAvV81hrjpV45TURo4n/VbCblC9m36s51dOlyTgkF2EHqQ5W1Y" + 
                    "uPoghqvmZcmPN2X1sNT2P3otIiOywMvo0aLfn4EZHrObWzznmieLg7LFM7/H43QW" + 
                    "499wpmERy2icb05dzV6hzI4HRp2ODBHhajE6mSn5B2fHKKEQ46O3071Fn7m2V62J" + 
                    "V9IwwOhNVZ6hJaDa9XqRfTjwCuHed3+l79igaVbGADUrLRl5hXiPZ0d3wAuL8M0B" + 
                    "O4rxzmj/6+ZHi6qiWohfZGyHmQS50+ggiFgUw9g3GMECgYEA+EqCPfWs7wTi0YAg" + 
                    "G/T4tPczUQFyRD9yKzsjVyDq9ZsrRaWT7Qv8NrYSbgXigkxR5MJFE720xymWhepz" + 
                    "+05olQ5WZG8mCi+81C2jbO+Y4j3lQ85B3wBt2gSNRZuE/81qceoTS1qukIF+SZ3j" + 
                    "FkUNMOY3KpgaFirMu6UwEjByTkcCgYEA3ZnM8vWvND0Uq5MtL9fLgR7jTN7SPxCe" + 
                    "5HEJ/Alkxyo8woBmiwk2jToNXQ92E5jX6L74EAY8KiwuOKWrytK7BI6Fv38dXji0" + 
                    "1SN5fHh5r0PlCUDP2LdVmWV+8590SwwH3N0CMNbwV4/6S/4O2DkmTGMSCUtr4cNG" + 
                    "dt4bNXBvxRkCgYAPi2KiDGp6QF0O7q2T8n9LnRpaXPPRcANuz3ID92yeWcuYvagI" + 
                    "NDLjHf0DZFzs+Wg7ISp/9OWHfoQM+EOwnRURF+eyu/6urpRs2DqeDFYO+/8JgrU/" + 
                    "B+DYwGYz0GvANBUQ8q2ojxHUvDay1y6ra0FLKW6UvRcNAjr1GH9s9BmD6wKBgFC1" + 
                    "XGS8wHP5LxcSQmnjYXYEhuqPWOmpJwWyed12mmLDal+4niBTWa/0DbvwDqQyjahy" + 
                    "LSzA6Ja/gpIVHM2BFKYjNmk5ZbvUUWAzTGLeCgugYcGN/mVC2IVG5wbZ82QF3LM/" + 
                    "NzYMgnqRsKqOvF9Z6S33AzGpa4BNO9O9LxG2gq65AoGAJH41/9jNaTadu4ULq5dW" + 
                    "6gUh0E0y2mPCrr2JuyUIwGpQ+TaeabWGMa1bXdsZhgamQlrBXj58lupSnhmHegh9" + 
                    "p+6hJ8irx4guUIChEirNfzL/OIVgKxLSqmw1KgiPmcsfTQfMQNg6Y7bjwTgXtIme" + 
                    "xJUAhUECe6g49iGyp9AfyRk=";
            // PKCS8格式的密钥
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr));
            // RSA 算法
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: PublicKey 公钥
     * @author: 白剑民
     * @date: 2022-10-17 22:47:54
     * @return: PublicKey
     * @version: 1.0
     */
    public static PublicKey getPublicKey() {
        try {
            String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1u19vlif2UjSZ42A1oFz" + 
                    "1AWGr4+rH9XKSEsUaugqPO10f1d6w39op9EJyqRqluq7NWncDum/Px4cyYRVVclh" + 
                    "EQ3vo2mqgwaXBhQb/7lxyoFHWkKimzuqT85yVBw5eNeSAgNuKkIW7Wa4o4iPw20e" + 
                    "XSv+6fTVjYvgfsmh4G6tAeOR+YWJgI7t29hG+DAqFdLFg2uBLSnsIPmzZ0H7EaO1" + 
                    "HXNJ//eEhgDlTpenVC6TykMEEuSV21bEhz/W89AtKHBrizRzq+ywBCz+mCqdE7Ro" + 
                    "22ZMPDLWZay0K4yia8ax6/jlXwIm4IiXYdv/bn0KJk1KVOtRtN2faorvsmaQsi9H" + 
                    "7wIDAQAB";
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }
}
