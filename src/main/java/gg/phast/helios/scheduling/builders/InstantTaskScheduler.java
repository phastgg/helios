package gg.phast.helios.scheduling.builders;

import gg.phast.helios.Helios;
import gg.phast.helios.scheduling.HeliosTask;
import gg.phast.helios.scheduling.builders.util.SchedulerUtil;
import gg.phast.helios.scheduling.eventhandler.TaskEventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Instant task builder, equivalent of {@link org.bukkit.scheduler.BukkitScheduler#runTask(Plugin, Runnable)}
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public class InstantTaskScheduler extends TaskBuilder {

    private Consumer<HeliosTask> consumer;

    /**
     * Sets up consumer for scheduled task, which will be called when running this task
     * @param consumer consumer
     * @return builder instance
     * @since 1.0-SNAPSHOT
     */
    public InstantTaskScheduler execute(@NotNull Consumer<HeliosTask> consumer) {
        Objects.requireNonNull(consumer, "consumer");
        this.consumer = consumer;
        return this;
    }

    /**
     * Schedules instant task, equivalent of {@link org.bukkit.scheduler.BukkitScheduler#runTask(Plugin, Runnable)}
     * @param async whether task should be async
     * @return bukkit task wrapper
     * @since 1.0-SNAPSHOT
     */
    @Override
    protected @NotNull HeliosTask schedule(boolean async) {
        Objects.requireNonNull(consumer, "consumer");
        TaskEventHandler eventHandler = createEventHandler();
        BukkitTask bukkitTask = SchedulerUtil.run(
                Helios.getPlugin(),
                SchedulerUtil.convertHeliosToBukkitConsumer(consumer, eventHandler, false),
                0L,
                -1L,
                async
        );

        return HeliosTask.of(bukkitTask, eventHandler);
    }
}
