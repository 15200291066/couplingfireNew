package com.couplingfire.registry;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import java.util.Set;

import static org.springframework.context.annotation.AnnotationConfigUtils.registerAnnotationConfigProcessors;

public class ClassPathListenerScanner extends ClassPathBeanDefinitionScanner {

    private boolean matchInterface; // 是否匹配接口

    public ClassPathListenerScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters,
                                  Environment environment, ResourceLoader resourceLoader) {
        super(registry, useDefaultFilters, environment);
        setResourceLoader(resourceLoader);
        registerAnnotationConfigProcessors(registry);
    }

    public ClassPathListenerScanner(BeanDefinitionRegistry registry, Environment environment, ResourceLoader resourceLoader) {
        this(registry, false, environment, resourceLoader);
    }

    public void setMatchInterface(boolean matchInterface) {
        this.matchInterface = matchInterface;
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        if (matchInterface) return beanDefinition.getMetadata().isInterface();
        else return ! beanDefinition.getMetadata().isInterface();
    }
}
