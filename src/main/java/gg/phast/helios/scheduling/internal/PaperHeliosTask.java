package gg.phast.helios.scheduling.internal;

import gg.phast.helios.Helios;
import gg.phast.helios.scheduling.HeliosTask;
import gg.phast.helios.scheduling.eventhandler.TaskEventHandler;
import gg.phast.helios.scheduling.eventhandler.TaskEventType;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Paper implementation of abstract Helios Task, which is created to work with {@link ScheduledTask} instances
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public class PaperHeliosTask extends HeliosTask {

    private final ScheduledTask task;

    /**
     * Constructor
     * @param eventHandler event handler used for triggering cancel
     * @since 1.0-SNAPSHOT
     */
    protected PaperHeliosTask(TaskEventHandler eventHandler, ScheduledTask task) {
        super(eventHandler);
        this.task = task;
    }

    /**
     * Cancels task if the task is still valid
     * @since 1.0-SNAPSHOT
     */
    @Override
    public void cancel() {
        if (isCancelled()) {
            Helios.getLogger().warning("Tried to cancel task (" + task + ") which is already considered cancelled!");
            return;
        }

        try {
            task.cancel();
            if (eventHandler != null) eventHandler.triggerEvent(this, TaskEventType.CANCEL, null);
        } catch (IllegalStateException ignored) {
            // ignored since we do not care if task wasn't scheduled already
        }
    }

    /**
     * Returns whether task is cancelled
     * @return whether task is cancelled
     * @since 1.0-SNAPSHOT
     */
    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }

    /**
     * Creates new Paper Helios Task instance, not suited for outside use
     * @param eventHandler event handler
     * @param task task
     * @return helios task
     * @since 1.0-SNAPSHOT
     */
    @Contract("_, _ -> new")
    @ApiStatus.Internal
    public static @NotNull HeliosTask create(TaskEventHandler eventHandler, ScheduledTask task) {
        return new PaperHeliosTask(eventHandler, task);
    }
}
