package com.example.douyayun.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author: houp
 * @date: 2023/3/17 22:02
 * @version: 1.0.0
 */
@RestController
@RequestMapping("test2")
@Slf4j
public class Test2Controller {

    @GetMapping("get")
    public String get() {
        log.info("test2 get...");
        return "test2 get";
    }

}
