package io.github.douyayun.signature.util;

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
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SM2Utils {
    private static final Logger log = LoggerFactory.getLogger(SM2Utils.class);

    /**
     * 获取密钥对
     *
     * @return 密钥对
     */
    public static KeyPair getKeyPair() throws Exception {
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
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFact = KeyFactory.getInstance("EC", new BouncyCastleProvider());
        return keyFact.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey.getBytes(StandardCharsets.UTF_8))));
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFact = KeyFactory.getInstance("EC", new BouncyCastleProvider());
        return keyFact.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey.getBytes(StandardCharsets.UTF_8))));
    }

    /**
     * SM2加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        ECPublicKeyParameters ecPublicKeyParameters = null;
        if (publicKey instanceof BCECPublicKey) {
            BCECPublicKey bcecPublicKey = (BCECPublicKey) publicKey;
            ECParameterSpec ecParameterSpec = bcecPublicKey.getParameters();
            ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(), ecParameterSpec.getG(), ecParameterSpec.getN());
            ecPublicKeyParameters = new ECPublicKeyParameters(bcecPublicKey.getQ(), ecDomainParameters);
        }
        SM2Engine sm2Engine = new SM2Engine(SM2Engine.Mode.C1C2C3);
        sm2Engine.init(true, new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom()));
        byte[] arrayOfBytes = null;
        try {
            byte[] in = data.getBytes(StandardCharsets.UTF_8);
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
        } catch (Exception e) {
            log.error("SM2加密时出现异常:", e.getMessage());
            throw new RuntimeException(e);
        }
        return new String(Base64.getEncoder().encode(arrayOfBytes), StandardCharsets.UTF_8);
    }

    /**
     * SM2解密
     *
     * @param data       待解密数据
     * @param privateKey 私钥
     * @return
     */
    public static String decrypt(String data, PrivateKey privateKey) throws Exception {
        BCECPrivateKey bcecPrivateKey = (BCECPrivateKey) privateKey;
        ECParameterSpec ecParameterSpec = bcecPrivateKey.getParameters();
        ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
                ecParameterSpec.getG(), ecParameterSpec.getN());
        ECPrivateKeyParameters ecPrivateKeyParameters = new ECPrivateKeyParameters(bcecPrivateKey.getD(),
                ecDomainParameters);
        SM2Engine sm2Engine = new SM2Engine(SM2Engine.Mode.C1C2C3);
        sm2Engine.init(false, ecPrivateKeyParameters);
        String result = null;

        byte[] arrayOfBytes = null;
        try {
            byte[] in = Base64.getDecoder().decode(data);
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
            result = new String(arrayOfBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("SM2解密时出现异常:", e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 签名
     *
     * @param data       待验签数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        // 生成SM2sign with sm3 签名验签算法实例
        Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), new BouncyCastleProvider());
        // 签名需要使用私钥，使用私钥 初始化签名实例
        signature.initSign(privateKey);
        // 签名原文
        byte[] plainText = data.getBytes(StandardCharsets.UTF_8);
        // 写入签名原文到算法中
        signature.update(plainText);
        // 计算签名值
        byte[] signatureValue = signature.sign();
        return Hex.toHexString(signatureValue);
    }

    /**
     * 验签
     *
     * @param data      待验签数据
     * @param publicKey 公钥
     * @param sign      签名
     * @return 是否验签通过
     */
    public static boolean verify(String data, PublicKey publicKey, String sign) throws Exception {
        // 生成SM2sign with sm3 签名验签算法实例
        Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), new BouncyCastleProvider());
        // 签名需要使用公钥，使用公钥 初始化签名实例
        signature.initVerify(publicKey);
        // 写入待验签的签名原文到算法中
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        // 验签
        return signature.verify(sign.getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String[] args) {
        try {
            // 签名
            String publicKey = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEjYvGEdH7cAQFFyzAh+k8BqYabfShNZks+WwtOzmxHk8AcuiNhReFisxSeH8DjeB4u1ml3dS8zw9wnSpZlh4Y5g==";
            String privateKey = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgNpcx5nZreNWOgLuzQsTVIRkq3/OzXyUZeZ7Vrl0/rGygCgYIKoEcz1UBgi2hRANCAASNi8YR0ftwBAUXLMCH6TwGphpt9KE1mSz5bC07ObEeTwBy6I2FF4WKzFJ4fwON4Hi7WaXd1LzPD3CdKlmWHhjm";
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

            // // 根据采用的编码结构反序列化公私钥
            // BouncyCastleProvider bc = new BouncyCastleProvider();
            // KeyFactory keyFact = KeyFactory.getInstance("EC", bc);
            // PublicKey publicKey1 = keyFact.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey.getBytes(StandardCharsets.UTF_8))));
            // PrivateKey privateKey1 = keyFact.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey.getBytes(StandardCharsets.UTF_8))));
            // // 生成SM2sign with sm3 签名验签算法实例
            // Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), new BouncyCastleProvider());
            // // 签名需要使用私钥，使用私钥 初始化签名实例
            // signature.initSign(privateKey1);
            // // 签名原文
            // byte[] plainText = data.getBytes(StandardCharsets.UTF_8);
            // // 写入签名原文到算法中
            // signature.update(plainText);
            // // 计算签名值
            // byte[] signatureValue = signature.sign();
            // System.out.println("签名 " + Hex.toHexString(signatureValue));
            //
            // // 签名需要使用公钥，使用公钥 初始化签名实例
            // signature.initVerify(publicKey1);
            // // 写入待验签的签名原文到算法中
            // signature.update(plainText);
            // // 验签
            // System.out.println("验证 " + signature.verify(signatureValue));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("加解密异常");
        }
    }

}