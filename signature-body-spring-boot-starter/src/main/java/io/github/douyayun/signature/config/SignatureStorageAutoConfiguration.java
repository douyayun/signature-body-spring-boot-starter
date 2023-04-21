package io.github.douyayun.signature.config;

import io.github.douyayun.signature.properties.SignatureProperties;
import io.github.douyayun.signature.properties.StorageType;
import io.github.douyayun.signature.storage.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SignatureStorageAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(SignatureStorageAutoConfiguration.class);

    @Resource
    private SignatureProperties signatureProperties;

    @Autowired(required = false)
    private RedisConnectionFactory redisConnectionFactory;

    // @Bean
    // @ConditionalOnMissingBean(SignatureConfigStorageManager.class)
    // public SignatureConfigStorageManager signatureManager() {
    //     SignatureConfigStorageManager signatureConfigStorageManager = new SignatureConfigStorageManager();
    //     signatureConfigStorageManager.setNonceStorage(getNonceStorage());
    //     SecretStorage secretStorage = getSecretStorage();
    //     signatureConfigStorageManager.setSecretStorage(secretStorage);
    //     SignatureSecretManager.secretStorage = secretStorage;
    //     return signatureConfigStorageManager;
    // }

    @Bean
    public StringRedisTemplate getRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        return stringRedisTemplate;
    }

    @Bean
    public NonceStorage getNonceStorage() {
        if (signatureProperties.getSecretStorageType() == StorageType.memory) {
            return new MemoryNonceStorage();
        } else if (signatureProperties.getSecretStorageType() == StorageType.redis) {
            return new RedisNonceStorage(getRedisTemplate(redisConnectionFactory));
        }
        return new MemoryNonceStorage();
    }

    @Bean
    public SecretStorage getSecretStorage() {
        if (signatureProperties.getSecretStorageType() == StorageType.memory) {
            return new MemorySecretStorage();
        } else if (signatureProperties.getSecretStorageType() == StorageType.redis) {
            return new RedisSecretStorage(getRedisTemplate(redisConnectionFactory));
        }
        return new MemorySecretStorage();
    }

}
