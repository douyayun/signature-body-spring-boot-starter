package com.example.douyayun.server.controller;

import io.github.douyayun.signature.properties.Secret;
import io.github.douyayun.signature.storage.SecretStorage;
import io.github.douyayun.signature.util.LocalCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SecretStorage secretStorage;

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
    public String appendSecret() {
        Secret secret = new Secret();
        secret.setAppId("1621923672506");
        secret.setAppSecret("f8c30adb67b14bc6a53b29b1de01b506");
        secret.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAi2Qlcr5vc430gUflUaXcLWGHDXtWQYhwzzPlFd4MHry11/HH37QonYI+37m7anPBiF0PtA7nL2RxGT1n65BbzaPY9duw8ozHxfmWL1CDmJCD+Bmhc9IEZWat1rklJ7oGaEkwrbA8h6rCCkmF/rbB2oJNYhgga6jgswMalgC+v4attscteuJtNnhh7ZE7zKmGwNWNBFKc2eKb3403l6aquYmttROWSQ1vCDx9v1Afx85sjVLed7hw2xqOgTWB2T6ftJK/iXjwlOPANkAe5rtuJ1+Moun1i48uOLEVDgfOWds7w58eRo5PNKTK2YDa6DUvThSS6d8SpPuvqsY4UlLT2wIDAQAB");
        secretStorage.appendSecret(secret);
        return secretStorage.getAllSecret().size() + "";
    }

    @GetMapping("/s/removeAllSecret")
    public String removeAllSecret() {
        secretStorage.removeAllSecret();
        return secretStorage.getAllSecret().size() + "";
    }

    @GetMapping("/s/s")
    public String index3() {
        return secretStorage.getAllSecret().size() + "";
    }

    @GetMapping("/s/r")
    public String index4() {
        secretStorage.removeAllSecret();
        return secretStorage.getAllSecret().size() + "";
    }
}
