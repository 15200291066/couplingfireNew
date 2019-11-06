package com.couplingfire.manager;

import com.couplingfire.factory.MicroModuleProxy;
import com.couplingfire.listener.MicroModuleListener;
import com.couplingfire.listener.MicroModuleListenersDTO;
import com.couplingfire.metaData.MicroModuleMetaData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date 2019/11/5 19:06
 * @Author lee
 **/
public class MicroModuleListenerManager {

    private final static Map<String, List<MicroModuleListener>> microModuleListenersMap = new ConcurrentHashMap<>();

    private final static Map<Class, MicroModuleMetaData> microModuleListenerMetaData = new ConcurrentHashMap<>();

    private final  static List<MicroModuleProxy> microModuleProxyList = new ArrayList();

    private final static List<Class> microModuleListenerClassList = new ArrayList();

    public static <T extends MicroModuleListener> MicroModuleListenersDTO<T> get(String microModuleName, Class<T> moduleListenerClass) {
        if (microModuleName == null)
            throw new IllegalArgumentException("microModuleName is null");

        List<MicroModuleListener> moduleListeners = microModuleListenersMap.get(microModuleName);
        if (moduleListeners != null) {
            return new MicroModuleListenersDTO(moduleListeners);
        }
        return null;
    }

    public static void addMicroModuleListenerMetaData(Class listenerClz) {
        if (listenerClz.getAnnotation(com.couplingfire.core.MicroModuleListener.class) != null) {
           // microModuleListenerMetaData.put(listenerClz, )
        }
    }

    public static void addMicroModuleListener(String microModuleName, MicroModuleListener listener) {
        List<MicroModuleListener> listeners = microModuleListenersMap.get(microModuleName);
        if (listeners == null) {
            listeners = new ArrayList();
        }
        listeners.add(listener);
        microModuleListenersMap.put(microModuleName, listeners);
    }

    public static void addMicroModuleListenerClass(Class clz){
        microModuleListenerClassList.add(clz);
    }

    public static void addMicroModuleProxy(MicroModuleProxy proxy) {
            microModuleProxyList.add(proxy);
            microModuleListenerClassList.add(proxy.getMicroModulelInterface());
    }

}
