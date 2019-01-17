package com.github.erdanielli.boot.shutdown;

import com.github.erdanielli.boot.shutdown.tomcat.TomcatGracefulShutdownConfiguration;
import com.github.erdanielli.boot.shutdown.undertow.UndertowGracefulShutdownConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnWebApplication
public class GracefulShutdownAutoConfiguration {

    @Configuration
    @ConditionalOnClass(name = "org.apache.catalina.connector.Connector")
    @Import(TomcatGracefulShutdownConfiguration.class)
    static class Tomcat {

    }

    @Configuration
    @ConditionalOnClass(name = "io.undertow.server.handlers.GracefulShutdownHandler")
    @Import(UndertowGracefulShutdownConfiguration.class)
    static class Undertow {

    }
}
