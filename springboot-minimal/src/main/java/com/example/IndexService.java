package com.example;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.Get;
import org.springframework.stereotype.Component;

@Component
public class IndexService {

    @Get("/")
    public HttpResponse index() {
        return HttpResponse.of("Hello, Armeria!");
    }
}