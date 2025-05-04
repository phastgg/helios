package gg.phast.helios.scheduling.builders;

import gg.phast.helios.scheduling.HeliosTask;
import gg.phast.helios.scheduling.runnables.ScheduledConsumer;
import gg.phast.helios.scheduling.runnables.ScheduledTaskException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract task builder class, which is being extended
 * by {@link InstantTaskBuilder}, {@link DelayedTaskBuilder} and {@link RepeatingTaskBuilder}
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public abstract class TaskBuilder extends TaskEventBuilder {

    private boolean scheduled = false;

    /**
     * Constructor
     * @since 1.0-SNAPSHOT
     */
    public TaskBuilder() {
    }

    /**
     * Overridden from {@link TaskEventBuilder} to return this builder instance instead
     * @param onException consumer
     * @return builder instance
     * @since 1.0-SNAPSHOT
     */
    @Override
    public TaskBuilder onException(final @Nullable ScheduledTaskException onException) {
        super.onException(onException);
        return this;
    }

    /**
     * Overridden from {@link TaskEventBuilder} to return this builder instance instead
     * @param onCancel consumer
     * @return builder instance
     * @since 1.0-SNAPSHOT
     */
    @Override
    public TaskBuilder onCancel(final @Nullable ScheduledConsumer onCancel) {
        super.onCancel(onCancel);
        return this;
    }

    /**
     * Overridden from {@link TaskEventBuilder} to return this builder instance instead
     * @param onFinish consumer
     * @return builder instance
     * @since 1.0-SNAPSHOT
     */
    @Override
    public TaskBuilder onFinish(final @Nullable ScheduledConsumer onFinish) {
        super.onFinish(onFinish);
        return this;
    }

    /**
     * Schedules sync task according to settings in specific builder
     * @return bukkit task wrapper
     * @since 1.0-SNAPSHOT
     */
    public @NotNull HeliosTask schedule() {
        if (scheduled) {
            throw new IllegalStateException("TaskBuilder has already been used to schedule a task");
        }
        scheduled = true;
        return schedule(false);
    }

    /**
     * Schedules async task according to settings in specific builder
     * @return bukkit task wrapper
     * @since 1.0-SNAPSHOT
     */
    public @NotNull HeliosTask scheduleAsync() {
        if (scheduled) {
            throw new IllegalStateException("TaskBuilder has already been used to schedule a task");
        }
        scheduled = true;
        return schedule(true);
    }

    /**
     * Abstract schedule method used to determine schedule method in every builder
     * @param async whether task should be async
     * @return bukkit task wrapper
     * @since 1.0-SNAPSHOT
     */
    protected abstract @NotNull HeliosTask schedule(boolean async);
}

