package io.github.douyayun.signature.util.digest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SM2Utils {
    private static final Logger log = LoggerFactory.getLogger(SM2Utils.class);

    /**
     * 获取密钥对
     *
     * @return 密钥对
     */
    public static KeyPair getKeyPair() {
        try {
            // 获取SM2椭圆曲线的参数
            ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
            // 获取一个椭圆曲线类型的密钥对生成器
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
            // 使用SM2参数初始化生成器
            kpg.initialize(sm2Spec);
            // 使用SM2的算法区域初始化密钥生成器
            kpg.initialize(sm2Spec, new SecureRandom());
            // 获取密钥对
            return kpg.generateKeyPair();
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
            KeyFactory keyFact = KeyFactory.getInstance("EC", new BouncyCastleProvider());
            return keyFact.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey.getBytes(StandardCharsets.UTF_8))));
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
            KeyFactory keyFact = KeyFactory.getInstance("EC", new BouncyCastleProvider());
            return keyFact.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKey.getBytes(StandardCharsets.UTF_8))));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * SM2加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return
     */
    public static String encrypt(String data, PublicKey publicKey) {
        try {
            ECPublicKeyParameters ecPublicKeyParameters = null;
            if (publicKey instanceof BCECPublicKey) {
                BCECPublicKey bcecPublicKey = (BCECPublicKey) publicKey;
                ECParameterSpec ecParameterSpec = bcecPublicKey.getParameters();
                ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(), ecParameterSpec.getG(), ecParameterSpec.getN());
                ecPublicKeyParameters = new ECPublicKeyParameters(bcecPublicKey.getQ(), ecDomainParameters);
            }
            SM2Engine sm2Engine = new SM2Engine(SM2Engine.Mode.C1C2C3);
            sm2Engine.init(true, new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom()));
            byte[] in = data.getBytes(StandardCharsets.UTF_8);
            byte[] arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
            return Base64.encodeBase64String(arrayOfBytes);
            // return new String(Base64.getEncoder().encode(arrayOfBytes), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * SM2解密
     *
     * @param data       待解密数据
     * @param privateKey 私钥
     * @return
     */
    public static String decrypt(String data, PrivateKey privateKey) {
        try {
            BCECPrivateKey bcecPrivateKey = (BCECPrivateKey) privateKey;
            ECParameterSpec ecParameterSpec = bcecPrivateKey.getParameters();
            ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(), ecParameterSpec.getG(), ecParameterSpec.getN());
            ECPrivateKeyParameters ecPrivateKeyParameters = new ECPrivateKeyParameters(bcecPrivateKey.getD(), ecDomainParameters);
            SM2Engine sm2Engine = new SM2Engine(SM2Engine.Mode.C1C2C3);
            sm2Engine.init(false, ecPrivateKeyParameters);
            byte[] in = Base64.decodeBase64(data);
            byte[] arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
            return new String(arrayOfBytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 签名
     *
     * @param data       待验签数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, PrivateKey privateKey) {
        BouncyCastleProvider provider = new BouncyCastleProvider();
        try {
            // 生成SM2sign with sm3 签名验签算法实例
            Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), provider);
            // 签名需要使用私钥，使用私钥 初始化签名实例
            signature.initSign(privateKey);
            // 签名原文
            byte[] plainText = data.getBytes(StandardCharsets.UTF_8);
            // 写入签名原文到算法中
            signature.update(plainText);
            // 计算签名值
            byte[] signatureValue = signature.sign();
            return Hex.encodeHexString(signatureValue);
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
            // 生成SM2sign with sm3 签名验签算法实例
            Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), new BouncyCastleProvider());
            // 签名需要使用公钥，使用公钥 初始化签名实例
            signature.initVerify(publicKey);
            // 写入待验签的签名原文到算法中
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            // 验签
            return signature.verify(Hex.decodeHex(sign));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    //
    // /**
    //  * 加签
    //  *
    //  * @param data
    //  * @param data
    //  * @return
    //  */
    // public static String sign2(String data, String privateKey) {
    //     BouncyCastleProvider provider = new BouncyCastleProvider();
    //     try {
    //         // 获取椭圆曲线KEY生成器
    //         KeyFactory keyFactory = KeyFactory.getInstance("EC", provider);
    //         byte[] privateKeyData = Base64.getDecoder().decode(privateKey);
    //         PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyData);
    //         Signature rsaSignature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), provider);
    //         rsaSignature.initSign(keyFactory.generatePrivate(privateKeySpec));
    //         rsaSignature.update(data.getBytes(StandardCharsets.UTF_8));
    //         byte[] signed = rsaSignature.sign();
    //         return Hex.encodeHexString(signed);
    //     } catch (Exception e) {
    //         throw new RuntimeException(e);
    //     }
    // }
    //
    // /**
    //  * 验签
    //  *
    //  * @param data
    //  * @param sign
    //  * @param publicKey
    //  * @return
    //  */
    // public static boolean verify2(String data, String sign, String publicKey) {
    //     BouncyCastleProvider provider = new BouncyCastleProvider();
    //     try {
    //         // 获取椭圆曲线KEY生成器
    //         KeyFactory keyFactory = KeyFactory.getInstance("EC", provider);
    //         byte[] publicKeyData = Base64.getDecoder().decode(publicKey);
    //         X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyData);
    //         // 初始化为验签状态
    //         Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), provider);
    //         signature.initVerify(keyFactory.generatePublic(publicKeySpec));
    //         String hexData = Hex.encodeHexString(data.getBytes());
    //         signature.update(Hex.decodeHex(hexData.toCharArray()));
    //         return signature.verify(Hex.decodeHex(sign.toCharArray()));
    //     } catch (Exception e) {
    //         throw new RuntimeException(e);
    //     }
    // }

    public static void main(String[] args) throws Exception {
        // 签名
        // String publicKey = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEmCqZjgrU8+vSi2/ECJzZX4GLP+OntUvwJFinAJELr2pTmDF0kRqsytqNCPvSYsLcnLS16vkPfMFeChi7Djuybg==";
        String publicKey = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEeKm6cIoSzj98papjjOo0bQDeoEe6WkQg/K0z3GuSjJHaNRdxwT7556SHKEJiZGGo/bV9f9cn/Bvjiqi2/dnxmA==";
        // String privateKey = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgNpcx5nZreNWOgLuzQsTVIRkq3/OzXyUZeZ7Vrl0/rGygCgYIKoEcz1UBgi2hRANCAASNi8YR0ftwBAUXLMCH6TwGphpt9KE1mSz5bC07ObEeTwBy6I2FF4WKzFJ4fwON4Hi7WaXd1LzPD3CdKlmWHhjm";
        String privateKey = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgl4mxsd4R1EI+bldkg9NVrHeq8Aleomk4aWQfmGvWszegCgYIKoEcz1UBgi2hRANCAAR4qbpwihLOP3ylqmOM6jRtAN6gR7paRCD8rTPca5KMkdo1F3HBPvnnpIcoQmJkYaj9tX1/1yf8G+OKqLb92fGY";
        // String data = "我是一段测试aaaa";
        String data = "16219236725041682303422099a3e3b7db-eca2-4d54-9aab-a384e436d5bbage=23&name=1,2,3&t=aaaaf8c30adb67b14bc6a53b29b1de01b150";
        PrivateKey privateKey1 = getPrivateKey(privateKey);
        PublicKey publicKey1 = getPublicKey(publicKey);

        String s = Base64.encodeBase64String(publicKey1.getEncoded());
        String s1 = Hex.encodeHexString(publicKey1.getEncoded());
        log.info("s  " + s);
        log.info("s1 " + s1);
        byte[] bytes = Hex.decodeHex(s1);
        String s2 = Base64.encodeBase64String(bytes);
        log.info("s2 " + s2);

        // RSA加密
        String encryptData = encrypt(data, publicKey1);
        System.out.println("加密后内容:" + encryptData);
        // RSA解密
        String decryptData = decrypt(encryptData, privateKey1);
        System.out.println("解密后内容:" + decryptData);

        // RSA签名
        String sign = sign(data, privateKey1);
        // String sign2 = sign2(data, privateKey);
        System.out.println("签名:" + sign);
        // System.out.println("签名2:" + sign2);
        // RSA验签
        // sign = "3046022100dba7f7c9d4a89090572008272a13c4d667407653cccc8749089eaff392316fee02210094c6957222f21fdcfaae0cfe66e448c2057ce0efa094f45da09870f401dd1bd6";
        boolean result = verify(data, publicKey1, sign);
        // boolean result2 = verify2(data, sign2, publicKey);
        System.out.println("验签结果:" + result);
        // System.out.println("验签结果2:" + result2);
    }

}