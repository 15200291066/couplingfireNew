package com.couplingfire.manager;

import com.couplingfire.core.MicroModule;
import com.couplingfire.event.MicroModuleEvent;
import com.couplingfire.factory.MicroModuleProxy;
import com.couplingfire.listener.GenericMicroModuleListener;
import com.couplingfire.listener.MicroModuleListener;
import com.couplingfire.listener.MicroModuleListenersDTO;
import com.couplingfire.metaData.MicroModuleMetaData;
import com.couplingfire.publisher.MicroModuleEventPublisher;
import com.couplingfire.registry.DefaultMicroModuleListenerTable;
import com.couplingfire.registry.MicroModuleListenerTable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @Date 2019/11/5 19:06
 * @Author lee
 **/
@Component
public class MicroModuleListenerContext implements ApplicationContextAware, MicroModuleEventPublisher {

    private ApplicationContext context;

    @Autowired
    private DefaultMicroModuleListenerTable defaultListenerTable;

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

    public void registMicroModuleListener(Class<? extends MicroModuleListener> listenerClz) {
        MicroModuleMetaData metaData = microModuleListenerMetaData.get(listenerClz);
        if (metaData == null) {
            metaData = MicroModuleMetaData.buildMetaData(listenerClz);
            microModuleListenerMetaData.put(listenerClz, metaData);
        }
        String microModuleName = metaData.getMicroModuleName();
        List<MicroModuleListener> listenerList = microModuleListenersMap.get(microModuleName);
        if (listenerList == null) {
            listenerList = new ArrayList();
        }
        listenerList.add(context.getBean(listenerClz));
        microModuleListenersMap.put(microModuleName, listenerList);
    }

    public void addMicroModuleListener(String microModuleName, MicroModuleListener listener) {
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void publishEvent(MicroModuleEvent e) {

    }

    @Override
    public void publishEvent(String microModuleName, MicroModuleEvent e) {
        Set<MicroModuleListener> listeners = defaultListenerTable.getListenerByModule(microModuleName);
        if (listeners != null) {
            for (MicroModuleListener l : listeners) {
                if (((GenericMicroModuleListener)l).supportsEventType(e.getClass())) {
                    l.onEvent(e);
                }
            }
        }
    }
}
