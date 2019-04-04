package com.ryan.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ryan
 * create by 17/8/23 下午5:42
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Provider {

    String process();

}
