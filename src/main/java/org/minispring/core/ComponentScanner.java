package org.minispring.core;


import org.minispring.annotation.Component;
import org.minispring.annotation.stereotype.RestController;
import org.minispring.annotation.stereotype.Service;

import java.io.File;
import java.net.URL;

import static org.reflections.Reflections.log;


public class ComponentScanner {
    private final BeanDefinitionRegistry registry;

    public ComponentScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void scan(String basePackage) {
        String path = basePackage.replace('.', '/');
        log.info("path updated!!! {}",path);
        URL root = Thread.currentThread().getContextClassLoader().getResource(path);

        if (root == null) {
            throw new RuntimeException("Could not find base package: " + basePackage);
        }

        File rootDir = new File(root.getFile());
        scanDirectory(basePackage, rootDir);
    }

    private void scanDirectory(String basePackage, File directory) {
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(basePackage + "." + file.getName(), file); // recurse
            } else if (file.getName().endsWith(".class")) {
                String className = basePackage + "." + file.getName().replace(".class", "");
                try {
                    Class<?> clazz = Class.forName(className);

                    if (isBeanClass(clazz)) {
                        Object instance = clazz.getDeclaredConstructor().newInstance();
                        registry.registerBean(clazz.getSimpleName(), instance);
                        System.out.println("Loaded Bean: " + className);
                    }

                } catch (Exception e) {
                    System.err.println("Error loading class: " + className);
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isBeanClass(Class<?> clazz) {
        return clazz.isAnnotationPresent(Component.class) ||
                clazz.isAnnotationPresent(Service.class) ||
                clazz.isAnnotationPresent(RestController.class);
    }
}