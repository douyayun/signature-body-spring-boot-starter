package io.github.douyayun.signature.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Signature {
    /**
     * 忽略所有
     *
     * @return
     */
    boolean ignore() default false;

    // /**
    //  * 忽略sign验签
    //  *
    //  * @return
    //  */
    // boolean ignoreVerifySign() default false;

}