package com.example;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecoratingServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(DecoratingServiceApplication.class);

    public static void main(String[] args) {
        final Server server = newServer(8080);
        server.start().join();
    }

    private static Server newServer(int port) {
        final ServerBuilder sb = new ServerBuilder();
        HttpService service = new IndexService();
        return sb.http(port)
                .service("/", service)
                .service("/decorate", service.decorate(delegate -> new SimpleDecoratingHttpService(delegate) {

                    @Override
                    public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
                        logger.info("second");
                        return delegate().serve(ctx, req);
                    }
                }).decorate(delegate -> new SimpleDecoratingHttpService(delegate) {

                    @Override
                    public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
                        logger.info("first");
                        return delegate().serve(ctx, req);
                    }
                }))
                .build();
    }
}

class IndexService extends AbstractHttpService {

    @Override
    public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
        return HttpResponse.of("Hello, Armeria!");
    }
}