package com.github.douyayun.signature.config;

import com.github.douyayun.signature.manager.SignatureManager;
import com.github.douyayun.signature.properties.SignatureProperties;
import com.github.douyayun.signature.storage.impl.DefaultNonceConfigStorageImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author: houp
 * @date: 2023/3/20 21:00
 * @version: 1.0.0
 */
@Configuration
@Slf4j
public class SignatureConfig {

    @Resource
    private SignatureProperties signatureProperties;

    @Bean
    @ConditionalOnMissingBean(SignatureManager.class)
    public SignatureManager signatureManager() {
        log.error("SignatureManager init...");
        SignatureManager signatureManager = new SignatureManager();
        DefaultNonceConfigStorageImpl defaultNonceConfigStorage = new DefaultNonceConfigStorageImpl(signatureProperties);
        signatureManager.setNonceConfigStorage(defaultNonceConfigStorage);
        return signatureManager;
    }

}
