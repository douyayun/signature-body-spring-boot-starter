package com.example.douyayun.server.controller;

import com.example.douyayun.server.vo.ApiResponse;
import com.example.douyayun.server.vo.TestRequestVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * TODO
 *
 * @author houp
 * @since 1.0.0
 */
@RestController
@RequestMapping("test1")
@Slf4j
public class Test1Controller {

    @GetMapping("get")
    public TestRequestVo get(String[] name, Integer age, String t) {
        TestRequestVo testRequestVo = TestRequestVo.builder().id(12).age(20).mobile("1311111112").name("houp").build();
        // String data = JsonUtils.toJson(testRequestVo);
        // log.info(data);
        return testRequestVo;
    }

    @PostMapping("post/1")
    public ApiResponse post1(String[] name, Integer age, String t) {
        log.info("test1 post1 name：{} age：{} t：{} ...", name, age, t);
        return ApiResponse.success(null);
    }

    @PostMapping("post/2")
    public ApiResponse post2(String t, @RequestBody TestRequestVo testRequestVo) {
        log.info("test1 post2 ：{} t：{} ...", testRequestVo.toString(), t);
        return ApiResponse.success(testRequestVo);
    }

    @PostMapping("post/3")
    public ApiResponse post3(String[] name, Integer age, String t, MultipartFile file) {
        log.info("test1 post3 ：name：{} age：{} t：{} ...", name, age, t);
        String filePath = "";
        String format = System.currentTimeMillis() + "";
        String fileSavePath = "C:\\images\\";
        File folder = new File(fileSavePath + format);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID() + oldName.substring(oldName.lastIndexOf("."), oldName.length());
        try {
            file.transferTo(new File(folder, newName));
            filePath = "/" + format + "/" + newName;
        } catch (IOException e) {
            log.error("上传失败", e);
            throw new RuntimeException("上传失败");
        }
        return ApiResponse.success(filePath);
    }

    @PostMapping("post/4")
    public ApiResponse post4(String t, String t2, String t3, MultipartFile[] files) {
        log.info("test1 post4 ：t：{} t2：{} t3：{} ...", t, t2, t3);
        return ApiResponse.success(t);
    }

}
