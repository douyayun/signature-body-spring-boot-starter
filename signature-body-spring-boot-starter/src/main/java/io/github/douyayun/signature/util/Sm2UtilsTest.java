package io.github.douyayun.signature.util;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * SM2工具类
 */
public class Sm2UtilsTest {

    /**
     * 加签
     *
     * @param plainText
     * @return
     */
    public static String sign(String plainText, String privateKeyStr) {
        BouncyCastleProvider provider = new BouncyCastleProvider();
        try {
            // 获取椭圆曲线KEY生成器
            KeyFactory keyFactory = KeyFactory.getInstance("EC", provider);
            byte[] privateKeyData = Base64.getDecoder().decode(privateKeyStr);
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyData);
            Signature rsaSignature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), provider);
            rsaSignature.initSign(keyFactory.generatePrivate(privateKeySpec));
            rsaSignature.update(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] signed = rsaSignature.sign();
            return Base64.getEncoder().encodeToString(signed);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 验签
     *
     * @param data
     * @param signatureValue
     * @return
     */
    public static boolean verify(String data, String signatureValue, String publicKeyStr) {
        BouncyCastleProvider provider = new BouncyCastleProvider();
        try {
            // 获取椭圆曲线KEY生成器
            KeyFactory keyFactory = KeyFactory.getInstance("EC", provider);
            byte[] publicKeyData = Base64.getDecoder().decode(publicKeyStr);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyData);
            // 初始化为验签状态
            Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), provider);
            signature.initVerify(keyFactory.generatePublic(publicKeySpec));
            // signature.update(data.getBytes(StandardCharsets.UTF_8));
            // return signature.verify(signatureValue.getBytes(StandardCharsets.UTF_8));

            signature.update(Hex.decodeHex(data.toCharArray()));
            return signature.verify(Hex.decodeHex(signatureValue.toCharArray()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //
    // /**
    //  * 加密
    //  *
    //  * @param plainText
    //  * @return
    //  */
    // public static byte[] encrypt(String plainText, String publicKeyStr) throws Exception {
    //     Security.addProvider(new BouncyCastleProvider());
    //     try {
    //         // 获取椭圆曲线KEY生成器
    //         KeyFactory keyFactory = KeyFactory.getInstance("EC");
    //         byte[] publicKeyData = Base64.getDecoder().decode(publicKeyStr);
    //         X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyData);
    //         PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
    //         CipherParameters publicKeyParamerters = ECUtil.generatePublicKeyParameter(publicKey);
    //         // 数据加密
    //         StandardSM2Engine engine = new StandardSM2Engine(new SM3Digest(), SM2Engine.Mode.C1C3C2);
    //         engine.init(true, new ParametersWithRandom(publicKeyParamerters));
    //         byte[] encryptData = engine.processBlock(plainText.getBytes(), 0, plainText.getBytes().length);
    //         return encryptData;
    //     } catch (Exception e) {
    //         throw new RuntimeException(e);
    //     }
    // }
    //
    // /**
    //  * 解密
    //  *
    //  * @param encryptedText
    //  * @return
    //  */
    // public static String decrypt(byte[] encryptedData, String privateKeyStr) {
    //     Security.addProvider(new BouncyCastleProvider());
    //     try {
    //         // 获取椭圆曲线KEY生成器
    //         KeyFactory keyFactory = KeyFactory.getInstance("EC");
    //         byte[] privateKeyData = Base64.getDecoder().decode(privateKeyStr);
    //         PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyData);
    //         PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
    //         CipherParameters privateKeyParamerters = ECUtil.generatePrivateKeyParameter(privateKey);
    //         // 数据解密
    //         StandardSM2Engine engine = new StandardSM2Engine(new SM3Digest(), SM2Engine.Mode.C1C3C2);
    //         engine.init(false, privateKeyParamerters);
    //         byte[] plainText = engine.processBlock(encryptedData, 0, encryptedData.length);
    //         return new String(plainText);
    //     } catch (NoSuchAlgorithmException | InvalidKeySpecException
    //              | InvalidKeyException | InvalidCipherTextException e) {
    //         throw new RuntimeException(e);
    //     }
    // }

    /**
     * SM2算法生成密钥对
     *
     * @return 密钥对信息
     */
    public static KeyPair generateSm2KeyPair() {
        try {
            final ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
            // 获取一个椭圆曲线类型的密钥对生成器
            final KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
            SecureRandom random = new SecureRandom();
            // 使用SM2的算法区域初始化密钥生成器
            kpg.initialize(sm2Spec, random);
            // 获取密钥对
            KeyPair keyPair = kpg.generateKeyPair();
            return keyPair;
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        // KeyPair keyPair = generateSm2KeyPair();
        // String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        // String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String publicKey = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEeKm6cIoSzj98papjjOo0bQDeoEe6WkQg/K0z3GuSjJHaNRdxwT7556SHKEJiZGGo/bV9f9cn/Bvjiqi2/dnxmA==";
        String privateKey = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgl4mxsd4R1EI+bldkg9NVrHeq8Aleomk4aWQfmGvWszegCgYIKoEcz1UBgi2hRANCAAR4qbpwihLOP3ylqmOM6jRtAN6gR7paRCD8rTPca5KMkdo1F3HBPvnnpIcoQmJkYaj9tX1/1yf8G+OKqLb92fGY";
        String data = "16219236725041682303422099a3e3b7db-eca2-4d54-9aab-a384e436d5bbage=23&name=1,2,3&t=aaaaf8c30adb67b14bc6a53b29b1de01b150";
        // String encryptedJsonStr = Hex.encodeHexString(encrypt(data, publicKey)) + "";// 16进制字符串
        // String decryptedJsonStr = decrypt(Hex.decodeHex(encryptedJsonStr), privateKey);
        byte[] decode = Base64.getDecoder().decode(sign(data, privateKey));
        String sign = Hex.encodeHexString(decode);
        boolean flag = verify(Hex.encodeHexString(data.getBytes()), sign, publicKey);
        System.out.println("base64后privateKey:" + privateKey);
        System.out.println("base64后publicKey:" + publicKey);
        System.out.println("加密前数据:" + data);
        // System.out.println("公钥加密后16进制字符串:" + encryptedJsonStr);
        // System.out.println("私钥解密后数据：" + decryptedJsonStr);
        System.out.println("私钥加签后数据(16进制)：" + sign);
        System.out.println("公钥验签结果：" + flag);

    }

}