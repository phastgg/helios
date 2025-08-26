package gg.phast.helios.scheduling;

import gg.phast.helios.scheduling.builders.DelayedTaskScheduler;
import gg.phast.helios.scheduling.builders.InstantTaskScheduler;
import gg.phast.helios.scheduling.builders.RepeatingTaskScheduler;
import gg.phast.helios.scheduling.eventhandler.TaskEventHandler;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * BukkitTask wrapper which is primarily made for being used
 * while scheduling via helios scheduling api, to schedule
 * tasks use either {@link #instantTaskScheduler()}, {@link #delayedTaskScheduler()}
 * or {@link #repeatingTaskScheduler()}
 *
 *  @author phastgg
 *  @since 1.0-SNAPSHOT
 */
public abstract class HeliosTask {

    protected final TaskEventHandler eventHandler;
    protected final UUID uuid;

    /**
     * Constructor
     * @param eventHandler event handler used for triggering cancel
     * @since 1.0-SNAPSHOT
     */
    @ApiStatus.Internal
    protected HeliosTask(TaskEventHandler eventHandler) {
        this.eventHandler = eventHandler;
        this.uuid = UUID.randomUUID();
    }

    /**
     * Cancels task if the task is still valid
     * @since 1.0-SNAPSHOT
     */
    public abstract void cancel();

    /**
     * Returns whether task is cancelled
     * @return whether task is cancelled
     * @since 1.0-SNAPSHOT
     */
    public abstract boolean isCancelled();

    /**
     * Returns unique id which is generated for every task to differentiate them
     * @return unique id
     * @since 1.0-SNAPSHOT
     */
    public UUID getUniqueId() {
        return uuid;
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
}
