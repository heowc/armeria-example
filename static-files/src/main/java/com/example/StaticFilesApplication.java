package com.example;

import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.file.FileService;

import java.util.concurrent.CompletableFuture;

public class StaticFilesApplication {

    public static void main(String[] args) {
        ServerBuilder sb = Server.builder();

        Server server = sb.http(8080)
                .serviceUnder("/txt", FileService.builder(ClassLoader.getSystemClassLoader(), "txt")
                        .maxCacheEntries(1)
                        .build())
                .build();
        CompletableFuture<Void> future = server.start();
        future.join();
    }
}
