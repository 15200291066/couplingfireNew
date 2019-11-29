package com.couplingfire.registry;

import com.couplingfire.conf.MicroModuleEnum;
import com.couplingfire.core.EnableCouplingFire;
import com.couplingfire.core.MicroModule;
import com.couplingfire.core.MicroModuleListener;
import com.couplingfire.factory.MicroModuleFactoryBean;
import com.couplingfire.factory.RemoteListenerFeignProxyFactory;
import com.couplingfire.manager.MicroModuleListenerContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * dynamic registry
 */
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
        try {
            microModuleBeanDefRegist(basePackages, beanDefinitionRegistry);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        microModuleListenerBeanDefRegist(basePackages, beanDefinitionRegistry);
    }

    protected ClassPathListenerScanner moduleScanner(BeanDefinitionRegistry registry) {
        ClassPathListenerScanner classPathModuleScanner
                = new ClassPathListenerScanner(registry, environment, resourceLoader);
        classPathModuleScanner.addIncludeFilter(new AnnotationTypeFilter(MicroModule.class));
        classPathModuleScanner.addExcludeFilter(new AnnotationTypeFilter(Component.class));
        classPathModuleScanner.setMatchInterface(true);
        return classPathModuleScanner;
    }

    private void microModuleBeanDefRegist(Set<String> basePackages, BeanDefinitionRegistry bfRegistry) throws ClassNotFoundException {
        ClassPathListenerScanner microModuleListenerScanner = moduleScanner(bfRegistry);
        for (String basePackage : basePackages) {
            Set<BeanDefinitionHolder> beanDefinitionHolders = microModuleListenerScanner.doScan(basePackage);
            for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
                Class<? extends com.couplingfire.listener.MicroModuleListener> clz =
                        (Class<? extends com.couplingfire.listener.MicroModuleListener>) Class.forName(beanDefinitionHolder.getBeanDefinition().getBeanClassName(), true, classLoader);
                MicroModuleListenerContext.addMicroModuleListenerClass(clz);
            }
            log.info(beanDefinitionHolders.size() + " MicroModuleListener { " +
                    beanDefinitionHolders +
                    " } found by package[" + basePackages + "]");
        }
    }

    protected ClassPathListenerScanner microModuleListenrScanner(BeanDefinitionRegistry bfRegistry) {
        ClassPathListenerScanner classPathModuleListenerScanner
                = new ClassPathListenerScanner(bfRegistry, environment, resourceLoader);
        classPathModuleListenerScanner.addIncludeFilter(new AnnotationTypeFilter(MicroModuleListener.class));
        classPathModuleListenerScanner.addExcludeFilter(new AnnotationTypeFilter(Component.class));
        classPathModuleListenerScanner.setMatchInterface(true);
        return classPathModuleListenerScanner;
    }


    private void microModuleListenerBeanDefRegist (Set<String> basePackages, BeanDefinitionRegistry bfRegistry) {
        ClassPathListenerScanner microModuleScanner = microModuleListenrScanner(bfRegistry);
        for (String basePackage : basePackages) {
            Set<BeanDefinitionHolder> beanDefinitionHolders = microModuleScanner.doScan(basePackage);
            for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
                doMicroModuleListenerRegist(beanDefinitionHolder, bfRegistry);
            }
            log.info(beanDefinitionHolders.size() + "Remote MicroModuleListener { " +
                    beanDefinitionHolders + " } found by package[" + basePackage + "]");
        }
    }

    /**
     * 将监听者的beanDefinition注册到Bean工厂
     * @param beanDefinitionHolder
     */
    private void doMicroModuleListenerRegist(BeanDefinitionHolder beanDefinitionHolder, BeanDefinitionRegistry bfRegistry) {
        BeanDefinition bf = beanDefinitionHolder.getBeanDefinition();
        if (bf instanceof AnnotatedBeanDefinition) {
            AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition)bf;
            AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
            Map<String,Object> attributes =annotationMetadata.getAnnotationAttributes(MicroModuleListener.class.getCanonicalName());
            String className =annotationMetadata.getClassName();

            if (Objects.equals(MicroModuleEnum.ListenerGroup.REMOTE_LISTENER, getListenerGroup(attributes))) {
                Assert.isTrue(annotationMetadata.isInterface(), "remote listener can only be specified on an interface");
            }

            String remoteAppName = getRemoteAppName(attributes, "remoteAppName");
            BeanDefinitionBuilder dfBuilder = BeanDefinitionBuilder.genericBeanDefinition(RemoteListenerFeignProxyFactory.class);
            dfBuilder.addPropertyValue("remoteAppName", remoteAppName);
            dfBuilder.setAutowireMode(2);
            BeanDefinitionHolder bfh = new BeanDefinitionHolder(dfBuilder.getBeanDefinition(),className);
            BeanDefinitionReaderUtils.registerBeanDefinition(bfh, bfRegistry);
        }
//        String beanClassName = null;
//        try {
//            GenericBeanDefinition definition = (GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
//            beanClassName = definition.getBeanClassName();
//            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
//            definition.setBeanClass(MicroModuleFactoryBean.class);
//            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
//        } catch (Exception e) {
//            log.error("Module {} defined failed for {}", beanClassName, e, e);
//        }
    }

    private MicroModuleEnum.ListenerGroup getListenerGroup(Map<String, Object> listenerAttr) {
        if (Objects.isNull(listenerAttr)) {
            return null;
        } else {
            MicroModuleEnum.ListenerGroup result = (MicroModuleEnum.ListenerGroup) listenerAttr.get("group");
            if (Objects.isNull(result)) {
                throw new IllegalStateException("MicroModuleListener attribute [group] must not be null");
            } else {
                return result;
            }
        }
    }

    private String getRemoteAppName(Map<String,Object> listenerAttr, String annoAttrName) {
        if (Objects.isNull(listenerAttr)) {
            return null;
        } else {
            String result = (String) listenerAttr.get("remoteAppName");
            if (StringUtils.isEmpty(result)) {
                throw new IllegalStateException("remoteAppName must be provided in @" + com.couplingfire.listener.MicroModuleListener.class.getSimpleName() + " with group = remote_listener");
            } else {
                return result;
            }
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
