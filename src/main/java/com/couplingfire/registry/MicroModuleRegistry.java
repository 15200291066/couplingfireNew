package com.couplingfire.registry;

import com.couplingfire.core.EnableCouplingFire;
import com.couplingfire.core.MicroModule;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.*;
import java.util.stream.Collectors;

public class MicroModuleRegistry implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware,
        BeanClassLoaderAware {
    private static Logger log = LoggerFactory.getLogger(MicroModuleRegistry.class);
    protected ClassLoader classLoader;
    protected Environment environment;
    protected ResourceLoader resourceLoader;

    public void setBeanClassLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes annotationAttr = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableCouplingFire.class.getName()));
        Set<String> basePackages = preparedScanPakages(annotationAttr);
        microServiceBeanRegist(basePackages, beanDefinitionRegistry);
    }

    protected ClassPathListenerScanner moduleScanner(BeanDefinitionRegistry registry) {
        ClassPathListenerScanner classPathModuleScanner
                = new ClassPathListenerScanner(registry, environment, resourceLoader);
        classPathModuleScanner.addIncludeFilter(new AnnotationTypeFilter(MicroModule.class));
        classPathModuleScanner.addExcludeFilter(new AnnotationTypeFilter(Component.class));
        classPathModuleScanner.setMatchInterface(true);
        return classPathModuleScanner;
    }

    private void microServiceBeanRegist (Set<String> basePackages, BeanDefinitionRegistry bfRegistry) {
        ClassPathListenerScanner microModuleScanner = moduleScanner(bfRegistry);
        for (String basePackage : basePackages) {
            Set<BeanDefinitionHolder> beanDefinitionHolders = microModuleScanner.doScan(basePackage);
            for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
                microModuleRegister(beanDefinitionHolder);
            }
            log.info(beanDefinitionHolders.size() + " annotated @MicroModule Components { " +
                    beanDefinitionHolders +
                    " } were scanned under package[" + basePackage + "]");
        }
    }

    private void microModuleRegister(BeanDefinitionHolder beanDefinitionHolder) {
        String beanClassName = null;
        try {
            GenericBeanDefinition definition = (GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            beanClassName = definition.getBeanClassName();
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            definition.setBeanClass(MicroModuleFactoryBean.class);
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        } catch (Exception e) {
            log.error("Module {} defined failed for {}", beanClassName, e, e);
        }
    }

    protected Set<String> preparedScanPakages(AnnotationAttributes annoAttr) {
        if (null == annoAttr)
            return null;
        Set<String> scanPakages = new LinkedHashSet<String>();
        String[] basePackages = annoAttr.getStringArray("basePackages");
        Class[] basePackageClasses = annoAttr.getClassArray("basePackagesClass");
        List<String> basePackageList = Arrays.asList(basePackages)
                                             .stream()
                                             .filter(l -> StringUtils.isNotEmpty(l)).collect(Collectors.toList());
        scanPakages.addAll(basePackageList);
        for (Class baseClz : basePackageClasses) {
            scanPakages.add(ClassUtils.getPackageName(baseClz));
        }
        return scanPakages;
    }
}
