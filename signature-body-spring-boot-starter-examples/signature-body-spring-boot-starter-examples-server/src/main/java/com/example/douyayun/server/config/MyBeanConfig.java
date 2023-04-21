package com.example.douyayun.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MyBeanConfig {

    // @Resource
    // private SignatureProperties signatureProperties;
    //
    // @Bean
    // public SignatureManager signatureManager() {
    //     log.error("MyBeanConfig SignatureManager init ...");
    //     SignatureManager signatureManager = new SignatureManager();
    //     signatureManager.setConfigStorage(new RedisConfigStorage(signatureProperties));
    //     return signatureManager;
    // }

}
