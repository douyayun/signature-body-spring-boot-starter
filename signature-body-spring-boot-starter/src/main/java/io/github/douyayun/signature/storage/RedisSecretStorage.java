package io.github.douyayun.signature.storage;

import io.github.douyayun.signature.properties.SignatureProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 秘钥存储redis实现
 *
 * @author houp
 * @since 1.0.0
 **/
@Slf4j
public class RedisSecretStorage implements SecretStorage, Serializable {

    private StringRedisTemplate stringRedisTemplate;

    private String key = "signature";

    public RedisSecretStorage(StringRedisTemplate stringRedisTemplate) {
        log.info("RedisSecretStorage...");
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 获取所有秘钥
     *
     * @return
     */
    @Override
    public List<SignatureProperties.Secret> getAllSecret() {
        List<SignatureProperties.Secret> list = new ArrayList<>();
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(key);
        for (Map.Entry<Object, Object> item : entries.entrySet()) {
            list.add(new SignatureProperties.Secret(item.getKey() + "", item.getValue() + ""));
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
        Object value = stringRedisTemplate.opsForHash().get(key, appId);
        if (value != null) {
            return new SignatureProperties.Secret(appId, value + "");
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
        removeAllSecret();
        if (secrets == null) {
            return;
        }
        secrets.forEach(item -> {
            stringRedisTemplate.opsForHash().put(key, item.getAppId(), item.getAppSecret());
        });
    }

    /**
     * 追加秘钥
     *
     * @param secret
     */
    @Override
    public void appendSecret(SignatureProperties.Secret secret) {
        stringRedisTemplate.opsForHash().put(key, secret.getAppId(), secret.getAppSecret());
    }

    /**
     * 移除单个秘钥
     *
     * @param appId
     */
    @Override
    public void removeSecret(String appId) {
        stringRedisTemplate.opsForHash().delete(key, appId);
    }

    /**
     * 移除所有秘钥
     */
    @Override
    public void removeAllSecret() {
        stringRedisTemplate.delete(key);
    }
}
