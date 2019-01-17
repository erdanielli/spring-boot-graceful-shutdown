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

import com.github.erdanielli.boot.shutdown.GracefulShutdown;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.GracefulShutdownHandler;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;

import java.time.Duration;

/**
 * {@link GracefulShutdown GracefulShutdown} implementation for Undertow according to comments from
 * <a href="https://github.com/spring-projects/spring-boot/issues/4657">issue #4657</a>
 *
 * @author erdanielli
 */
final class UndertowGracefulShutdown extends GracefulShutdown implements HandlerWrapper {

    private volatile GracefulShutdownHandler handler;

    UndertowGracefulShutdown(@NonNull Duration timeout) {
        super(timeout);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HttpHandler wrap(HttpHandler handler) {
        this.handler = new GracefulShutdownHandler(handler);
        return this.handler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void gracefulShutdown(Duration timeout, ContextClosedEvent event) throws InterruptedException {
        handler.shutdown();
        if (!handler.awaitShutdown(timeout.toMillis())) {
            log.warn("Undertow did not terminate gracefully within " + timeout.getSeconds() + " seconds");
        }
    }
}
