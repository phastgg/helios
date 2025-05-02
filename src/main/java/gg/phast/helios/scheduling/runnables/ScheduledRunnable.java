package gg.phast.helios.scheduling.runnables;

import gg.phast.helios.scheduling.HeliosTask;

/**
 * Functional interface used for running code for scheduled task,
 * functional method is {@link #run()}
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
@FunctionalInterface
public interface ScheduledRunnable extends ScheduledConsumer {

    /**
     * Overridden method from {@link ScheduledConsumer}
     * @param heliosTask the input argument
     */
    @Override
    default void accept(HeliosTask heliosTask) {
        run();
    }

    /**
     * Functional method basically same as {@link Runnable#run()}
     */
    void run();
}
