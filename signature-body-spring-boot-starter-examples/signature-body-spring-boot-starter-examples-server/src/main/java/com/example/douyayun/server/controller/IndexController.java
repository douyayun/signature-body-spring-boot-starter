package com.example.douyayun.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author houp
 * @since 1.0.0
 **/
@RestController
@RequestMapping()
public class IndexController {

    @GetMapping("/")
    public String index() {
        return System.currentTimeMillis() + "";
    }

}
