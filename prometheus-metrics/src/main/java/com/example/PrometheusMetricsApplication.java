package com.example;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.common.metric.MeterIdPrefixFunction;
import com.linecorp.armeria.common.metric.PrometheusMeterRegistries;
import com.linecorp.armeria.server.*;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.metric.MetricCollectingService;
import com.linecorp.armeria.server.metric.PrometheusExpositionService;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

public class PrometheusMetricsApplication {

    public static void main(String[] args) {
        final PrometheusMeterRegistry meterRegistry = PrometheusMeterRegistries.newRegistry();

        ServerBuilder sb = Server.builder();
        Server server = sb.http(8080)
                .meterRegistry(meterRegistry)
                .service(new HttpServiceWithRoutes() {

                    @Override
                    public Set<Route> routes() {
                        return Set.of(Route.builder().path("/").build());
                    }

                    @Override
                    public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) {
                        return HttpResponse.of("Hello, Armeria!");
                    }
                }, MetricCollectingService.newDecorator(MeterIdPrefixFunction.ofDefault("index")))
                .annotatedService()
                    .pathPrefix("/custom")
                    .decorator(MetricCollectingService.newDecorator(MeterIdPrefixFunction.ofDefault("custom")))
                    .build(new Object() {

                        @Get("/")
                        public String get() {
                            return "get";
                        }

                        @Post("/")
                        public String post() {
                            return "post";
                        }
                    })
                // metrics
                .serviceUnder("/metrics", new PrometheusExpositionService(meterRegistry.getPrometheusRegistry()))
                .build();
        CompletableFuture<Void> future = server.start();
        future.join();
    }
}