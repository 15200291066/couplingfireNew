package com.couplingfire.factory;

import org.springframework.beans.factory.FactoryBean;

/**
 * @Date 2019/11/28 21:28
 * @Author lee
 **/
public class RemoteListenerFeignProxyFactory implements FactoryBean<Object> {

    private Class<?> type;

    private String remoteAppName;

    public RemoteListenerFeignProxyFactory(Class<?> type) {
        this.type = type;
    }
//    private Feign.Builder getFeignBuilder() {
//        Feign.Builder builder = Feign.builder()
//                .encoder(new JacksonEncoder())
//                .decoder(new JacksonDecoder())
//                .options(new Request.Options(1000, 3500))
//                .retryer(new Retryer.Default(5000, 5000, 3)); //后期优化配置
//        return builder;
//    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getRemoteAppName() {
        return remoteAppName;
    }

    public void setRemoteAppName(String remoteAppName) {
        this.remoteAppName = remoteAppName;
    }

    @Override
    public Object getObject() throws Exception {
        return getTarget();
    }

    protected Object getTarget() {
        //TODO
        return new Object();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
