package com.couplingfire.core;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MicroModuleListener {
    /**
     * {@link #name()}
     * @return
     */
    String value() default "";

    /**
     * 监听微服务的名称
     * @return
     */
    String name() default "";
}
