package com.xkcoding.dynamic.datasource.annotation;

import java.lang.annotation.*;

/**
 * @Author lhMeng
 * @Create 2022-04-10-11:29
 * @Describe
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DatasourceDynamic {
    long configId() default 1L;
}
