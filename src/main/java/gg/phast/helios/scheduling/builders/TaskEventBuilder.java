package gg.phast.helios.scheduling.builders;

import gg.phast.helios.scheduling.eventhandler.TaskEventHandler;
import gg.phast.helios.scheduling.runnables.ScheduledConsumer;
import gg.phast.helios.scheduling.runnables.ScheduledTaskException;
import org.jetbrains.annotations.Nullable;

/**
 * Event handler builder for building {@link TaskEventHandler},
 * this builder is implemented also in {@link TaskBuilder}
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public class TaskEventBuilder {

    private ScheduledTaskException onException;
    private ScheduledConsumer onCancel;
    private ScheduledConsumer onFinish;

    /**
     * Constructor
     * @since 1.0-SNAPSHOT
     */
    public TaskEventBuilder() {
    }

    /**
     * Changes onException consumer
     * @param onException consumer
     * @return builder instance
     * @since 1.0-SNAPSHOT
     */
    public TaskEventBuilder onException(final @Nullable ScheduledTaskException onException) {
        this.onException = onException;
        return this;
    }

    /**
     * Changes onCancel consumer
     * @param onCancel consumer
     * @return builder instance
     * @since 1.0-SNAPSHOT
     */
    public TaskEventBuilder onCancel(final @Nullable ScheduledConsumer onCancel) {
        this.onCancel = onCancel;
        return this;
    }

    /**
     * Changes onFinish consumer
     * @param onFinish consumer
     * @return builder instance
     * @since 1.0-SNAPSHOT
     */
    public TaskEventBuilder onFinish(final @Nullable ScheduledConsumer onFinish) {
        this.onFinish = onFinish;
        return this;
    }

    /**
     * Builds event handler for helios scheduling api
     * @return new built instance
     * @since 1.0-SNAPSHOT
     */
    protected TaskEventHandler buildEventHandler() {
        return new TaskEventHandler(onException, onCancel, onFinish);
    }
}
