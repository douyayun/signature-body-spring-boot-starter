package io.github.douyayun.signature;

import io.github.douyayun.signature.config.SignatureStorageAutoConfiguration;
import io.github.douyayun.signature.config.SignatureWebAutoConfiguration;
import io.github.douyayun.signature.interceptor.SignatureInterceptor;
import io.github.douyayun.signature.properties.SignatureProperties;
import io.github.douyayun.signature.storage.NonceStorage;
import io.github.douyayun.signature.storage.SecretStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * 签名自动配置
 *
 * @author houp
 * @since 1.0.0
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(SignatureInterceptor.class)
@EnableConfigurationProperties(SignatureProperties.class)
@ConditionalOnProperty(prefix = "signature", value = "enabled", havingValue = "true")
@Import({SignatureWebAutoConfiguration.class, SignatureStorageAutoConfiguration.class})
public class SignatureAutoConfigure implements WebMvcConfigurer {
    private static final Logger log = LoggerFactory.getLogger(SignatureAutoConfigure.class);

    @Resource
    private SignatureProperties signatureProperties;

    // @Resource
    // private SignatureConfigStorageManager signatureConfigStorageManager;

    @Resource
    private SecretStorage secretStorage;

    @Resource
    private NonceStorage nonceStorage;

    /**
     * 配置签名拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Assert.notEmpty(signatureProperties.getIncludePaths(), "signature.includePaths配置不能为空");
        List<String> excludePaths = signatureProperties.getExcludePaths();
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new SignatureInterceptor(signatureProperties, nonceStorage, secretStorage))
                .addPathPatterns(signatureProperties.getIncludePaths());
        if (excludePaths != null && !excludePaths.isEmpty()) {
            interceptorRegistration.excludePathPatterns(excludePaths);
        }
    }

}
