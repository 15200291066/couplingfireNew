package com.couplingfire.publisher;

import com.couplingfire.event.MicroModuleEvent;
import com.couplingfire.listener.GenericMicroModuleListener;
import com.couplingfire.listener.MicroModuleListener;
import com.couplingfire.manager.MicroModuleListenerContext;
import com.couplingfire.registry.DefaultMicroModuleListenerTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date 2019/11/28 20:19
 * @Author lee
 **/
@Component
public class SimpleMicroModuleEventPublisher implements MicroModuleEventPublisher{
    @Autowired
    private DefaultMicroModuleListenerTable defaultListenerTable;

    private Map<ListenerCacheKey,Set<MicroModuleListener>> listenerCache = new ConcurrentHashMap<>(64);

    @Override
    public void publishEvent(MicroModuleEvent e) {

    }

    @Override
    public void publishEvent(String microModuleName, MicroModuleEvent e) {
        ListenerCacheKey cacheKey = new ListenerCacheKey(ResolvableType.forClass(e.getClass()), e.getSource().getClass(), microModuleName);
        Set<MicroModuleListener> listeners = listenerCache.get(cacheKey);

        if (listeners == null) {
            listeners = defaultListenerTable.getListenerByModule(microModuleName);
            if (listeners != null ) {
                Set<MicroModuleListener> listenerCacheSet = new HashSet<>();
                for (MicroModuleListener l : listeners) {
                    if (((GenericMicroModuleListener)l).supportsEventType(e.getClass())) {
                        listenerCacheSet.add(l);
                        l.onEvent(e);
                    }
                }
                listenerCache.put(cacheKey,listenerCacheSet);
            }
        } else {
            for (MicroModuleListener l : listeners) {
                l.onEvent(e);
            }
        }
    }
    private static final class ListenerCacheKey implements Comparable<ListenerCacheKey>{
        private final String microModuleName;
        private final ResolvableType eventType;
        private final Class<?> sourceType;

        public ListenerCacheKey(ResolvableType eventType, Class<?> sourceType, String microModuleName) {
            this.eventType = eventType;
            this.sourceType = sourceType;
            this.microModuleName = microModuleName;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            } else {
                ListenerCacheKey otherKey = (ListenerCacheKey)other;
                return ObjectUtils.nullSafeEquals(this.eventType, otherKey.eventType) && ObjectUtils.nullSafeEquals(this.microModuleName, otherKey.microModuleName);
            }
        }

        public int hashCode() {
            return ObjectUtils.nullSafeHashCode(microModuleName) * 30 +
                    ObjectUtils.nullSafeHashCode(this.eventType) * 29 +
                    ObjectUtils.nullSafeHashCode(this.sourceType);
        }

        public String toString() {
            return "ListenerCacheKey [eventType = " + this.eventType + ", microModuleName = " + this.microModuleName + " sourceType = " + this.sourceType.getName() + "]";
        }

        public int compareTo(ListenerCacheKey other) {
            int result = 0;
            if (this.eventType != null) {
                result = this.eventType.toString().compareTo(other.eventType.toString());
            }
            if (result == 0 && this.microModuleName != null) {
                result = this.microModuleName.compareTo(other.microModuleName);
            }
            return result;
        }
    }
}
