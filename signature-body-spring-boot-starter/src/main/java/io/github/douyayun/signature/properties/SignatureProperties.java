package io.github.douyayun.signature.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 签名配置
 *
 * @author houp
 * @since 1.0.0
 */
@Data
@ToString
@ConfigurationProperties(prefix = "signature")
public class SignatureProperties {

    /**
     * 是否启用
     */
    private boolean debug = false;

    /**
     * 是否启用
     */
    private boolean enabled = false;

    /**
     * 启用时间戳
     */
    private boolean timestampEnabled = true;

    /**
     * 时间戳有效性(单位秒) 默认5分钟
     */
    private int timestampValidityInSeconds = 60 * 5;

    /**
     * 验证路径
     */
    private List<String> includePaths;

    /**
     * 排除路径
     */
    private List<String> excludePaths;

    /**
     * 签名密钥
     */
    private List<Secret> secret;

    /**
     * 签名密钥
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Secret {

        /**
         * 客户端id
         */
        private String appId;

        /**
         * 签名密钥
         */
        private String appSecret;

    }

}
