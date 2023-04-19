package com.example.douyayun.server.run;

import io.github.douyayun.signature.manager.SignatureSecretManager;
import io.github.douyayun.signature.properties.SignatureProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author houp
 * @since 1.0.0
 **/
@Component
@Slf4j
public class InitSecretRunner implements CommandLineRunner {

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     */
    @Override
    public void run(String... args) {
        List<SignatureProperties.Secret> secrets = new ArrayList<>();
        SignatureProperties.Secret secret = new SignatureProperties.Secret();
        secret.setAppId("1621923672504");
        secret.setAppSecret("f8c30adb67b14bc6a53b29b1de01b150");
        secrets.add(secret);
        SignatureSecretManager.initSecret(secrets);
    }
}
