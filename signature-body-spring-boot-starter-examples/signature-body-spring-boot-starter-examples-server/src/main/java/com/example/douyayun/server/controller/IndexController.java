package com.example.douyayun.server.controller;

import io.github.douyayun.signature.manager.SignatureSecretManager;
import io.github.douyayun.signature.properties.SignatureProperties;
import io.github.douyayun.signature.util.LocalCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * TODO
 *
 * @author houp
 * @since 1.0.0
 **/
@RestController
@RequestMapping()
@Slf4j
public class IndexController {

    private static Long num = 0L;

    @GetMapping("/")
    public String index() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 10000 * 100; i++, num++) {
            String key = "1621923672504:" + UUID.randomUUID();
            String value = "value" + num;
            if (!LocalCache.getInstance().containsKey(key)) {
                LocalCache.getInstance().putValue(key, value, 10);
                // TimeUnit.MILLISECONDS.sleep(1);
            }
        }
        stopWatch.stop();
        return stopWatch.getTotalTimeMillis() + "";
    }

    @GetMapping("/1")
    public String index1() {
        return LocalCache.getInstance().size() + "";
    }

    @GetMapping("/s/a")
    public String index2() {
        SignatureProperties.Secret secret = new SignatureProperties.Secret();
        secret.setAppId("1621923672506");
        secret.setAppSecret("f8c30adb67b14bc6a53b29b1de01b150");
        SignatureSecretManager.appendSecret(secret);
        return SignatureSecretManager.getAllSecret().size() + "";
    }

    @GetMapping("/s/s")
    public String index3() {
        return SignatureSecretManager.getAllSecret().size() + "";
    }

    @GetMapping("/s/r")
    public String index4() {
        SignatureSecretManager.removeAllSecret();
        return SignatureSecretManager.getAllSecret().size() + "";
    }
}
