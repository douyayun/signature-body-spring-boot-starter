package com.example.douyayun.server.runner;

import io.github.douyayun.signature.properties.SignatureProperties;
import io.github.douyayun.signature.storage.SecretStorage;
import io.github.douyayun.signature.util.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private SignatureProperties signatureProperties;

    @Value("${appId}")
    private String appId;
    @Value("${appSecret}")
    private String appSecret;
    // RSA
    @Value("${publicKey}")
    private String publicKey;
    @Value("${privateKey}")
    private String privateKey;

    @Override
    public void run(String... args) {
        List<SignatureProperties.Secret> secrets = new ArrayList<>();
        SignatureProperties.Secret secret = new SignatureProperties.Secret(appId, appSecret, publicKey);
        secrets.add(secret);
        secretStorage.initSecret(secrets);
        // secretStorage.appendSecret(secret);
        // secretStorage.getSecret("1621923672504");
        // secretStorage.getAllSecret();
        // secretStorage.removeSecret("1621923672504");
        // secretStorage.removeAllSecret();
        String data = "16219236725041682303422099a3e3b7db-eca2-4d54-9aab-a384e436d5bbage=23&name=1,2,3&t=aaaaf8c30adb67b14bc6a53b29b1de01b150";
        String sign = SignUtils.sign(data, privateKey, signatureProperties.getSignType());
        log.info("signType：{} sign：{}", signatureProperties.getSignType(), sign);
    }

}
