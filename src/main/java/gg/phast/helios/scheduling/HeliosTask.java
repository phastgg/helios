package gg.phast.helios.scheduling;

import gg.phast.helios.Helios;
import gg.phast.helios.scheduling.builders.DelayedTaskScheduler;
import gg.phast.helios.scheduling.builders.InstantTaskScheduler;
import gg.phast.helios.scheduling.builders.RepeatingTaskScheduler;
import gg.phast.helios.scheduling.eventhandler.TaskEventHandler;
import gg.phast.helios.scheduling.eventhandler.TaskEventType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * BukkitTask wrapper which is primarily made for being used
 * while scheduling via helios scheduling api, to schedule
 * tasks use either {@link #instantTaskScheduler()}, {@link #delayedTaskScheduler()}
 * or {@link #repeatingTaskScheduler()}
 *  @author phastgg
 *  @since 1.0-SNAPSHOT
 */
public final class HeliosTask {

    private final BukkitTask task;
    private final TaskEventHandler eventHandler;

    /**
     * Constructor
     * @param task which task to wrap around
     * @param eventHandler event handler used for triggering cancel
     * @since 1.0-SNAPSHOT
     */
    @ApiStatus.Internal
    private HeliosTask(@NotNull BukkitTask task, TaskEventHandler eventHandler) {
        this.task = task;
        this.eventHandler = eventHandler;
    }

    /**
     * Cancels wrapped bukkit task if it is not already cancelled
     * and catches exception in case cancelling bukkit task would throw one
     * @since 1.0-SNAPSHOT
     */
    public void cancel() {
        if (isCancelled()) {
            Helios.getLogger().warning("Tried to cancel task (ID: " + task.getTaskId() + " ) which is already considered cancelled!");
            return;
        }

        try {
            task.cancel();
            if (eventHandler != null) eventHandler.triggerEvent(this, TaskEventType.CANCEL, null);
        } catch (IllegalStateException e) {
            // ignored since we do not care if task wasn't scheduled already
        }
    }

    /**
     * Whether task is cancelled
     * @return task is cancelled
     * @since 1.0-SNAPSHOT
     */
    public boolean isCancelled() {
        return task.isCancelled();
    }

    /**
     * Whether task is async
     * @return task is async
     * @since 1.0-SNAPSHOT
     */
    public boolean isAsync() {
        return !task.isSync();
    }

    /**
     * Id used to identify tasks in bukkit task
     * @return id of the task
     * @since 1.0-SNAPSHOT
     */
    public int getId() {
        return task.getTaskId();
    }

    /**
     * Creates new instant task scheduler (equivalent of {@link org.bukkit.scheduler.BukkitScheduler#runTask(Plugin, Runnable)}),
     * for creating task with the same properties after scheduling, new instance needs to be made
     * @return new builder instance
     * @since 1.0-SNAPSHOT
     */
    public static @NotNull InstantTaskScheduler instantTaskScheduler() {
        return new InstantTaskScheduler();
    }

    /**
     * Creates new delayed task scheduler (equivalent of {@link org.bukkit.scheduler.BukkitScheduler#runTaskLater(Plugin, Runnable, long)},
     * for creating task with the same properties after scheduling, new instance needs to be made
     * @return new builder instance
     * @since 1.0-SNAPSHOT
     */
    public static @NotNull DelayedTaskScheduler delayedTaskScheduler() {
        return new DelayedTaskScheduler();
    }

    /**
     * Creates new repeating task scheduler (equivalent of {@link org.bukkit.scheduler.BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)},
     * for creating task with the same properties after scheduling, new instance needs to be made
     * @return new builder instance
     * @since 1.0-SNAPSHOT
     */
    public static @NotNull RepeatingTaskScheduler repeatingTaskScheduler() {
        return new RepeatingTaskScheduler();
    }

    /**
     * Creates wrapper around bukkit task with event handler used for triggering onCancel,
     * this method can be used for different use as wrapping around bukkit task without using
     * helios scheduling api, however should be used carefully as it is not designed for that
     * @param task bukkit task to wrap around
     * @param eventHandler event handler used for triggering few events, can be null
     * @return new wrapper around helios task
     * @since 1.0-SNAPSHOT
     */
    @Contract("_,_ -> new")
    public static @NotNull HeliosTask of(@NotNull BukkitTask task, @Nullable TaskEventHandler eventHandler) {
        Objects.requireNonNull(task, "task");
        return new HeliosTask(task, eventHandler);
    }
}
