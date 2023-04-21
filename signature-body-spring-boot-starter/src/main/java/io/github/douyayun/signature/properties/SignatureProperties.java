package io.github.douyayun.signature.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 签名配置
 *
 * @author houp
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "signature")
public class SignatureProperties {

    /**
     * 是否开发调试模式
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
     * 秘钥存储类型
     */
    private StorageType secretStorageType = StorageType.memory;

    /**
     * 签名密钥
     */
    private List<Secret> secret;

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isTimestampEnabled() {
        return timestampEnabled;
    }

    public void setTimestampEnabled(boolean timestampEnabled) {
        this.timestampEnabled = timestampEnabled;
    }

    public int getTimestampValidityInSeconds() {
        return timestampValidityInSeconds;
    }

    public void setTimestampValidityInSeconds(int timestampValidityInSeconds) {
        this.timestampValidityInSeconds = timestampValidityInSeconds;
    }

    public List<String> getIncludePaths() {
        return includePaths;
    }

    public void setIncludePaths(List<String> includePaths) {
        this.includePaths = includePaths;
    }

    public List<String> getExcludePaths() {
        return excludePaths;
    }

    public void setExcludePaths(List<String> excludePaths) {
        this.excludePaths = excludePaths;
    }

    public StorageType getSecretStorageType() {
        return secretStorageType;
    }

    public void setSecretStorageType(StorageType secretStorageType) {
        this.secretStorageType = secretStorageType;
    }

    public List<Secret> getSecret() {
        return secret;
    }

    public void setSecret(List<Secret> secret) {
        this.secret = secret;
    }

    /**
     * 签名密钥
     */
    public static class Secret {

        /**
         * 客户端id
         */
        private String appId;

        /**
         * 签名密钥
         */
        private String appSecret;

        public Secret() {
        }

        public Secret(String appId, String appSecret) {
            this.appId = appId;
            this.appSecret = appSecret;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAppSecret() {
            return appSecret;
        }

        public void setAppSecret(String appSecret) {
            this.appSecret = appSecret;
        }
    }

}
