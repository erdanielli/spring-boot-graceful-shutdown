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

package com.github.erdanielli.boot.shutdown.tomcat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

/**
 * {@link org.springframework.context.annotation.Configuration Configuration} to enable graceful shutdown for Tomcat.
 * <p>The implementation is based on comments from <a href="https://github.com/spring-projects/spring-boot/issues/4657">issue #4657</a></p>
 *
 * @author erdanielli
 */
public class TomcatGracefulShutdownConfiguration {

    @Bean
    public TomcatGracefulShutdown gracefulShutdown(@Value("${server.shutdown-timeout:30000}") long timeout) {
        return new TomcatGracefulShutdown(timeout);
    }

    @Bean
    public EmbeddedServletContainerCustomizer tomcatCustomizer(final TomcatGracefulShutdown connector) {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                if (container instanceof TomcatEmbeddedServletContainerFactory) {
                    ((TomcatEmbeddedServletContainerFactory) container).addConnectorCustomizers(connector);
                }
            }
        };
    }
}
