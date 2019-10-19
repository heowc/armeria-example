package com.example;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;

import java.util.concurrent.CompletableFuture;

public class GettingStartedApplication {

    public static void main(String[] args) {
        ServerBuilder sb = Server.builder();;
        Server server = sb.http(8080)
                .service("/", (ctx, res) -> HttpResponse.of("Hello, Armeria!"))
                .build();
        CompletableFuture<Void> future = server.start();
        future.join();
    }
}
