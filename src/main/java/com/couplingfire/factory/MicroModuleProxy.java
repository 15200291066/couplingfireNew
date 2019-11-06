package com.couplingfire.factory;

import com.couplingfire.metaData.MicroModuleMetaData;
import org.aopalliance.intercept.Invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Date 2019/11/5 18:44
 * @Author lee
 **/
public class MicroModuleProxy implements InvocationHandler {

    private MicroModuleMetaData metaData;

    private Class microModulelInterface;

    public MicroModuleMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MicroModuleMetaData metaData) {
        this.metaData = metaData;
    }

    public Class getMicroModulelInterface() {
        return microModulelInterface;
    }

    public void setMicroModulelInterface(Class microModulelInterface) {
        this.microModulelInterface = microModulelInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
