package top.dabaibai.core.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @description: RSA加密工具类
 * @author: 白剑民
 * @dateTime: 2022-10-17 16:08:52
 */
public class RSAUtils {

    /**
     * 私钥文件名
     */
    public final static String PRIVATE_KEY_FILE_NAME = "privateKey.keystore";

    /**
     * 公钥文件名
     */
    public final static String PUBLIC_KEY_FILE_NAME = "publicKey.keystore";

    /**
     * 密钥文件存放路径
     */
    private final static String FILE_PATH = System.getProperty("user.dir") + File.separator + "rsa_key";

    private static Cipher cipher;

    static {
        try {
            // 指定加密算法
            cipher = Cipher.getInstance("RSA");
            // 本地项目路径的rsa_key文件夹下生成密钥对
            generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param key 经过base64编码的密钥字符串
     * @description: 获取公钥
     * @author: 白剑民
     * @date: 2022-11-04 10:22:18
     * @return: java.security.PublicKey
     * @version: 1.0
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decodeBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * @param key 经过base64编码的密钥字符串
     * @description: 得到私钥
     * @author: 白剑民
     * @date: 2022-11-04 10:24:08
     * @return: java.security.PrivateKey
     * @version: 1.0
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decodeBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * @param publicKey 经过base64编码的公钥字符串
     * @param text      待加密明文
     * @description: 使用指定公钥对明文进行加密并返回base64编码的字符串
     * @author: 白剑民
     * @date: 2022-11-04 10:26:07
     * @return: java.lang.String
     * @version: 1.0
     */
    public static String encrypt(String publicKey, String text) {
        String result = "";
        try {
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
            byte[] enBytes = cipher.doFinal(text.getBytes());
            result = Base64.encodeBase64String(enBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param privateKey 经过base64编码的私钥字符串
     * @param decryptStr 待解密密文
     * @description: 使用指定私钥对明文密文进行解密
     * @author: 白剑民
     * @date: 2022-11-04 10:28:42
     * @return: java.lang.String
     * @version: 1.0
     */
    public static String decrypt(String privateKey, String decryptStr) {
        String result = "";
        try {
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
            byte[] deBytes = cipher.doFinal(Base64.decodeBase64(decryptStr));
            result = new String(deBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param text 待加密字符串
     * @description: 使用keystore对明文进行加密
     * @author: 白剑民
     * @date: 2022-11-04 10:14:29
     * @return: java.lang.String
     * @version: 1.0
     */
    public static String encrypt(String text) {
        String result = "";
        try {
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(readKeyFile(PUBLIC_KEY_FILE_NAME)));
            byte[] enBytes = cipher.doFinal(text.getBytes());
            result = Base64.encodeBase64String(enBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param decryptStr 待解密字符串
     * @description: 使用默认本地生成的keystore对密文进行解密
     * @author: 白剑民
     * @date: 2022-11-04 10:11:38
     * @return: java.lang.String
     * @version: 1.0
     */
    public static String decrypt(String decryptStr) {
        String result = "";
        try {
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(readKeyFile(PRIVATE_KEY_FILE_NAME)));
            byte[] deBytes = cipher.doFinal(Base64.decodeBase64(decryptStr));
            result = new String(deBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param keyName 密钥文件名
     * @description: 使用默认本地生成的keystore密钥文件读取
     * @author: 白剑民
     * @date: 2022-11-04 10:13:41
     * @return: java.lang.String
     * @version: 1.0
     */
    public static String readKeyFile(String keyName) throws IOException {
        // 从项目根路径下读取生成的keystore密钥文件
        FileReader fr = new FileReader(FILE_PATH + File.separator + keyName);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder privateKeyStr = new StringBuilder();
        String str;
        while ((str = br.readLine()) != null) {
            privateKeyStr.append(str);
        }
        br.close();
        fr.close();
        return privateKeyStr.toString();
    }

    /**
     * @description: 生成密钥对
     * @author: 白剑民
     * @date: 2022-11-04 10:36:17
     * @version: 1.0
     */
    public static void generateKeyPair() {
        // 密钥存放路径
        File file = new File(FILE_PATH);
        // 路径不存在就生成
        if (!file.exists()) {
            try {
                boolean result = file.mkdir();
                if (!result) {
                    throw new IOException("RSA密钥文件夹创建失败");
                }
                KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
                // 密钥位数
                keyPairGen.initialize(2048);
                // 密钥对
                KeyPair keyPair = keyPairGen.generateKeyPair();
                // 公钥
                PublicKey publicKey = keyPair.getPublic();
                // 私钥
                PrivateKey privateKey = keyPair.getPrivate();
                //得到公钥字符串
                String publicKeyString = getKeyString(publicKey);
                //得到私钥字符串
                String privateKeyString = getKeyString(privateKey);
                //将密钥对写入到文件
                FileWriter publicFile = new FileWriter(FILE_PATH + File.separator + PUBLIC_KEY_FILE_NAME);
                FileWriter privateFile = new FileWriter(FILE_PATH + File.separator + PRIVATE_KEY_FILE_NAME);
                BufferedWriter publicBuffer = new BufferedWriter(publicFile);
                BufferedWriter privateBuffer = new BufferedWriter(privateFile);
                publicBuffer.write(publicKeyString);
                privateBuffer.write(privateKeyString);
                publicBuffer.flush();
                publicBuffer.close();
                publicFile.close();
                privateBuffer.flush();
                privateBuffer.close();
                privateFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param key 经过base64编码的字符串
     * @description: 获取密钥字符串
     * @author: 白剑民
     * @date: 2022-11-04 10:36:31
     * @return: java.lang.String
     * @version: 1.0
     */
    private static String getKeyString(Key key) {
        return Base64.encodeBase64String(key.getEncoded());
    }
}
