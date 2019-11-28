package com.couplingfire.listener;

import com.couplingfire.event.MicroModuleEvent;


public interface MicroModuleListener<E extends MicroModuleEvent> extends EventListener {
    void onEvent(E event);
}
