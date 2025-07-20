package org.minispring.core;

import com.sun.net.httpserver.*;
import org.minispring.annotation.RequestMapping;
import org.minispring.annotation.stereotype.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

public class HttpServerRunner {
    private final BeanDefinitionRegistry registry;
    private HttpServer server;

    public HttpServerRunner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        System.out.println("🚀 HTTP server started on port " + port);

        for (Object bean : registry.getAllBeans().values()) {
            Class<?> clazz = bean.getClass();
            if (clazz.isAnnotationPresent(RestController.class)) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                        String path = mapping.path();
                        String httpMethod = mapping.method().toUpperCase();

                        server.createContext(path, exchange -> {
                            if (!exchange.getRequestMethod().equalsIgnoreCase(httpMethod)) {
                                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                                return;
                            }

                            try {
                                Object response = method.invoke(bean); // assuming no params for now
                                String responseText = response != null ? response.toString() : "";
                                exchange.sendResponseHeaders(200, responseText.getBytes().length);
                                OutputStream os = exchange.getResponseBody();
                                os.write(responseText.getBytes());
                                os.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                                exchange.sendResponseHeaders(500, -1); // Internal Server Error
                            }
                        });

                        System.out.printf("Mapped: [%s] %s → %s.%s()%n",
                                httpMethod, path, clazz.getSimpleName(), method.getName());
                    }
                }
            }
        }

        server.setExecutor(null); // default executor
        server.start();
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("🔴 HTTP server stopped");
        }
    }
}
