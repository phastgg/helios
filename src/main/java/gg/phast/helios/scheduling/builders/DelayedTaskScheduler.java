package gg.phast.helios.scheduling.builders;

import com.google.common.base.Preconditions;
import gg.phast.helios.Helios;
import gg.phast.helios.scheduling.HeliosTask;
import gg.phast.helios.scheduling.builders.settings.TaskScheduleContext;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Delayed task builder, equivalent of {@link org.bukkit.scheduler.BukkitScheduler#runTaskLater(Plugin, Consumer, long)}
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public class DelayedTaskScheduler extends TaskScheduler {

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
     * Schedules delayed task with specific context
     * @param context context
     * @return helios task
     * @since 1.0-SNAPSHOT
     */
    @Override
    public @NotNull HeliosTask schedule(@Nullable TaskScheduleContext context) {
        Objects.requireNonNull(delay, "delay");
        Objects.requireNonNull(consumer, "consumer");

        if (context == null) {
            context = TaskScheduleContext.defaultContext();
        }

        return context.getSchedulerHandler().schedule(Helios.getPlugin(), consumer, this.createEventHandler(), delay, -1L);
    }
}
