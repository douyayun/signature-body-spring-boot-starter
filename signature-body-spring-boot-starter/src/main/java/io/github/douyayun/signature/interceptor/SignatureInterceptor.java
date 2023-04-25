package io.github.douyayun.signature.interceptor;

import io.github.douyayun.signature.exception.SignatureException;
import io.github.douyayun.signature.properties.Secret;
import io.github.douyayun.signature.properties.SignatureProperties;
import io.github.douyayun.signature.storage.NonceStorage;
import io.github.douyayun.signature.storage.SecretStorage;
import io.github.douyayun.signature.util.JsonUtils;
import io.github.douyayun.signature.util.RequestUtils;
import io.github.douyayun.signature.util.SignUtils;
import io.github.douyayun.signature.wrapper.RequestWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 签名拦截器
 *
 * @author houp
 * @since 1.0.0
 */
public class SignatureInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(SignatureInterceptor.class);

    /**
     * 签名配置
     */
    private SignatureProperties signatureProperties;

    private SecretStorage secretStorage;

    private NonceStorage nonceStorage;

    // private SignatureConfigStorageManager signatureConfigStorageManager;

    public SignatureInterceptor(SignatureProperties signatureProperties, NonceStorage nonceStorage, SecretStorage secretStorage) {
        this.signatureProperties = signatureProperties;
        this.nonceStorage = nonceStorage;
        this.secretStorage = secretStorage;
        // if (signatureProperties.getSecret() != null && !signatureProperties.getSecret().isEmpty()) {
        //     secretStorage.initSecret(signatureProperties.getSecret());
        // }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            return false;
        }
        String jsonData = "";
        String method = request.getMethod().toUpperCase();
        String appId = request.getHeader("appId");
        Secret secret = secretStorage.getSecret(appId);
        String appSecret = secret == null ? "" : secret.getAppSecret();
        String timestamp = request.getHeader("timestamp");
        String nonce = request.getHeader("nonce");
        String sign = request.getHeader("sign");
        log.info("signature url：{} method：{} appId：{} timestamp：{} nonce：{} sign：{}", request.getRequestURL().toString(), method, appId, timestamp, nonce, sign);
        Assert.isTrue(StringUtils.isNotBlank(appId), "appId不能为空");
        Assert.notNull(secret, "appId不存在");
        Assert.isTrue(StringUtils.isNotBlank(timestamp), "timestamp不能为空(毫秒)");
        Assert.isTrue(StringUtils.isNotBlank(nonce), "nonce不能为空");
        Assert.isTrue(StringUtils.isNotBlank(sign), "sign不能为空");
        if (signatureProperties.isTimestampEnabled()) {
            long _timestamp = NumberUtils.toLong(timestamp);
            Assert.isTrue(_timestamp > 0, "timestamp错误(毫秒)");
            if (Math.abs(System.currentTimeMillis() - _timestamp) > 1000 * signatureProperties.getTimestampValidityInSeconds()) {
                Assert.isTrue(false, "timestamp已过期,有效期" + signatureProperties.getTimestampValidityInSeconds() + "秒");
            }
        }
        Assert.isTrue(nonceStorage.uniqueRequest(appId, nonce, signatureProperties.getTimestampValidityInSeconds()), "nonce不能重复使用");
        Map<String, String[]> parameterMap = request.getParameterMap();
        log.info("signature parameter：" + JsonUtils.toJson(parameterMap));
        String parameterData = SignUtils.sortMapByKey(parameterMap);
        log.info("signature parameter data：" + parameterData);
        if (RequestUtils.isJson(request)) {
            // 获取json字符串
            jsonData = new RequestWrapper(request).getBodyString();
            log.info("signature json data：{}", jsonData);
        }
        String noSign = appId + timestamp + nonce + parameterData + jsonData + appSecret;
        log.info("signature 待签名字符串：{},签名参数：{}", noSign, sign);
        if (!SignUtils.verify(noSign, secret.getPublicKey(), sign, signatureProperties.getSignType())
                && !signatureProperties.isDebug()) {
            throw new SignatureException("sign签名错误");
        }
        return true;
    }

}
