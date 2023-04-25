package io.github.douyayun.signature.storage;

import io.github.douyayun.signature.properties.Secret;
import io.github.douyayun.signature.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class RedisSecretStorage implements SecretStorage, Serializable {
    private static final Logger log = LoggerFactory.getLogger(RedisSecretStorage.class);

    private StringRedisTemplate stringRedisTemplate;

    private String key = "signature:secret";

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
    public List<Secret> getAllSecret() {
        List<Secret> list = new ArrayList<>();
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(key);
        for (Map.Entry<Object, Object> item : entries.entrySet()) {
            list.add(JsonUtils.fromJSON(item.getValue() + "", Secret.class));
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
        Object value = stringRedisTemplate.opsForHash().get(key, appId);
        if (value != null) {
            return JsonUtils.fromJSON(value + "", Secret.class);
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
        removeAllSecret();
        if (secrets == null) {
            return;
        }
        secrets.forEach(item -> {
            stringRedisTemplate.opsForHash().put(key, item.getAppId(), JsonUtils.toJson(item));
        });
    }

    /**
     * 追加秘钥
     *
     * @param secret
     */
    @Override
    public void appendSecret(Secret secret) {
        stringRedisTemplate.opsForHash().put(key, secret.getAppId(), JsonUtils.toJson(secret));
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
