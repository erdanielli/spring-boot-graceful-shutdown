package com.github.erdanielli.boot.shutdown.tomcat;

import com.github.erdanielli.boot.shutdown.GracefulShutdown;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

final class TomcatGracefulShutdown extends GracefulShutdown implements TomcatConnectorCustomizer {

    private volatile Connector connector;

    TomcatGracefulShutdown(@NonNull Duration timeout) {
        super(timeout);
    }

    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }

    @Override
    protected void gracefulShutdown(Duration timeout, ContextClosedEvent event) throws InterruptedException {
        connector.pause();
        Executor executor = connector.getProtocolHandler().getExecutor();
        if (executor instanceof ExecutorService) {
            ExecutorService executorService = (ExecutorService) executor;
            executorService.shutdown();
            if (timeoutReached(timeout, executorService)) {
                log.warn("Tomcat did not terminate gracefully within " + timeout.getSeconds() + " seconds");
                executorService.shutdownNow();
                if (timeoutReached(timeout, executorService)) {
                    log.warn("Timeout reached (pending requests aborted)");
                }
            }
        }
    }

    private boolean timeoutReached(Duration timeout, ExecutorService executorService) throws InterruptedException {
        return !executorService.awaitTermination(timeout.toMillis(), TimeUnit.MILLISECONDS);
    }
}
