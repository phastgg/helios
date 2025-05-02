package gg.phast.helios.scheduling.eventhandler;

import gg.phast.helios.scheduling.HeliosTask;
import gg.phast.helios.scheduling.runnables.ScheduledConsumer;
import gg.phast.helios.scheduling.runnables.ScheduledTaskException;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Event handler designed for being used when scheduling task
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public final class TaskEventHandler {

    private final ScheduledTaskException onException;
    private final ScheduledConsumer onCancel;
    private final ScheduledConsumer onFinish;

    /**
     * Constructor
     * @param onException onException consumer
     * @param onCancel onCancel consumer
     * @param onFinish onFinish consumer
     * @since 1.0-SNAPSHOT
     */
    public TaskEventHandler(@Nullable ScheduledTaskException onException,
                            @Nullable ScheduledConsumer onCancel,
                            @Nullable ScheduledConsumer onFinish
    ) {
        this.onException = onException;
        this.onCancel = onCancel;
        this.onFinish = onFinish;
    }

    /**
     * Triggers on exception consumer
     * @param heliosTask bukkit task wrapper
     * @param exception thrown exception
     * @since 1.0-SNAPSHOT
     */
    public void triggerOnException(@NotNull HeliosTask heliosTask, @NotNull Exception exception) {
        if (onException == null) return;
        Objects.requireNonNull(exception, "exception");
        onException.accept(Pair.of(heliosTask, exception));
    }

    /**
     * Triggers on cancel consumer
     * @param heliosTask bukkit task wrapper
     * @since 1.0-SNAPSHOT
     */
    public void triggerOnCancel(@NotNull HeliosTask heliosTask) {
        trigger(heliosTask, onCancel);
    }

    /**
     * Triggers on finish consumer
     * @param heliosTask bukkit task wrapper
     * @since 1.0-SNAPSHOT
     */
    public void triggerOnFinish(@NotNull HeliosTask heliosTask) {
        trigger(heliosTask, onFinish);
    }

    /**
     * Internal consumer triggering with few important checks
     * @param heliosTask bukkit task wrapper
     * @param scheduledConsumer consumer
     * @since 1.0-SNAPSHOT
     */
    private void trigger(@NotNull HeliosTask heliosTask, @Nullable ScheduledConsumer scheduledConsumer) {
        Objects.requireNonNull(heliosTask, "heliosTask");
        if (scheduledConsumer == null) return;
        scheduledConsumer.accept(heliosTask);
    }
}
