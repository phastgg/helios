package gg.phast.helios.scheduling.builders;

import gg.phast.helios.Helios;
import gg.phast.helios.scheduling.HeliosTask;
import gg.phast.helios.scheduling.builders.util.SchedulerUtil;
import gg.phast.helios.scheduling.eventhandler.TaskEventHandler;
import gg.phast.helios.scheduling.runnables.ScheduledConsumer;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Instant task builder, equivalent of {@link org.bukkit.scheduler.BukkitScheduler#runTask(Plugin, Runnable)}
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public class InstantTaskBuilder extends TaskBuilder {

    private ScheduledConsumer scheduledConsumer;

    /**
     * Sets up consumer for scheduled task, which will be called when running this task
     * @param scheduledConsumer consumer
     * @return builder instance
     * @since 1.0-SNAPSHOT
     */
    public InstantTaskBuilder consumer(@NotNull ScheduledConsumer scheduledConsumer) {
        this.scheduledConsumer = scheduledConsumer;
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
        Objects.requireNonNull(scheduledConsumer, "scheduledConsumer");
        TaskEventHandler eventHandler = buildEventHandler();
        BukkitTask bukkitTask = SchedulerUtil.run(
                Helios.getPlugin(),
                SchedulerUtil.convertHeliosToBukkitConsumer(scheduledConsumer, eventHandler, true),
                0L,
                -1L,
                async
        );

        return HeliosTask.of(bukkitTask, eventHandler);
    }
}
