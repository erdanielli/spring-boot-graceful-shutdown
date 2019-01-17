/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.github.erdanielli.boot.shutdown.undertow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

/**
 * {@link org.springframework.context.annotation.Configuration Configuration} to enable graceful shutdown for Undertow.
 * <p>The implementation is based on comments from <a href="https://github.com/spring-projects/spring-boot/issues/4657">issue #4657</a></p>
 *
 * @author erdanielli
 */
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
