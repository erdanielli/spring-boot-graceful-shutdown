package com.github.erdanielli.boot.shutdown.undertow;

import com.github.erdanielli.boot.shutdown.GracefulShutdown;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.GracefulShutdownHandler;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;

import java.time.Duration;

final class UndertowGracefulShutdown extends GracefulShutdown implements HandlerWrapper {

    private volatile GracefulShutdownHandler handler;

    UndertowGracefulShutdown(@NonNull Duration timeout) {
        super(timeout);
    }

    @Override
    public HttpHandler wrap(HttpHandler handler) {
        this.handler = new GracefulShutdownHandler(handler);
        return this.handler;
    }

    @Override
    protected void gracefulShutdown(Duration timeout, ContextClosedEvent event) throws InterruptedException {
        handler.shutdown();
        if (!handler.awaitShutdown(timeout.toMillis())) {
            log.warn("Undertow did not terminate gracefully within " + timeout.getSeconds() + " seconds");
        }
    }
}
