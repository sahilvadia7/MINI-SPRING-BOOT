package org.minispring.core;

import org.minispring.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.Map;

public class DependencyInjector {
    private final BeanDefinitionRegistry registry;

    public DependencyInjector(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void injectDependencies() {
        for (Map.Entry<String, Object> entry : registry.getAllBeans().entrySet()) {
            Object bean = entry.getValue();
            Class<?> clazz = bean.getClass();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Class<?> fieldType = field.getType();

                    // Try to get a matching bean by type
                    Object dependency = registry.getBean(fieldType);
                    if (dependency != null) {
                        try {
                            field.setAccessible(true);
                            field.set(bean, dependency);
                            System.out.printf("Injected %s into %s.%s%n",
                                    dependency.getClass().getSimpleName(),
                                    clazz.getSimpleName(),
                                    field.getName());
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Failed to inject dependency into field: " + field.getName(), e);
                        }
                    } else {
                        throw new RuntimeException("No bean found for type: " + fieldType.getName());
                    }
                }
            }
        }
    }
}
