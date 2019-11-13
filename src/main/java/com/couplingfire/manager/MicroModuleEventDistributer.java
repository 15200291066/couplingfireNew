package com.couplingfire.manager;

import com.couplingfire.listener.MicroModuleListener;

public interface MicroModuleEventDistributer {
    void addMicroModuleListener(MicroModuleListener<?> var1);

    void addMicroModuleListenerClass(Class var1);

    void removeMicroModuleListener(MicroModuleListener<?> var1);

    void removeMicroModuleListenerClass(Class var1);

    void removeAllListeners();
}
