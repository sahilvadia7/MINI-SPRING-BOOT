# 🧩 MiniSpring Framework

A lightweight Java framework inspired by Spring, built from scratch to understand core concepts like dependency injection, component scanning, lifecycle hooks, and request routing.

---

## 📦 Features

- Custom annotations (`@Component`, `@Service`, `@Autowired`, `@PostConstruct`, `@PreDestroy`, `@RestController`, `@RequestMapping`)
- Bean registry and dependency injection
- Classpath scanning and bean instantiation
- Lifecycle management (`@PostConstruct` & `@PreDestroy`)
- Minimal embedded HTTP server for routing HTTP requests
- Custom annotation-based controller support

---

## 📁 Project Structure
```
org.minispring
│
├── annotations/           → Custom annotations
├── core/
│   ├── BeanDefinitionRegistry.java
│   ├── ComponentScanner.java
│   ├── DependencyInjector.java
│   └── LifecycleProcessor.java
│
├── web/
│   ├── HttpServerRunner.java
│   ├── Dispatcher.java
│   └── Router.java
│
├── application/           → Your sample beans and controllers
│   ├── HelloController.java
│   └── UserService.java
│
└── App.java               → Main runner
```
## 🚀 How It Works

### 1. Start the Framework
```java
public class App {
    public static void main(String[] args) throws IOException {
        // Register beans and scan for components
        BeanDefinitionRegistry registry = new BeanDefinitionRegistry();
        new ComponentScanner(registry).scan("org.minispring.application");

        // Perform dependency injection
        new DependencyInjector(registry).injectDependencies();

        // Handle lifecycle methods
        LifecycleProcessor lifecycle = new LifecycleProcessor(registry);
        lifecycle.processPostConstruct();
        lifecycle.registerPreDestroyCallbacks();

        // Start embedded HTTP server
        new HttpServerRunner(registry).start(8080);
    }
}
````

### 2. Define Components and Controllers

```java
@Service
public class UserService {
    public String getName() {
        return "Ace";
    }
}

@RestController
public class HelloController {
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/hello", method = "GET")
    public String hello() {
        return "Hello " + userService.getName();
    }
}
```

---

## 🧪 Run It

1. Clone the repo
2. Open in your IDE
3. Run `App.java`
4. Visit: `http://localhost:8080/hello`

---

## 📚 Concepts Covered

* IoC container from scratch
* Dependency Injection via reflection
* Classpath scanning with `Reflections`
* Custom HTTP routing with annotations
* Lifecycle callback methods

---

## 📌 Future Improvements

* Add support for request body mapping
* Add response types (JSON support)
* Add unit tests and exception handling
* Add support for method parameters (path, query, body)

---

## 🛠 Technologies

* Java 8+
* `com.sun.net.httpserver.HttpServer`
* Reflection API

---

