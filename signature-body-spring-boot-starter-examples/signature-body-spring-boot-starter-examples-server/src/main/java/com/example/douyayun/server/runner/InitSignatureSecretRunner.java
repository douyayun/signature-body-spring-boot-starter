package com.example.douyayun.server.runner;

import io.github.douyayun.signature.properties.SignatureProperties;
import io.github.douyayun.signature.storage.SecretStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化秘钥信息
 *
 * @author houp
 * @since 1.0.0
 **/
@Component
@Slf4j
public class InitSignatureSecretRunner implements CommandLineRunner {

    @Autowired
    private SecretStorage secretStorage;

    @Override
    public void run(String... args) {
        List<SignatureProperties.Secret> secrets = new ArrayList<>();
        SignatureProperties.Secret secret = new SignatureProperties.Secret(
                "1621923672504",
                "f8c30adb67b14bc6a53b29b1de01b150",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAi2Qlcr5vc430gUflUaXcLWGHDXtWQYhwzzPlFd4MHry11/HH37QonYI+37m7anPBiF0PtA7nL2RxGT1n65BbzaPY9duw8ozHxfmWL1CDmJCD+Bmhc9IEZWat1rklJ7oGaEkwrbA8h6rCCkmF/rbB2oJNYhgga6jgswMalgC+v4attscteuJtNnhh7ZE7zKmGwNWNBFKc2eKb3403l6aquYmttROWSQ1vCDx9v1Afx85sjVLed7hw2xqOgTWB2T6ftJK/iXjwlOPANkAe5rtuJ1+Moun1i48uOLEVDgfOWds7w58eRo5PNKTK2YDa6DUvThSS6d8SpPuvqsY4UlLT2wIDAQAB");
        secrets.add(secret);
        secretStorage.initSecret(secrets);
        // secretStorage.appendSecret(secret);
        // secretStorage.getSecret("1621923672504");
        // secretStorage.getAllSecret();
        // secretStorage.removeSecret("1621923672504");
        // secretStorage.removeAllSecret();
    }

}
