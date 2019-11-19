package com.couplingfire.metaData;

import com.couplingfire.core.MicroModuleListener;

/**
 * @Date 2019/11/5 18:58
 * @Author lee
 **/
public class MicroModuleMetaData extends AbstractMicroModuleListenerMetaData<MicroModuleListener>{

    @Override
    public String getMicroModuleName() {
        return listener.microModuleName();
    }

    public static MicroModuleMetaData buildMetaData(Class microModuleListenerClz) {
        MicroModuleMetaData metaData = new MicroModuleMetaData();
        metaData.listenerClass = microModuleListenerClz;
        metaData.listener = (MicroModuleListener) microModuleListenerClz.getAnnotation(MicroModuleListener.class);
        return metaData;
    }
}
