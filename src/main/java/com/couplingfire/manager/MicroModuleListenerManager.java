package com.couplingfire.manager;

import com.couplingfire.listener.MicroModuleListener;
import com.couplingfire.listener.MicroModuleListenersDTO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date 2019/11/5 19:06
 * @Author lee
 **/
public class MicroModuleListenerManager {

    private Map<String, List<MicroModuleListener>> microModuleListeners = new ConcurrentHashMap<>();

    public <T extends MicroModuleListener> MicroModuleListenersDTO<T> get(String microModuleName, Class<T> moduleListenerClass) {
        if (microModuleName == null)
            throw new IllegalArgumentException("microModuleName is null");

        List<MicroModuleListener> moduleListeners = microModuleListeners.get(microModuleName);
        if (moduleListeners != null) {
            return new MicroModuleListenersDTO(moduleListeners);
        }
        return null;
    }

}
