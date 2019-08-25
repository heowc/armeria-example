package com.example;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.annotation.Get;

public class AnnotatedServiceApplication {

    public static void main(String[] args) {
        final Server server = newServer(8080);
        server.start().join();
    }

    private static Server newServer(int port) {
        final ServerBuilder sb = new ServerBuilder();
        return sb.http(port)
                .annotatedService(new IndexService())
                .build();
    }
}

class IndexService {

    @Get("/")
    public HttpResponse index() {
        return HttpResponse.of("Hello, Armeria!");
    }
}