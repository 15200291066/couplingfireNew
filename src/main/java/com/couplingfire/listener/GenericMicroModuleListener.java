package com.couplingfire.listener;

import com.couplingfire.event.MicroModuleEvent;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;

public interface GenericMicroModuleListener extends MicroModuleListener<MicroModuleEvent>, Ordered {
    boolean supportsEventType(ResolvableType var1);

    default boolean supportsEventType(Class<? extends MicroModuleEvent> eventType) {
        return supportsEventType(ResolvableType.forClass(eventType));
    }

    default boolean supportsSourceType( Class<?> sourceType) {
        return true;
    }

    default int getOrder() {
        return 2147483647;
    }
}
