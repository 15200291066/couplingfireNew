package com.couplingfire.registry;

import com.couplingfire.core.MicroModuleListener;
import com.couplingfire.manager.MicroModuleListenerManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;

/**
 * @Date 2019/11/6 18:57
 * @Author lee
 **/
public class MicroModuleIOC implements BeanPostProcessor {


    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        Class beanClz = o.getClass();
        Annotation anno = beanClz.getAnnotation(MicroModuleListener.class);
        if (anno != null) {
            MicroModuleListenerManager.addMicroModuleListenerClass(beanClz);
           // MicroModuleListenerManager.addMicroModuleListener();
        }
        return o;
    }
}
