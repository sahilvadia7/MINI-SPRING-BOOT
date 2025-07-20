package org.minispring.core;

import org.minispring.annotation.PostConstruct;
import org.minispring.annotation.PreDestroy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LifecycleProcessor {
    private final BeanDefinitionRegistry registry;
    private final List<Runnable> destroyCallbacks = new ArrayList<>();

    public LifecycleProcessor(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void processPostConstruct() {
        for (Object bean : registry.getAllBeans().values()) {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(PostConstruct.class)) {
                    method.setAccessible(true);
                    try {
                        method.invoke(bean);
                        System.out.printf("PostConstruct called: %s.%s()%n",
                                bean.getClass().getSimpleName(), method.getName());
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to invoke @PostConstruct: " + method.getName(), e);
                    }
                }
            }
        }
    }

    public void registerPreDestroyCallbacks() {
        for (Object bean : registry.getAllBeans().values()) {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(PreDestroy.class)) {
                    method.setAccessible(true);
                    destroyCallbacks.add(() -> {
                        try {
                            method.invoke(bean);
                            System.out.printf("PreDestroy called: %s.%s()%n",
                                    bean.getClass().getSimpleName(), method.getName());
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to invoke @PreDestroy: " + method.getName(), e);
                        }
                    });
                }
            }
        }

        // Hook on JVM shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down... running @PreDestroy callbacks:");
            for (Runnable callback : destroyCallbacks) {
                callback.run();
            }
        }));
    }
}
