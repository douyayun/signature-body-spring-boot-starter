package io.github.douyayun.signature.properties;

/**
 * 签名密钥
 */
public class Secret {

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
