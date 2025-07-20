package org.minispring;

import org.minispring.application.controller.HelloController;
import org.minispring.core.*;

import java.io.IOException;


public class App 
{
    public static void main(String[] args) throws IOException {
        BeanDefinitionRegistry registry = new BeanDefinitionRegistry();
        ComponentScanner scanner = new ComponentScanner(registry);
        DependencyInjector injector = new DependencyInjector(registry);
        LifecycleProcessor lifecycle = new LifecycleProcessor(registry);

        HttpServerRunner server = new HttpServerRunner(registry);

        scanner.scan("org.minispring.application");
        injector.injectDependencies();
        lifecycle.processPostConstruct();
        lifecycle.registerPreDestroyCallbacks();

        server.start(8080);

//        // Replace with your actual base package (where components are located)
//        scanner.scan("org.minispring.application"); // scans examples/ folder
//        injector.injectDependencies();    // perform @Autowired injection
//        lifecycle.processPostConstruct();        // runs all @PostConstruct
//        lifecycle.registerPreDestroyCallbacks(); // registers shutdown hook
//
//
//
//        System.out.println("Beans Loaded:");
//        registry.getAllBeans().forEach((k, v) -> System.out.println(" → " + k));
//
//
//        HelloController controller = (HelloController) registry.getBean("HelloController");
//        controller.helloworld(); // should print: User: Ace from MiniSpring!

    }
}
