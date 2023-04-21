package io.github.douyayun.signature.storage;

import io.github.douyayun.signature.properties.SignatureProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author houp
 * @since 1.0.0
 */
@Slf4j
public class RedisConfigStorage implements ConfigStorage, Serializable {

    private SignatureProperties signatureProperties;

    private StringRedisTemplate stringRedisTemplate;

    public RedisConfigStorage(SignatureProperties signatureProperties, StringRedisTemplate stringRedisTemplate) {
        log.info("RedisNonceConfigStorageImpl...");
        this.signatureProperties = signatureProperties;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 获取票据  锁定当前appId的nonce. 防止重放攻击
     *
     * @param appId            appId值
     * @param nonce            nonce值
     * @param expiresInSeconds 过期时间，以秒为单位
     * @return 返回true表示可以使用；false表示已经存在，不能使用
     */
    @Override
    public boolean getTicket(String appId, String nonce, int expiresInSeconds) {
        String key = "signature:" + appId + ":" + nonce;
        String value = stringRedisTemplate.opsForValue().get(key);
        if (stringRedisTemplate.opsForValue().setIfAbsent(key, "", expiresInSeconds, TimeUnit.SECONDS)) {
            // if (StringUtils.isBlank(value)) {
            // stringRedisTemplate.opsForValue().set(key, "1", expiresInSeconds, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }
}
