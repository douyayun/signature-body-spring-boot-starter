# SpringBoot 接口数据签名组件

### 概述
此组件支持query、form、json表单签名<br>
请求头需添加以下参数<br>

| 参数名称  | 必填 | 说明                           | 示例                                 |
| --------- | ---- | ------------------------------ | ------------------------------------ |
| appId     | 是   | 应用Id                         | 1621923672504                        |
| timestamp | 是   | 请求时间戳（秒）               | 1681805978                           |
| nonce     | 是   | 每次请求随机生成，需要全局唯一 | 8091a230-8c4f-4d6f-b4be-6c585af6c8ad |
| sign      | 是   | 签名                           | ee242f43922dc3f70383a1c17ecaf169     |

### 依赖
```xml
<dependency>
    <groupId>io.github.douyayun</groupId>
    <artifactId>signature-body-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

#### `property`配置

```
signature.enabled=true
signature.include-paths=/test1/**,/test2/**
signature.exclude-paths=/echo/**
signature.secret[0].app-id=1621923672504
signature.secret[0].app-secret=f8c30adb67b14bc6a53b29b1de01b150
signature.secret[1].app-id=1621923672505
signature.secret[1].app-secret=f8c30adb67b14bc6a53b29b1de01b150
```

# sign签名规则：

1、设接口所有请求内容或请求处理结果（body）的数据为集合M，向集合M添加字段signTime，其值为请求时间（格式：yyyyMMddhhmm，精准到分）。<br>
2、将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），按照key:value的格式生成键值对（即key1:value1:key2:value2:key3:value3…）拼接成字符串stringA。<br>
3、在stringA最前与最后都拼接上秘钥signKey（分配得到）得到stringB字符串（signKey:key1:value1:key2:value2…key9:value9:signKey），并对stringB进行MD5运算，再将得到的字符串所有字符转换为大写，得到最终sign值。<br>

# 特别注意以下重要规则：

◆ 参数名ASCII码从小到大排序（字典序）；<br>
◆ 如果参数的值为空不参与签名；<br>
◆ 参数名区分大小写；<br>
◆ 传送的sign参数不参与签名。<br>