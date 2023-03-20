package com.example.douyayun.server.config;

import com.github.douyayun.signature.manager.SignatureManager;
import com.github.douyayun.signature.properties.SignatureProperties;
import com.github.douyayun.signature.storage.impl.DefaultNonceConfigStorageImpl;
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
        log.error("SignatureManager init 22222...");
        SignatureManager signatureManager = new SignatureManager();
        signatureManager.setNonceConfigStorage(new DefaultNonceConfigStorageImpl(signatureProperties));
        return signatureManager;
    }

}