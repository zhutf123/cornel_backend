package com.demai.cornel.auth.annotation;

import java.lang.annotation.*;

/**
 * Created by bin.zhang on 2019/10/30
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authority {
}
