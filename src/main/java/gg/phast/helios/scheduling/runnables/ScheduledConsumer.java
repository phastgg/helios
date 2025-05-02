package gg.phast.helios.scheduling.runnables;

import gg.phast.helios.scheduling.HeliosTask;

import java.util.function.Consumer;

/**
 * Functional interface used for running code for scheduled
 * task while having option to get the task wrapper,
 * functional method is {@link Consumer#accept(Object)}
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
@FunctionalInterface
public interface ScheduledConsumer extends Consumer<HeliosTask> {
}
