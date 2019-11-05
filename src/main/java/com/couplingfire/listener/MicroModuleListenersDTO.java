package com.couplingfire.listener;

import java.util.List;

/**
 * @Date 2019/11/5 19:19
 * @Author lee
 **/
public class MicroModuleListenersDTO<T extends MicroModuleListener> {
    private List<T> moduleListeners;

    public MicroModuleListenersDTO() {
    }

    public MicroModuleListenersDTO(List<T> moduleListeners) {

        this.moduleListeners = moduleListeners;
    }

    public List<T> getModuleListeners() {
        return moduleListeners;
    }
}
