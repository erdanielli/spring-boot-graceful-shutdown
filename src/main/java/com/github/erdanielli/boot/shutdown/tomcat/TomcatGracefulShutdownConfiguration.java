package com.github.erdanielli.boot.shutdown.tomcat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

public class TomcatGracefulShutdownConfiguration {

    @Bean
    public TomcatGracefulShutdown gracefulShutdown(@Value("${server.await-termination:30s}") Duration timeout) {
        return new TomcatGracefulShutdown(timeout);
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer(TomcatGracefulShutdown connector) {
        return factory -> factory.addConnectorCustomizers(connector);
    }
}
