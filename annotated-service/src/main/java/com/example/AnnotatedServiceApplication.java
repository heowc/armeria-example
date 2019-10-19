package com.example;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.PathPrefix;

public class AnnotatedServiceApplication {

    public static void main(String[] args) {
        final Server server = newServer(8080);
        server.start().join();
    }

    private static Server newServer(int port) {
        final ServerBuilder sb = Server.builder();
        return sb.http(port)
                .annotatedService(new IndexService())
                .build();
    }
}

@PathPrefix("/")
class IndexService {

    @Get("/")
    public HttpResponse index() {
        return HttpResponse.of("Hello, Armeria!");
    }
}