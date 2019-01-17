package com.github.erdanielli.boot.shutdown.undertow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

public class UndertowGracefulShutdownConfiguration {

    @Bean
    public UndertowGracefulShutdown gracefulShutdown(@Value("${server.await-termination:30s}") Duration timeout) {
        return new UndertowGracefulShutdown(timeout);
    }

    @Bean
    public WebServerFactoryCustomizer<UndertowServletWebServerFactory> undertowCustomizer(UndertowGracefulShutdown handler) {
        return factory -> factory.addDeploymentInfoCustomizers(info -> info.addInitialHandlerChainWrapper(handler));
    }
}
