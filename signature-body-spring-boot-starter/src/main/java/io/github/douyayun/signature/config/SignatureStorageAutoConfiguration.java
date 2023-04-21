package io.github.douyayun.signature.config;

import io.github.douyayun.signature.manager.SignatureConfigStorageManager;
import io.github.douyayun.signature.properties.SignatureProperties;
import io.github.douyayun.signature.properties.StorageType;
import io.github.douyayun.signature.storage.ConfigStorage;
import io.github.douyayun.signature.storage.MemoryConfigStorage;
import io.github.douyayun.signature.storage.RedisConfigStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author houp
 * @since 1.0.0
 **/
@Configuration
@Slf4j
public class SignatureStorageAutoConfiguration {

    @Resource
    private SignatureProperties signatureProperties;

    @Autowired(required = false)
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    @ConditionalOnMissingBean(SignatureConfigStorageManager.class)
    public SignatureConfigStorageManager signatureManager() {
        SignatureConfigStorageManager signatureConfigStorageManager = new SignatureConfigStorageManager();
        ConfigStorage configStorage = getConfigStorage();
        signatureConfigStorageManager.setConfigStorage(configStorage);
        return signatureConfigStorageManager;
    }

    @Bean
    public StringRedisTemplate getRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        return stringRedisTemplate;
    }

    private ConfigStorage getConfigStorage() {
        if (signatureProperties.getSecretStorageType() == StorageType.memory) {
            return new MemoryConfigStorage();
        } else if (signatureProperties.getSecretStorageType() == StorageType.redis) {
            return new RedisConfigStorage(getRedisTemplate(redisConnectionFactory));
        }
        return new MemoryConfigStorage();
    }

}
