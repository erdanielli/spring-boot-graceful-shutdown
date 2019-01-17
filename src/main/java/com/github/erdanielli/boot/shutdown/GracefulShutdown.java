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

package com.github.erdanielli.boot.shutdown;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;

import java.time.Duration;

/**
 * A base class for any embedded container interested in graceful shut down support.
 * It's just a {@link ContextClosedEvent} listener that triggers the graceful shut down routine.
 *
 * @author erdanielli
 */
public abstract class GracefulShutdown implements ApplicationListener<ContextClosedEvent> {

    protected final Log log = LogFactory.getLog(GracefulShutdown.class);

    private final Duration timeout;

    /**
     * Constructor with required timeout.
     * @param timeout shut down timeout
     */
    protected GracefulShutdown(@NonNull Duration timeout) {
        this.timeout = timeout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void onApplicationEvent(ContextClosedEvent event) {
        log.info("Awaiting pending requests");
        try {
            gracefulShutdown(timeout, event);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * The real shut down process goes here.
     *
     * @param timeout maximum time to wait for the container to stop gracefully.
     * @param event the original event that triggered this.
     * @throws InterruptedException if the shut down process is interrupted.
     */
    protected abstract void gracefulShutdown(Duration timeout, ContextClosedEvent event) throws InterruptedException;
}
