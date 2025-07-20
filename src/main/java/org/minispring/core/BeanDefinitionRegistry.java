package org.minispring.core;

import java.util.HashMap;
import java.util.Map;

public class BeanDefinitionRegistry {

    public BeanDefinitionRegistry(){}
    private final Map<String, Object> beans = new HashMap<>();

    public void registerBean(String name, Object instance){
        beans.put(name,instance);
    }

    public Object getBean(String name){
        return beans.get(name);
    }

    public <T> T getBean(Class<T> clazz) {
        for(Object bean : beans.values()){
            if(clazz.isAssignableFrom(bean.getClass())){
                return clazz.cast(bean);
            }
        }
        throw new RuntimeException("No bean found of type: s" + clazz.getName());
    }

    public Map<String, Object> getAllBeans() {
        return beans;
    }
}
