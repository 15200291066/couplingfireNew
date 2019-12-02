package com.couplingfire.annotation;

import com.couplingfire.conf.MicroModuleEnum;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MicroModuleListener {
    /**
     *
     * @return
     */
    String value() default "";

    /**
     * 监听微服务的名称
     * @return
     */
    String microModuleName() default "";


    MicroModuleEnum.ListenerGroup group() default MicroModuleEnum.ListenerGroup.LOCAL_LISTENER;

    String remoteAppName() default "";
}
