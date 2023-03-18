# SpringBoot 接口数据签名组件

概述

此组件支持form、json表单签名

请求头需要添加

appId、timestamp、nonce、sign

appId：应用id

timestamp：毫秒时间戳（1679071882431）

nonce：

sign：

### 依赖

```xml
<dependency>
    <groupId>com.github.douyayun</groupId>
    <artifactId>signature-body-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### `property`配置

```
signature.enabled=true
signature.include-paths=/test1/**,/test2/**
signature.exclude-paths=/echo/**
signature.secret[0].app-id=1621923672504
signature.secret[0].app-secret=f8c30adb67b14bc6a53b29b1de01b150
```

# 默认签名规则：

1、设接口所有请求内容或请求处理结果（body）的数据为集合M，向集合M添加字段signTime，其值为请求时间（格式：yyyyMMddhhmm，精准到分）。
2、将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），按照key:value的格式生成键值对（即key1:value1:key2:value2:key3:value3…）拼接成字符串stringA。
3、在stringA最前与最后都拼接上秘钥signKey（分配得到）得到stringB字符串（signKey:key1:value1:key2:value2…key9:value9:signKey），并对stringB进行MD5运算，再将得到的字符串所有字符转换为大写，得到最终sign值。

# 特别注意以下重要规则：

◆ 参数名ASCII码从小到大排序（字典序）；
◆ 如果参数的值为空不参与签名；
◆ 参数名区分大小写；
◆ 传送的sign参数不参与签名。