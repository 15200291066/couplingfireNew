package com.couplingfire.registry;

import com.couplingfire.conf.MicroModuleEnum;
import com.couplingfire.core.MicroModuleListener;
import com.couplingfire.manager.MicroModuleListenerContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

/**
 * @Date 2019/11/6 18:57
 * @Author lee
 **/
@Component
public class MicroModuleIOC implements BeanPostProcessor {

    @Resource
    private MicroModuleListenerContext listenerManager;

    @Resource
    private DefaultMicroModuleListenerTable listenerTable;

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        Class beanClz = o.getClass();
        MicroModuleListener anno = (MicroModuleListener) beanClz.getAnnotation(MicroModuleListener.class);
        if (Objects.nonNull(anno)
                    && Objects.equals(MicroModuleEnum.ListenerGroup.LOCAL_LISTENER, anno.group())) {
            listenerTable.registMicroModuleListener(beanClz);
        }
        return o;
    }
}
