package gg.phast.helios.scheduling.builders;

import com.google.common.base.Preconditions;
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
 * Repeating task builder, equivalent of {@link org.bukkit.scheduler.BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)}
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public class RepeatingTaskBuilder extends TaskBuilder {

    private Long period;
    private Long delay = 0L;
    private ScheduledConsumer scheduledConsumer;

    /**
     * Sets up period for scheduled task
     * @param period period (must be greater than 0)
     * @return builder instance
     * @since 1.0-SNAPSHOT
     */
    public RepeatingTaskBuilder period(long period) {
        Preconditions.checkArgument(period > 0, "period must be greater than 0");
        this.period = period;
        return this;
    }

    /**
     * Sets up delay for scheduled task
     * @param delay delay (must be equals or greater than 0)
     * @return builder instance
     * @since 1.0-SNAPSHOT
     */
    public RepeatingTaskBuilder delay(long delay) {
        Preconditions.checkArgument(delay >= 0, "delay must be equals or greater than 0");
        this.delay = delay;
        return this;
    }

    /**
     * Sets up consumer for scheduled task, which will be called when running this task
     * @param scheduledConsumer consumer
     * @return builder instance
     * @since 1.0-SNAPSHOT
     */
    public RepeatingTaskBuilder consumer(@NotNull ScheduledConsumer scheduledConsumer) {
        this.scheduledConsumer = scheduledConsumer;
        return this;
    }

    /**
     * Schedules repeating task, equivalent of {@link org.bukkit.scheduler.BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)}
     * @param async whether task should be async
     * @return bukkit task wrapper
     * @since 1.0-SNAPSHOT
     */
    @Override
    protected @NotNull HeliosTask schedule(boolean async) {
        Objects.requireNonNull(period, "period");
        Objects.requireNonNull(delay, "delay");
        Objects.requireNonNull(scheduledConsumer, "scheduledConsumer");
        TaskEventHandler eventHandler = buildEventHandler();
        BukkitTask bukkitTask = SchedulerUtil.run(
                Helios.getPlugin(),
                SchedulerUtil.convertHeliosToBukkitConsumer(scheduledConsumer, eventHandler, true),
                delay,
                period,
                async
        );

        return HeliosTask.of(bukkitTask, eventHandler);
    }
}
