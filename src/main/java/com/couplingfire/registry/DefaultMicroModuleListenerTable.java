package com.couplingfire.registry;

import com.couplingfire.listener.MicroModuleListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date 2019/11/14 18:58
 * @Author lee
 **/
@Component
public class DefaultMicroModuleListenerTable implements MicroModuleListenerTable, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Map<String, Set<MicroModuleListener>> listenerTable = new ConcurrentHashMap<>(64);

    private List<Class<? extends MicroModuleListener>> listenerClassHolder = new ArrayList<>();

    @Override
    public void addMicroModuleListenerClass(Class<? extends MicroModuleListener> clz) {
        listenerClassHolder.add(clz);
    }

    @Override
    public void registMicroModuleListener(String microModule, MicroModuleListener listener) {
        Set<MicroModuleListener> listeners = listenerTable.get(microModule);
        if (listeners == null) {
            listeners = new HashSet();
        }
        listeners.add(listener);
        listenerTable.put(microModule,listeners);
    }

    @Override
    public void registMicroModuleListener(Class<? extends MicroModuleListener> microModuleListenerClz) {
        Annotation anno = microModuleListenerClz.getAnnotation(com.couplingfire.core.MicroModuleListener.class);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}