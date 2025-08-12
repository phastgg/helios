package gg.phast.helios.scheduling.builders;

import com.google.common.base.Preconditions;
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
 * Delayed task builder, equivalent of {@link org.bukkit.scheduler.BukkitScheduler#runTaskLater(Plugin, Consumer, long)}
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public class DelayedTaskScheduler extends TaskBuilder {

    private Integer delay;
    private Consumer<HeliosTask> consumer;

    /**
     * Sets up delay for scheduled task
     * @param delay delay (must be equals or greater than 0)
     * @return builder instance
     * @since 1.0-SNAPSHOT
     */
    public DelayedTaskScheduler delay(int delay) {
        Preconditions.checkArgument(delay >= 0, "delay must be greater or equal to 0");
        this.delay = delay;
        return this;
    }

    /**
     * Sets up consumer for scheduled task, which will be called when running this task
     * @param consumer consumer
     * @return builder instance
     * @since 1.0-SNAPSHOT
     */
    public DelayedTaskScheduler execute(Consumer<HeliosTask> consumer) {
        Objects.requireNonNull(consumer, "consumer");
        this.consumer = consumer;
        return this;
    }

    /**
     * Schedules delayed task, equivalent of {@link org.bukkit.scheduler.BukkitScheduler#runTaskLater(Plugin, Runnable, long)}
     * @param async whether task should be async
     * @return bukkit task wrapper
     * @since 1.0-SNAPSHOT
     */
    @Override
    protected @NotNull HeliosTask schedule(boolean async) {
        Objects.requireNonNull(delay, "delay");
        Objects.requireNonNull(consumer, "consumer");
        TaskEventHandler eventHandler = createEventHandler();
        BukkitTask bukkitTask = SchedulerUtil.run(
                Helios.getPlugin(),
                SchedulerUtil.convertHeliosToBukkitConsumer(consumer, eventHandler, false),
                delay,
                -1L,
                async
        );

        return HeliosTask.of(bukkitTask, eventHandler);
    }
}
