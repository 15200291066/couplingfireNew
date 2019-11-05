package com.couplingfire.core;

import java.lang.annotation.*;

/**
 * @version 1.0.1
 * @auto jerry lee
 * @date 2019/8/27
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableCouplingFire {

    String[] basePackages() default {};

    Class<?>[] basePackagesClass() default {};
}