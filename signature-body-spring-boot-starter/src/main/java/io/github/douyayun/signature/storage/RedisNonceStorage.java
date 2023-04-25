package io.github.douyayun.signature.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author houp
 * @since 1.0.0
 */
public class RedisNonceStorage implements NonceStorage, Serializable {
    private static final Logger log = LoggerFactory.getLogger(RedisNonceStorage.class);

    private StringRedisTemplate stringRedisTemplate;

    public RedisNonceStorage(StringRedisTemplate stringRedisTemplate) {
        // log.info("RedisConfigStorage...");
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 唯一请求  锁定当前appId的nonce. 防止重放攻击
     *
     * @param appId            appId值
     * @param nonce            nonce值
     * @param expiresInSeconds 过期时间，以秒为单位
     * @return 返回true表示可以使用；false表示已经存在，不能使用
     */
    @Override
    public boolean uniqueRequest(String appId, String nonce, int expiresInSeconds) {
        String key = "signature:" + appId + ":" + nonce;
        if (stringRedisTemplate.opsForValue().setIfAbsent(key, "", expiresInSeconds, TimeUnit.SECONDS)) {
            return true;
        }
        return false;
    }
}
