package io.github.douyayun.signature.util;

import io.github.douyayun.signature.properties.SignType;
import io.github.douyayun.signature.util.digest.MD5Utils;
import io.github.douyayun.signature.util.digest.RSAUtils;
import io.github.douyayun.signature.util.digest.SM2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

/**
 * 签名工具
 *
 * @author houp
 * @since 1.0.0
 */
public class SignUtils {
    private static final Logger log = LoggerFactory.getLogger(SignUtils.class);

    private SignUtils() {
    }

    /**
     * 获取密钥对
     *
     * @return 密钥对
     */
    public static KeyPair getKeyPair(SignType signType) {
        if (signType == SignType.RSA) {
            return RSAUtils.getKeyPair();
        } else if (signType == SignType.SM2) {
            return SM2Utils.getKeyPair();
        }
        throw new RuntimeException("此加密类型不支持");
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey, SignType signType) {
        if (signType == SignType.RSA) {
            return RSAUtils.getPrivateKey(privateKey);
        } else if (signType == SignType.SM2) {
            return SM2Utils.getPrivateKey(privateKey);
        }
        throw new RuntimeException("此加密类型不支持");
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey getPublicKey(String publicKey, SignType signType) {
        if (signType == SignType.RSA) {
            return RSAUtils.getPublicKey(publicKey);
        } else if (signType == SignType.SM2) {
            return SM2Utils.getPublicKey(publicKey);
        }
        throw new RuntimeException("此加密类型不支持");
    }

    /**
     * 加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return
     */
    public static String encrypt(String data, String publicKey, SignType signType) {
        if (signType == SignType.RSA) {
            return RSAUtils.encrypt(data, getPublicKey(publicKey, signType));
        } else if (signType == SignType.SM2) {
            return SM2Utils.encrypt(data, getPublicKey(publicKey, signType));
        } else if (signType == SignType.MD5) {
            return MD5Utils.md5(data);
        }
        throw new RuntimeException("此加密类型不支持");
    }

    /**
     * 解密
     *
     * @param data       待解密数据
     * @param privateKey 私钥
     * @return
     */
    public static String decrypt(String data, String privateKey, SignType signType) {
        if (signType == SignType.RSA) {
            return RSAUtils.decrypt(data, getPrivateKey(privateKey, signType));
        } else if (signType == SignType.SM2) {
            return SM2Utils.decrypt(data, getPrivateKey(privateKey, signType));
        }
        throw new RuntimeException("此加密类型不支持");
    }

    /**
     * 签名
     *
     * @param data       待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, String privateKey, SignType signType) {
        if (signType == SignType.RSA) {
            return RSAUtils.sign(data, getPrivateKey(privateKey, signType));
        } else if (signType == SignType.SM2) {
            return SM2Utils.sign(data, getPrivateKey(privateKey, signType));
        } else if (signType == SignType.MD5) {
            return MD5Utils.md5(data);
        }
        throw new RuntimeException("此加密类型不支持");
    }

    /**
     * 验签
     *
     * @param data      待验签数据
     * @param publicKey 公钥
     * @param sign      签名
     * @return 是否验签通过
     */
    public static boolean verify(String data, String publicKey, String sign, SignType signType) {
        if (signType == SignType.RSA) {
            return RSAUtils.verify(data, getPublicKey(publicKey, signType), sign);
        } else if (signType == SignType.SM2) {
            return SM2Utils.verify(data, getPublicKey(publicKey, signType), sign);
        } else if (signType == SignType.MD5) {
            return sign.equalsIgnoreCase(MD5Utils.md5(data));
        }
        throw new RuntimeException("此加密类型不支持");
    }

    // /**
    //  * 数据签名
    //  *
    //  * @param data 数据
    //  * @return
    //  */
    // public static String getSign(String data) {
    //     // commons-codec
    //     // return DigestUtils.md5Hex(data).toLowerCase();
    //     return MD5Utils.md5(data).toLowerCase();
    // }

    /**
     * map排序
     * 对参数进行ASCII码从小到大排序（字典序）
     *
     * @param map 待排序集合
     * @return 排序后字符串
     */
    public static String sortMapByKey(Map<String, String[]> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        List<Map.Entry<String, String[]>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String[]>>() {
            @Override
            public int compare(Map.Entry<String, String[]> o1, Map.Entry<String, String[]> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        List<String> data = new ArrayList<>();
        for (Map.Entry<String, String[]> item : list) {
            String key = item.getKey();
            String[] value = item.getValue();
            if (null != value && value.length > 0) {
                data.add(key + "=" + String.join(",", value));
            } else {
                data.add(key + "=");
            }
        }
        return String.join("&", data);
    }
}
