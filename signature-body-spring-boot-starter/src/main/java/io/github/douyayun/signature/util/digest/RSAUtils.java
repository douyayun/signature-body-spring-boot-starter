package io.github.douyayun.signature.util.digest;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {
    private static final Logger log = LoggerFactory.getLogger(RSAUtils.class);

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 获取密钥对
     *
     * @return 密钥对
     */
    public static KeyPair getKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return generator.generateKeyPair();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes(StandardCharsets.UTF_8));
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes(StandardCharsets.UTF_8));
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * RSA加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return
     */
    public static String encrypt(String data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int inputLen = data.getBytes(StandardCharsets.UTF_8).length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offset = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offset > 0) {
                if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8), offset, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8), offset, inputLen - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
            // 加密后的字符串
            // return Base64.getEncoder().encodeToString(encryptedData);
            return Base64.encodeBase64String(encryptedData);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * RSA解密
     *
     * @param data       待解密数据
     * @param privateKey 私钥
     * @return
     */
    public static String decrypt(String data, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] dataBytes = Base64.decodeBase64(data);
            int inputLen = dataBytes.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offset = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offset > 0) {
                if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            // 解密后的内容
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 签名
     *
     * @param data       待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, PrivateKey privateKey) {
        try {
            byte[] keyBytes = privateKey.getEncoded();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey key = keyFactory.generatePrivate(keySpec);
            // Signature signature = Signature.getInstance("MD5withRSA");
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(key);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64String(signature.sign());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 验签
     *
     * @param data      待验签数据
     * @param publicKey 公钥
     * @param sign      签名
     * @return 是否验签通过
     */
    public static boolean verify(String data, PublicKey publicKey, String sign) {
        try {
            byte[] keyBytes = publicKey.getEncoded();
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey key = keyFactory.generatePublic(keySpec);
            // Signature signature = Signature.getInstance("MD5withRSA");
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(key);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.decodeBase64(sign.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        // https://baijiahao.baidu.com/s?id=1759054966311921498&wfr=spider&for=pc
        // 生成密钥对
        // KeyPair keyPair = getKeyPair();
        // String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
        // String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
        // System.out.println("私钥:" + privateKey);
        // System.out.println("公钥:" + publicKey);
        // RSA加密
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCe3KaMcKh7LHGf4Z2+TC0vfuzcffkvgVIGcTEdu7jW/c9f7a4sRo9YfZUxwpsRanvxwWB6XzBIsBH6bV+wCiRl0RYzCHV/59dxsutBxxJGXoWMNqgWyt7pkGlEin67CH1tLEzJLjN/KLcgMd6qjaHQbRg5xv4tMUSD+5893kSIhQIDAQAB";
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJ7cpoxwqHsscZ/hnb5MLS9+7Nx9+S+BUgZxMR27uNb9z1/trixGj1h9lTHCmxFqe/HBYHpfMEiwEfptX7AKJGXRFjMIdX/n13Gy60HHEkZehYw2qBbK3umQaUSKfrsIfW0sTMkuM38otyAx3qqNodBtGDnG/i0xRIP7nz3eRIiFAgMBAAECgYAZshYMZFKULdGpKzyxIbAzXQPh/ExYh0HlwdxQc2NH/yDSYucHyVf5V8LQnCop7H/k0EMTOB9euoAF8jgGiPVu8wSfajGIVs7wPGsEPMyewvFFgYiAr04Y9/1/x7eunuIKG7wnSPfNLO5N2F9RlBLTz9dAnDBpzyFJ10GgBaEqoQJBAN/uoO4Te9j7ML3SacS0wT/aQGM/2D8Wm5BB7kVukdkc5WFCLIf5GG/aQkqqh9hI7+3g0dAcYQ10aUPCDrHLq5ECQQC1nIq6z7D/pr+v3ZOPog5AwXqiAjGtx07U5vJ0ElaY4jooLlEhRPBuRs4qT99xE/THWbAdA5VCJ5+7+GT2FQu1AkA4uGDa7OR52WZHLnzL7lzkp4uXmBxWHovYxlpsC821iLQxJmnRJy8hZc9Uyk0OY6siPcfnRsjud4YSO50tO8GBAkB8Bk/ni/bAYEv/+j3PlUhOiNMK2Jy8pKP9WUqyYeOpvLUiw568LaxUYx5gXvOk8Y/0pBdVeSDxcMXxRI0OUQkRAkAIbvj76J72xUo59Slx+MBz0YlZNit1k/qZtlE321ihddEnw6cLxpSBupL8okd9Jhju8637dPt/jpISJ8t9vAM4";
        String data = "我是一段测试aaaa";
        // RSA加密
        String encryptData = encrypt(data, getPublicKey(publicKey));
        System.out.println("加密后内容:" + encryptData);
        // RSA解密
        String decryptData = decrypt(encryptData, getPrivateKey(privateKey));
        System.out.println("解密后内容:" + decryptData);

        // RSA签名
        PrivateKey privateKey1 = getPrivateKey(privateKey);
        PublicKey publicKey1 = getPublicKey(publicKey);
        String sign = sign(data, privateKey1);
        System.out.println("签名:" + sign);
        // RSA验签
        boolean result = verify(data, publicKey1, sign);
        System.out.println("验签结果:" + result);
    }

}