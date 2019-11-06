package com.couplingfire.core;

import com.couplingfire.listener.MicroModuleListener;
import com.couplingfire.listener.MicroModuleListenersDTO;
import com.couplingfire.manager.MicroModuleListenerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

/**
 * @Date 2019/11/6 18:35
 * @Author lee
 **/
public class MicroModuleEventDistributer {
    private static Logger log = LoggerFactory.getLogger(MicroModuleEventDistributer.class);
    public static  <T extends MicroModuleListener> void fireEvent(String microModule, Class<T> listenerClass, Consumer<T> consumer) {
        MicroModuleListenersDTO<T> microModuleListenersDTO = MicroModuleListenerManager.get(microModule, listenerClass);
        if (microModuleListenersDTO != null) {
            List<T> listeners = microModuleListenersDTO.getModuleListeners();
            int listenerSize = listeners.size();
            if (listeners != null && listenerSize > 0) {
                for (T listener : listeners) {
                    consumer.accept(listener);
                }
                if (log.isDebugEnabled())
                    log.debug("microModule {} {} execute Successful！Listener ：{}", microModule, listenerClass, listenerSize);
            }
        } else {
            log.warn("microModule{} {} no listener！", microModule, listenerClass);
        }
    }
}
