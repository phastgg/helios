package gg.phast.helios.scheduling.builders;

import gg.phast.helios.Helios;
import gg.phast.helios.scheduling.HeliosTask;
import gg.phast.helios.scheduling.builders.settings.TaskScheduleContext;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * Schedules instant task with specific context
     * @param context context
     * @return helios task
     * @since 1.0-SNAPSHOT
     */
    @Override
    public @NotNull HeliosTask schedule(@Nullable TaskScheduleContext context) {
        Objects.requireNonNull(consumer, "consumer");

        if (context == null) {
            context = TaskScheduleContext.defaultContext();
        }

        return context.getSchedulerHandler().schedule(Helios.getPlugin(), consumer, this.createEventHandler(), 0L, -1L);
    }
}
