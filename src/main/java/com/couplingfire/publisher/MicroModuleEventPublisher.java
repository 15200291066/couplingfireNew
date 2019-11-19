package com.couplingfire.publisher;

import com.couplingfire.event.MicroModuleEvent;

public interface MicroModuleEventPublisher {
    void publishEvent(MicroModuleEvent e);

    void publishEvent(String microModuleName, MicroModuleEvent e);
}
