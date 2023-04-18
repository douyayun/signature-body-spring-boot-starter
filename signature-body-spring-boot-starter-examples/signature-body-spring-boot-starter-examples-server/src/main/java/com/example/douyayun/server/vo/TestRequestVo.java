package com.example.douyayun.server.vo;

import lombok.*;

import java.io.Serializable;

/**
 * TODO
 *
 * @author: houp
 * @date: 2023/3/18 12:58
 * @version: 1.0.0
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestRequestVo implements Serializable {

    private Integer id;

    private String name;

    private Integer age;

    private String mobile;

}
