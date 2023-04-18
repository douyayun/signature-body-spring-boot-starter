package io.github.douyayun.signature.interceptor;

import io.github.douyayun.signature.exception.SignatureException;
import io.github.douyayun.signature.manager.SignatureManager;
import io.github.douyayun.signature.manager.SignatureSecretManager;
import io.github.douyayun.signature.properties.SignatureProperties;
import io.github.douyayun.signature.util.JsonUtils;
import io.github.douyayun.signature.util.RequestUtils;
import io.github.douyayun.signature.util.SignUtils;
import io.github.douyayun.signature.wrapper.RequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
@Slf4j
public class SignatureInterceptor implements HandlerInterceptor {

    /**
     * 签名配置
     */
    private SignatureProperties signatureProperties;

    private SignatureManager signatureManager;

    public SignatureInterceptor(SignatureProperties signatureProperties, SignatureManager signatureManager) {
        this.signatureProperties = signatureProperties;
        this.signatureManager = signatureManager;
        if (signatureProperties.getSecret() != null && !signatureProperties.getSecret().isEmpty()) {
            SignatureSecretManager.initSecret(signatureProperties.getSecret());
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            return false;
        }
        String jsonData = "";
        String method = request.getMethod().toUpperCase();
        String appId = request.getHeader("appId");
        SignatureProperties.Secret secret = SignatureSecretManager.getSecret(appId);
        String appSecret = secret == null ? "" : secret.getAppSecret();
        String timestamp = request.getHeader("timestamp");
        String nonce = request.getHeader("nonce");
        String sign = request.getHeader("sign");
        log.info("signature url：{} method：{} appId：{} timestamp：{} nonce：{} sign：{}",
                request.getRequestURL().toString(), method, appId, timestamp, nonce, sign);
        Assert.isTrue(StringUtils.isNotBlank(appId), "appId不能为空");
        Assert.notNull(secret, "appId不存在");
        Assert.isTrue(StringUtils.isNotBlank(timestamp), "timestamp不能为空");
        Assert.isTrue(StringUtils.isNotBlank(nonce), "nonce不能为空");
        Assert.isTrue(StringUtils.isNotBlank(sign), "sign不能为空");
        if (signatureProperties.isTimestampEnabled()) {
            long _timestamp = NumberUtils.toLong(timestamp);
            Assert.isTrue(_timestamp <= 0, "timestamp错误");
            if (Math.abs(System.currentTimeMillis() - _timestamp) > 1000 * signatureProperties.getTimestampValidityInSeconds()) {
                Assert.isTrue(false, "timestamp已过期,有效期" + signatureProperties.getTimestampValidityInSeconds() + "秒");
            }
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        log.info("signature parameter：" + JsonUtils.toJson(parameterMap));
        String parameterData = SignUtils.sortMapByKey(parameterMap);
        log.info("signature parameter data：" + parameterData);
        if (RequestUtils.isJson(request)) {
            // 获取json字符串
            jsonData = new RequestWrapper(request).getBodyString();
            log.info("signature json data：{}", jsonData);
        }
        String noSign = appId + timestamp + nonce + parameterData + jsonData;
        String signData = SignUtils.getSign(noSign, appSecret);
        log.info("signature 待签名字符串：{},本机签名：{},签名参数：{}", noSign, signData, sign);
        if (!sign.equalsIgnoreCase(signData) && !signatureProperties.isDebug()) {
            throw new SignatureException(1000, "sign签名错误");
        }
        return true;
    }

}
