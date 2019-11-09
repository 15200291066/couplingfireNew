package com.couplingfire.core;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MicroModule {
    String name() default "";
}
