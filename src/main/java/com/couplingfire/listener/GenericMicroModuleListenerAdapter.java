package com.couplingfire.listener;

import com.couplingfire.event.MicroModuleEvent;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Map;

/**
 * @Date 2019/11/26 19:02
 * @Author lee
 **/
public class GenericMicroModuleListenerAdapter implements GenericMicroModuleListener {
    private static final Map<Class<?>, ResolvableType> eventTypeCache = new ConcurrentReferenceHashMap();
    private final MicroModuleListener<MicroModuleEvent> delegate;
    private final ResolvableType declaredEventType;

    public GenericMicroModuleListenerAdapter(MicroModuleListener<MicroModuleEvent> delegate) {
        Assert.notNull(delegate, "Delegate listener must not be null");
        this.delegate = delegate;
        this.declaredEventType = resolveDeclaredEventType(this.delegate);
    }

    private static ResolvableType resolveDeclaredEventType(MicroModuleListener<MicroModuleEvent> listener) {
        ResolvableType declaredEventType = resolveDeclaredEventType(listener.getClass());
        if (declaredEventType == null || declaredEventType.isAssignableFrom(MicroModuleEvent.class)) {
            Class<?> targetClass = AopUtils.getTargetClass(listener);
            if (targetClass != listener.getClass()) {
                declaredEventType = resolveDeclaredEventType(targetClass);
            }
        }
        return declaredEventType;
    }

    static ResolvableType resolveDeclaredEventType(Class<?> listenerType) {
        ResolvableType eventType = eventTypeCache.get(listenerType);
        if (eventType == null) {
            eventType = ResolvableType.forClass(listenerType).as(MicroModuleListener.class).getGeneric(new int[0]);
            eventTypeCache.put(listenerType, eventType);
        }

        return eventType != ResolvableType.NONE ? eventType : null;
    }

    @Override
    public void onEvent(MicroModuleEvent event) {
        this.delegate.onEvent(event);
    }

    @Override
    public boolean supportsEventType(ResolvableType eventType) {
        return this.declaredEventType == null || this.declaredEventType.isAssignableFrom(eventType);
    }
}
