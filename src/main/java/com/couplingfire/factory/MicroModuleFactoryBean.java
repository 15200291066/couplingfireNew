package com.couplingfire.factory;

import com.couplingfire.manager.MicroModuleListenerManager;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @Date 2019/11/5 18:39
 * @Author lee
 **/
public class MicroModuleFactoryBean<T> implements FactoryBean<T> {

    private Class<T> parentInterface;

    public MicroModuleFactoryBean(Class<T> parentInterface) {

        this.parentInterface = parentInterface;

    }

    @Override
    public T getObject() throws Exception {
        if (parentInterface == null) {
            throw new IllegalAccessException("MicroModule parentInterface is null");
        }
        MicroModuleProxy microModuleProxy = new MicroModuleProxy();
        microModuleProxy.setMicroModulelInterface(parentInterface);
        MicroModuleListenerManager.addMicroModuleProxy(microModuleProxy);
        return (T) Proxy.newProxyInstance(parentInterface.getClassLoader(), new Class[] { parentInterface}, microModuleProxy);
    }

    @Override
    public Class<?> getObjectType() {

        return parentInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
