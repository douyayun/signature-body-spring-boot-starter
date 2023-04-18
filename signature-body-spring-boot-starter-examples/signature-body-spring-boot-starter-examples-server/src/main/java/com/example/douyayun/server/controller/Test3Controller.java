package com.example.douyayun.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author houp
 * @since 1.0.0
 */
@RestController
@RequestMapping("test3")
@Slf4j
public class Test3Controller {

    @GetMapping("get")
    public String get() {
        log.info("test3 get...");
        return "test3 get";
    }

}
