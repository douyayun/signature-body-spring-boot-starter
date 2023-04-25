package io.github.douyayun.signature.storage;

import io.github.douyayun.signature.properties.Secret;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger log = LoggerFactory.getLogger(MemorySecretStorage.class);

    /**
     * 签名密钥
     */
    private static Map<String, Secret> signSecretMap = new ConcurrentHashMap<>();

    /**
     * 获取所有秘钥
     *
     * @return
     */
    @Override
    public List<Secret> getAllSecret() {
        List<Secret> list = new ArrayList<>();
        for (Map.Entry<String, Secret> item : signSecretMap.entrySet()) {
            list.add(item.getValue());
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
    public Secret getSecret(String appId) {
        if (null == appId) {
            return null;
        }
        if (signSecretMap.containsKey(appId)) {
            return signSecretMap.get(appId);
        }
        return null;
    }

    /**
     * 初始化秘钥
     *
     * @param secrets
     */
    @Override
    public void initSecret(List<Secret> secrets) {
        signSecretMap.clear();
        if (secrets == null) {
            return;
        }
        secrets.forEach(item -> {
            signSecretMap.put(item.getAppId(), item);
        });
    }

    /**
     * 追加秘钥
     *
     * @param secret
     */
    @Override
    public void appendSecret(Secret secret) {
        signSecretMap.put(secret.getAppId(), secret);
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
