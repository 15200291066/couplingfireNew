package com.couplingfire.registry;

import com.couplingfire.core.MicroModuleListener;
import com.couplingfire.manager.MicroModuleListenerContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;

/**
 * @Date 2019/11/6 18:57
 * @Author lee
 **/
@Component
public class MicroModuleIOC implements BeanPostProcessor {

    @Resource
    private MicroModuleListenerContext listenerManager;

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        Class beanClz = o.getClass();
        Annotation anno = beanClz.getAnnotation(MicroModuleListener.class);
        if (anno != null) {
            MicroModuleListenerContext.addMicroModuleListenerClass(beanClz);
            listenerManager.registMicroModuleListener(beanClz);
        }
        return o;
    }
}
