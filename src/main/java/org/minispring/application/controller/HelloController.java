package org.minispring.application.controller;

import org.minispring.annotation.Autowired;
import org.minispring.annotation.RequestMapping;
import org.minispring.annotation.stereotype.RestController;
import org.minispring.application.service.HelloService;

@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;

    @RequestMapping(path = "/hello", method = "GET")
    public String helloWorld(){
       return helloService.sayHello();
    }


}
