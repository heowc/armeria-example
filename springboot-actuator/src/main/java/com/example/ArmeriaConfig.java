package com.example;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class ArmeriaConfig {

    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator(IndexService indexService) {
        return builder -> {
            // register services
            builder.annotatedService(indexService);
        };
    }

    private static final int INTERNAL_PORT = 8080;

    @Bean
    public ArmeriaServerConfigurator secureActuator() {
        return builder -> {
            builder.routeDecorator()
                    .pathPrefix("/actuator")
                    .build((delegate, ctx, req) -> {
                        final InetSocketAddress laddr = ctx.localAddress();
                        if (laddr.getPort() != INTERNAL_PORT) {
                            return HttpResponse.of(404);
                        } else {
                            return delegate.serve(ctx, req);
                        }
                    });
        };
    }
}
