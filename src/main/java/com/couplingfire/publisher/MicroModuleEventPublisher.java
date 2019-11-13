package com.couplingfire.publisher;

import com.couplingfire.event.MicroModuleEvent;

@FunctionalInterface
public interface MicroModuleEventPublisher {
    void publishEvent(MicroModuleEvent e);
}
