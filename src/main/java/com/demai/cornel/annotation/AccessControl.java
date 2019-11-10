/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.annotation;

import org.springframework.core.Ordered;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 访问频率控制，method name param 生成key
 * 基于redis计数 做访问频率控制
 * value 属性设值：s_n  s:秒 n次
 *
 * Create By zhutf  19-11-9  上午10:32
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessControl {

    String value() default "";
}
