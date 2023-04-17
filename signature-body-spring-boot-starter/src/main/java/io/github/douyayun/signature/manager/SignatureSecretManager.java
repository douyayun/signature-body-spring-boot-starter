package io.github.douyayun.signature.manager;

import io.github.douyayun.signature.properties.SignatureProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 秘钥管理器
 *
 * @author houp
 * @version 1.0.0
 * @date 2023/3/17 20:16
 */
public class SignatureSecretManager {

    /**
     * 签名密钥
     */
    private static Map<String, String> signSecretMap = new ConcurrentHashMap<>();

    /**
     * 获取所有秘钥
     *
     * @return
     */
    public static List<SignatureProperties.Secret> getAllSecret() {
        List<SignatureProperties.Secret> list = new ArrayList<>();
        for (Map.Entry<String, String> item : signSecretMap.entrySet()) {
            list.add(new SignatureProperties.Secret(item.getKey(), item.getValue()));
        }
        return list;
    }

    /**
     * 获取单个秘钥
     *
     * @param appId
     * @return
     */
    public static SignatureProperties.Secret getSecret(String appId) {
        if (null == appId) {
            return null;
        }
        if (signSecretMap.containsKey(appId)) {
            return new SignatureProperties.Secret(appId, signSecretMap.get(appId));
        }
        return null;
    }

    /**
     * 初始化秘钥
     *
     * @param secrets
     */
    public static void initSecret(List<SignatureProperties.Secret> secrets) {
        signSecretMap.clear();
        secrets.forEach(item -> {
            signSecretMap.put(item.getAppId(), item.getAppSecret());
        });
    }

    /**
     * 追加秘钥
     *
     * @param secret
     */
    public static void appendSecret(SignatureProperties.Secret secret) {
        signSecretMap.put(secret.getAppId(), secret.getAppSecret());
    }

    /**
     * 移除单个秘钥
     *
     * @param appId
     */
    public static void removeSecret(String appId) {
        signSecretMap.remove(appId);
    }

    /**
     * 移除所有秘钥
     */
    public static void removeAllSecret() {
        signSecretMap.clear();
    }

}
