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
     * 签名类型
     */
    private SignType signType = SignType.SM2;

    /**
     * 平台秘钥
     */
    private PlatformKey platformKey;

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

    public SignType getSignType() {
        return signType;
    }

    public void setSignType(SignType signType) {
        this.signType = signType;
    }

    public PlatformKey getPlatformKey() {
        return platformKey;
    }

    public void setPlatformKey(PlatformKey platformKey) {
        this.platformKey = platformKey;
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
     * 平台秘钥
     */
    public static class PlatformKey {

        /**
         * 平台公钥
         */
        private String publicKey;

        /**
         * 平台私钥
         */
        private String privateKey;

        public PlatformKey() {
        }

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }
    }

    /**
     * 签名密钥
     */
    public static class Secret {

        /**
         * 应用id
         */
        private String appId;

        /**
         * 应用密钥
         */
        private String appSecret;

        /**
         * 应用公钥
         */
        private String publicKey;

        // /**
        //  * 应用私钥
        //  */
        // private String privateKey;

        public Secret() {
        }

        public Secret(String appId, String appSecret, String publicKey) {
            this.appId = appId;
            this.appSecret = appSecret;
            this.publicKey = publicKey;
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

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }
    }

}
