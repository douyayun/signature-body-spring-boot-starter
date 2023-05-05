# SpringBoot 接口数据签名组件

### 概述

此组件支持query、form、json表单签名<br>
请求头需添加以下参数<br>

| 参数名称  | 必填 | 说明                          | 示例                                 |
| --------- | ---- | ----------------------------- | ------------------------------------ |
| appId     | 是   | 应用Id                        | 1621923672504                        |
| timestamp | 是   | 请求时间戳（毫秒）            | 1682303422099              |
| nonce     | 是   | 每次请求随机生成，需全局唯一 | 8091a230-8c4f-4d6f-b4be-6c585af6c8ad |
| sign      | 是   | 签名                          | ee242f43922dc3f70383a1c17ecaf169     |

### 依赖

```xml

<dependency>
    <groupId>io.github.douyayun</groupId>
    <artifactId>signature-body-spring-boot-starter</artifactId>
    <version>1.0.5</version>
</dependency>
        <!-- SM2模式需要使用以下依赖 -->
<dependency>
<groupId>org.bouncycastle</groupId>
<artifactId>bcpkix-jdk15to18</artifactId>
<version>1.72</version>
</dependency>
```

#### `property`配置

signature.sign-type支持MD5、RSA、SM2

```
# signature
signature.debug=false
signature.enabled=true
signature.timestamp-enabled=true
signature.timestamp-validity-in-seconds=30000
signature.sign-type=rsa
signature.include-paths=/test1/**,/test2/**
signature.exclude-paths=/echo/**
signature.secret-storage-type=redis
# redis
spring.redis.host=127.0.0.1
spring.redis.port=6379
spring.redis.password=123456
spring.redis.database=0
spring.redis.lettuce.pool.max-active=20
spring.redis.lettuce.pool.max-idle=10
spring.redis.lettuce.pool.min-idle=5
spring.redis.lettuce.pool.max-wait=5000ms
```

### 应用秘钥外部初始化

```java
import io.github.douyayun.signature.properties.SignatureProperties;
import io.github.douyayun.signature.storage.SecretStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化秘钥信息
 *
 * @author houp
 * @since 1.0.0
 **/
@Component
@Slf4j
public class InitSignatureSecretRunner implements CommandLineRunner {

    @Autowired
    private SecretStorage secretStorage;

    @Override
    public void run(String... args) {
        List<Secret> secrets = new ArrayList<>();
        Secret secret = new Secret(appId, this.appSecret, publicKey);
        secrets.add(secret);
        secretStorage.initSecret(secrets);
        // secretStorage.appendSecret(secret);
        // secretStorage.getSecret("1621923672504");
        // secretStorage.getAllSecret();
        // secretStorage.removeSecret("1621923672504");
        // secretStorage.removeAllSecret();
    }

}
```

```
#秘钥配置
appId=1621923672504
appSecret=f8c30adb67b14bc6a53b29b1de01b150
# RSA
publicKey=MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlR4kurbOkZDw0mFSv7nLKwCDtgfxFifSR0y22LN/u/I45xny2KJyFwS5VRoJt8kSQ+6rAypnqABkTfAefbEGTQzAmRQRCKBdw4l7z0xQ5bOszRo+W1EcRr9DZSbZk4paD7Lz9ofBdkAWtitMWngW36r4Hxxk7h1gIdFzOw3HzFj5C5/bch65Nf9IsdalzPLbaNZrdW9c6+sGaNUxGDszURI8OXUtwX8jMozilHqbiPwKa64RhEz+KIc6PQGj3I2v3uH+yttPtks9RjTKLO3SiYSao2Uf5jLG5BpncvNONuR2Fx5OmNLRW/NJSRM8Hk4udEy42uTOXF/2HLQPWCO7wwIDAQAB
privateKey=MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCVHiS6ts6RkPDSYVK/ucsrAIO2B/EWJ9JHTLbYs3+78jjnGfLYonIXBLlVGgm3yRJD7qsDKmeoAGRN8B59sQZNDMCZFBEIoF3DiXvPTFDls6zNGj5bURxGv0NlJtmTiloPsvP2h8F2QBa2K0xaeBbfqvgfHGTuHWAh0XM7DcfMWPkLn9tyHrk1/0ix1qXM8tto1mt1b1zr6wZo1TEYOzNREjw5dS3BfyMyjOKUepuI/AprrhGETP4ohzo9AaPcja/e4f7K20+2Sz1GNMos7dKJhJqjZR/mMsbkGmdy80425HYXHk6Y0tFb80lJEzweTi50TLja5M5cX/YctA9YI7vDAgMBAAECggEAWcEuDHCyP24Fdiv8WcikIHsLzEpVc6d+a0oimKbaUK9ITtoQkRKeJ3rBbxbgGI5ffyIuLxacE8RAm6TXRYKDL2domfs5EX8tUN/09N6Tpt8SONX+CIr2wSKolz7eIW9aLC7jp1UpPlrvELFZIO89nV1UQa2elrB5x4FBoZ72zukg853QrgPRcwVyxzUy6lzM14cab0JbIxR83bgx9oHsRD6AHCGJurkrQqiZahiRXX8aCBCCB7+FJm3fWc1dHta+SV1+beblIOM/0IJ4LLKvZ+dY1RhJbHhr2Ms+9WrojqiaXZlljbAiCbn/lbsbx2x9Qy58paXKVjLLOA2ludlUKQKBgQD4GINwoDS9cYCvY8d4T36cgeTcav2Sk9vaNzDqcy4nVuvztBMlueFXs/1/oh/9jkLG3j/PhaZBwCJJVsbaYz1hSxnttg0XJ3JC9EkgbyVLIoFa58ceRc5cXu6NEy7o2m7htdzrUYx54tRblJVM0WnK8GLRfKmTCx6jNhhW8mZ15wKBgQCZ3lu/n0wk2uJ9a7r+A5yGrSIuE1Y2iAH8Yf8Hv4/tK1sqbJGLI87yudGTHzKzzCMvh9VDE6ynvq51Pw5pKkUQ4Pi/WN/GglHowP8sVuIarspk7C99UnpIzgE8lCcuwMNsdC5VP0FpSc9+CcZ8jVpQwxsQhL65/YYhsl3tNsrXxQKBgB/QZOqIYLr6VxomnWmcKAxBy+tH/QRS2Zp+AVPPOVfqGY/Cw8HOuEra1eMMEzYcjAucbGpcJRnBXaQx8IYIu9JCIy0Mb1lMwaY8VTRHjGy6HARgPHRxVykZM1X748QQXq+q9o1mg7ENmVW1FAApynsvoDxk1An7Hw6m3TApHWJ5AoGBAJAsEC3BdhhGW9gG/GQMQDrUuOURS0O3Np0juVGqRZKFgFuE9O65s6wtPliq79gWW5WqocHDCpD2kwLa/Ya+i45gLdMk1DBtZZBAXhhOpHc59fyQbYLwqBHVrMT1UK13AuV5CvznU0PzXA8N28RSsiBgX9ifeRJOocKrHWUqZO+9AoGAGCYyrolYQwUzpdKnHIAwPEfcnHV8gGicM8taMnaRx2q2fBmlNeH92fs+zPE1wYDWDr4RZSd1Oao8XibfG6mj5NAO+B6RYhdQZHga+vYuWSBAPvyUoeZ5hcKeey/Nz4fm3Jd2i3ge49etQuf974KOUqsMTVE6Ce31LZ4BMtzNHUM=
# SM2
publicKey=MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEeKm6cIoSzj98papjjOo0bQDeoEe6WkQg/K0z3GuSjJHaNRdxwT7556SHKEJiZGGo/bV9f9cn/Bvjiqi2/dnxmA==
privateKey=MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgl4mxsd4R1EI+bldkg9NVrHeq8Aleomk4aWQfmGvWszegCgYIKoEcz1UBgi2hRANCAAR4qbpwihLOP3ylqmOM6jRtAN6gR7paRCD8rTPca5KMkdo1F3HBPvnnpIcoQmJkYaj9tX1/1yf8G+OKqLb92fGY

```

### 签名规则

```java
 String noSign=appId+timestamp+nonce+parameterData+jsonData+secret;
        String sign=Md5(noSign);
```

1、parameterData为请求参数包含url中的query和form表单中的参数，并按照参数名ASCII码从小到大排序，按照key=value的格式生成键值对（即key1=value1&key2=value2&key3=value3）拼接成字符串parameterData<br>
2、jsonData为请求体中的json数据，此数据原封不动的通过接口传递至接口中<br>

### sign签名示例

| 参数名称  | 说明                           | 示例值                               |
| --------- | ------------------------------ | ------------------------------------ |
| appId     | 应用Id                         | 1621923672504                        |
| timestamp | 请求时间戳（秒）               | 1682303422099                        |
| nonce     | 每次请求随机生成，需要全局唯一 | 8091a230-8c4f-4d6f-b4be-6c585af6c8ad |
| secret | 应用秘钥                       | f8c30adb67b14bc6a53b29b1de01b150     |

#### (1)、get请求

http://localhost:8000/test1/get?name=1,2,3&age=23&t=aaaa
![img.png](docs/img/get_1.png)
![img.png](docs/img/get_2.png)

```
待签名字符串：162192367250416818059788091a230-8c4f-4d6f-b4be-6c585af6c8adage=23&name=1,2,3&t=aaaaf8c30adb67b14bc6a53b29b1de01b150
签名：789726dea11c827f65e66981362a0d1a
```

#### (2)、post请求 form-data

http://localhost:8000/test1/post/1?t=aaaa

![img.png](docs/img/post_form-data_1.png)
![img.png](docs/img/post_form-data_2.png)

```
待签名字符串：162192367250416818059788091a230-8c4f-4d6f-b4be-6c585af6c8adage=23&name=1,2,3&t=aaaaf8c30adb67b14bc6a53b29b1de01b150
签名：789726dea11c827f65e66981362a0d1a
```

#### (3)、post请求 x-www-form-urlencoded

http://localhost:8000/test1/post/1?t=aaaa
![img.png](docs/img/post_x-www-form-urlencoded_1.png)
![img.png](docs/img/post_x-www-form-urlencoded_2.png)

```
待签名字符串：162192367250416818059788091a230-8c4f-4d6f-b4be-6c585af6c8adage=23&name=1,2,3&t=aaaaf8c30adb67b14bc6a53b29b1de01b150
签名：789726dea11c827f65e66981362a0d1a
```

#### (4)、post请求 application/json

http://localhost:8000/test1/post/2?t=aaaa
![img.png](docs/img/post_json_1.png)
![img.png](docs/img/post_json_2.png)

```
待签名字符串：162192367250416818059788091a230-8c4f-4d6f-b4be-6c585af6c8adt=aaaa{"id":123,"name":"zhangsan","age":23,"mobile":"13111111111","hobby":["篮球","足球"]}f8c30adb67b14bc6a53b29b1de01b150
签名：22c6d0584016bd2a5937b5b95cc06c87
```

### 特别注意以下重要规则：

◆ 参数名ASCII码从小到大排序（字典序）<br>
◆ 参数名区分大小写<br>

### 加密解密在线工具

https://the-x.cn/cryptography/Sm2.aspx