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

import com.github.erdanielli.boot.shutdown.GracefulShutdown;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * {@link GracefulShutdown GracefulShutdown} implementation for Tomcat according to comments from
 * <a href="https://github.com/spring-projects/spring-boot/issues/4657">issue #4657</a>
 *
 * @author erdanielli
 */
final class TomcatGracefulShutdown extends GracefulShutdown implements TomcatConnectorCustomizer {

    private volatile Connector connector;

    TomcatGracefulShutdown(long timeout) {
        super(timeout);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void gracefulShutdown(long timeout, ContextClosedEvent event) throws InterruptedException {
        connector.pause();
        Executor executor = connector.getProtocolHandler().getExecutor();
        if (executor instanceof ExecutorService) {
            ExecutorService executorService = (ExecutorService) executor;
            executorService.shutdown();
            if (timeoutReached(timeout, executorService)) {
                log.warn("Tomcat did not terminate gracefully within " + (timeout / 1000) + " seconds");
                executorService.shutdownNow();
                if (timeoutReached(timeout, executorService)) {
                    log.warn("Timeout reached (pending requests aborted)");
                }
            }
        }
    }

    private boolean timeoutReached(long timeout, ExecutorService executorService) throws InterruptedException {
        return !executorService.awaitTermination(timeout, TimeUnit.MILLISECONDS);
    }
}
