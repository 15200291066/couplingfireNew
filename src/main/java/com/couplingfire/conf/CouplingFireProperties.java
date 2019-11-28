package com.couplingfire.conf;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Date 2019/11/28 19:41
 * @Author lee
 **/
@Configuration
public class CouplingFireProperties {

    @Value("${couplingfire.remote.rpcWay:feign}")
    private String rpcWay; //RPC方式 默认feign

    @Value("${couplingfire.remote.listener.relationCenter.type:redis}")
    private String relationCenterType;

    @Value("${couplingfire.remote.listener.relationCenter.serviceProvider:}")
    private String relationCenterClassPath; //注册中心提供者全包路径

    public String getRpcWay() {
        return rpcWay;
    }

    public void setRpcWay(String rpcWay) {
        this.rpcWay = rpcWay;
    }

    public String getRelationCenterType() {
        return relationCenterType;
    }

    public void setRelationCenterType(String relationCenterType) {
        this.relationCenterType = relationCenterType;
    }

    public String getRelationCenterClassPath() {
        return relationCenterClassPath;
    }

    public void setRelationCenterClassPath(String relationCenterClassPath) {
        this.relationCenterClassPath = relationCenterClassPath;
    }
}
