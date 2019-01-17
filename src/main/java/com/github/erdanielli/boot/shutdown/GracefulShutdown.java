package com.github.erdanielli.boot.shutdown;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;

import java.time.Duration;

public abstract class GracefulShutdown implements ApplicationListener<ContextClosedEvent> {

    protected final Log log = LogFactory.getLog(GracefulShutdown.class);

    private final Duration timeout;

    protected GracefulShutdown(@NonNull Duration timeout) {
        this.timeout = timeout;
    }

    @Override
    public final void onApplicationEvent(ContextClosedEvent event) {
        log.info("Awaiting pending requests");
        try {
            gracefulShutdown(timeout, event);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected abstract void gracefulShutdown(Duration timeout, ContextClosedEvent event) throws InterruptedException;
}
