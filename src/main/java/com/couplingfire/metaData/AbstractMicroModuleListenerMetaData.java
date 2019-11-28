package com.couplingfire.metaData;

/**
 * @Date 2019/11/7 18:35
 * @Author lee
 **/
public abstract class AbstractMicroModuleListenerMetaData<T> {
    protected Class listenerClass;

    protected T listener;

    protected String microModuleName;

    public abstract String getMicroModuleName();

}
