package com.example.douyayun.server.controller;

import com.example.douyayun.server.vo.ApiResponse;
import com.example.douyayun.server.vo.TestRequestVo;
import com.github.douyayun.signature.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * TODO
 *
 * @author: houp
 * @date: 2023/3/17 22:02
 * @version: 1.0.0
 */
@RestController
@RequestMapping("test1")
@Slf4j
public class Test1Controller {

    @GetMapping("get")
    public String get() {
        TestRequestVo testRequestVo = TestRequestVo.builder().id(12).age(20).mobile("1311111112").name("houp").build();
        String data = JsonUtils.toJson(testRequestVo);
        log.info(data);
        return data;
    }

    @PostMapping("post/1")
    public ApiResponse post1(String[] name, String age, String t) {
        log.info("test1 post1 name：{} age：{} t：{} ...", name, age, t);
        return ApiResponse.success(null);
    }

    @PostMapping("post/2")
    public ApiResponse post2(String t, @RequestBody TestRequestVo testRequestVo) {
        log.info("test1 post2 ：{} t：{} ...", testRequestVo.toString(), t);
        return ApiResponse.success(testRequestVo);
    }

}
