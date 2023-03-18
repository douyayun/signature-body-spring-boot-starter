package com.example.douyayun.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author: houp
 * @date: 2023/3/18 0:44
 * @version: 1.0.0
 */
@RestController
@RequestMapping("echo")
public class EchoController {

    @GetMapping("time")
    public String time() {
        return System.currentTimeMillis() + "";
    }

    @GetMapping("append")
    public String appent() {
//        SignatureSecretManager.appendSecret(new SignatureProperties.Secret("1", "1"));
        return System.currentTimeMillis() + "";
    }

}
