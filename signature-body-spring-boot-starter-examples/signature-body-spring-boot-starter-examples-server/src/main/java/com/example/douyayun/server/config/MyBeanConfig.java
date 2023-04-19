package com.example.douyayun.server.config;

import io.github.douyayun.signature.manager.SignatureManager;
import io.github.douyayun.signature.properties.SignatureProperties;
import io.github.douyayun.signature.storage.impl.RedisConfigStorageImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@Slf4j
public class MyBeanConfig {

    @Resource
    private SignatureProperties signatureProperties;

    @Bean
    public SignatureManager signatureManager() {
        log.error("MyBeanConfig SignatureManager init ...");
        SignatureManager signatureManager = new SignatureManager();
        signatureManager.setConfigStorage(new RedisConfigStorageImpl(signatureProperties));
        return signatureManager;
    }

}
