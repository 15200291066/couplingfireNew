package com.couplingfire.listener;

import com.couplingfire.event.MicroModuleEvent;
import com.couplingfire.listener.EventListener;


public interface MicroModuleListener<E extends MicroModuleEvent>  extends EventListener {
    void onEvent(E event);
}
