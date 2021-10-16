package com.example;

import com.linecorp.armeria.server.tomcat.TomcatService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArmeriaConfig {

    @Bean
    public ArmeriaServerConfigurator withTomcat(ApplicationContext applicationContext) {
        final WebServer webServer = ((WebServerApplicationContext) applicationContext).getWebServer();
        if (webServer instanceof TomcatWebServer) {
            final Tomcat tomcat = ((TomcatWebServer) webServer).getTomcat();

            return serverBuilder -> serverBuilder.serviceUnder("/", TomcatService.of(tomcat));
        }
        return serverBuilder -> {};
    }
}
