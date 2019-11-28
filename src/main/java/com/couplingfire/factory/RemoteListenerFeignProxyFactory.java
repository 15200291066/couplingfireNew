package com.couplingfire.factory;

import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.FactoryBean;

/**
 * @Date 2019/11/28 21:28
 * @Author lee
 **/
public class RemoteListenerFeignProxyFactory<T> implements FactoryBean<T> {



    private Feign.Builder getFeignBuilder(){
        Feign.Builder builder = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(1000, 3500))
                .retryer(new Retryer.Default(5000, 5000, 3)); //后期优化配置
        return builder;
    }

    @Override
    public T getObject() throws Exception {
        return null;
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
