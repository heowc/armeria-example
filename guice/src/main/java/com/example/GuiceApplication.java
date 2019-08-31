package com.example;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Path;
import com.linecorp.armeria.server.docs.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(GuiceApplication.class);
    private static Injector injector;

    static {
        injector = Guice.createInjector(modules());
    }

    private static Iterable<? extends Module> modules() {
        return ImmutableList.of(new ServiceModule());
    }

    public static void main(String[] args) {
        final Server server = newServer(8080);
        server.start().join();
    }

    private static Server newServer(int port) {
        final ServerBuilder sb = new ServerBuilder();
        return sb.http(port)
                .annotatedService(injector.getInstance(IndexService.class))
                .serviceUnder("/docs", new DocService())
                .build();
    }
}

class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().bind(IndexService.class);
    }
}

class IndexService {

    @Get
    @Path("/")
    public HttpResponse index() {
        return HttpResponse.of("Hello, Armeria!");
    }
}