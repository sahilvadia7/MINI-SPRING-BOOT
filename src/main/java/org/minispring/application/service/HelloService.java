package org.minispring.application.service;

import org.minispring.annotation.stereotype.Service;

@Service
public class HelloService {
    public String sayHello() {
        return "Hello from HelloService!";
    }
}
