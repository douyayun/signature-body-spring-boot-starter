package io.github.douyayun.signature.storage;

import io.github.douyayun.signature.properties.SignatureProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 秘钥存储本地内存实现
 *
 * @author houp
 * @since 1.0.0
 **/
public class MemorySecretStorage implements SecretStorage, Serializable {

    /**
     * 签名密钥
     */
    private static Map<String, String> signSecretMap = new ConcurrentHashMap<>();

    /**
     * 获取所有秘钥
     *
     * @return
     */
    @Override
    public List<SignatureProperties.Secret> getAllSecret() {
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
    @Override
    public SignatureProperties.Secret getSecret(String appId) {
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
    @Override
    public void initSecret(List<SignatureProperties.Secret> secrets) {
        signSecretMap.clear();
        if (secrets == null) {
            return;
        }
        secrets.forEach(item -> {
            signSecretMap.put(item.getAppId(), item.getAppSecret());
        });
    }

    /**
     * 追加秘钥
     *
     * @param secret
     */
    @Override
    public void appendSecret(SignatureProperties.Secret secret) {
        signSecretMap.put(secret.getAppId(), secret.getAppSecret());
    }

    /**
     * 移除单个秘钥
     *
     * @param appId
     */
    @Override
    public void removeSecret(String appId) {
        signSecretMap.remove(appId);
    }

    /**
     * 移除所有秘钥
     */
    @Override
    public void removeAllSecret() {
        signSecretMap.clear();
    }

}
