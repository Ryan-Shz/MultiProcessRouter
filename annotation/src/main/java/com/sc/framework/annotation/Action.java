package com.sc.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ShamsChu
 * @Date 17/8/23 下午5:49
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Action {

    Class<?> provider();

}
