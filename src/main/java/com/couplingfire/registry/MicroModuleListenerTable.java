package com.couplingfire.registry;

import com.couplingfire.listener.MicroModuleListener;

public interface MicroModuleListenerTable {
    void addMicroModuleListenerClass(Class<? extends MicroModuleListener> clz);

    void registMicroModuleListener(String microModule, MicroModuleListener listener);

    void registMicroModuleListener(Class<? extends MicroModuleListener> microModuleListenerClz);

    void registMicroModuleListener(String microModule, Class<? extends MicroModuleListener> clz);
}
