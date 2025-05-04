package gg.phast.helios.scheduling.builders.util;

import gg.phast.helios.scheduling.HeliosTask;
import gg.phast.helios.scheduling.eventhandler.TaskEventHandler;
import gg.phast.helios.scheduling.runnables.ScheduledConsumer;
import io.netty.util.internal.UnstableApi;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.scheduler.CraftScheduler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Util used when scheduling in helios scheduling api,
 * this util should not be used in production environment
 * as it can be subject to change anytime and is not considered
 * as stable api
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public class SchedulerUtil {

    private static final CraftScheduler SCHEDULER = ((CraftScheduler) Bukkit.getServer().getScheduler());

    /**
     * Runs task accordingly and returns bukkit task
     * @param javaPlugin java plugin
     * @param consumer consumer
     * @param delay delay
     * @param period period
     * @param async async
     * @return bukkit task
     * @since 1.0-SNAPSHOT
     */
    @ApiStatus.Internal
    @UnstableApi
    public static BukkitTask run(@NotNull JavaPlugin javaPlugin, @NotNull Consumer<BukkitTask> consumer, long delay, long period, boolean async) {
        if (async) {
            return SCHEDULER.runTaskTimerAsynchronously(javaPlugin, (Object) consumer, delay, period);
        }
        return SCHEDULER.runTaskTimer(javaPlugin, (Object) consumer, delay, period);
    }

    /**
     * Converts helios consumer ({@link ScheduledConsumer}) to {@link Consumer<BukkitTask>}
     * @param scheduledConsumer helios consumer
     * @param taskEventHandler event handler for firing actions
     * @param isRepeatingTask whether task is repeating, used for task
     *                        event handler to determine whether fire {@link TaskEventHandler#triggerOnFinish(HeliosTask)}
     * @return bukkit consumer
     * @since 1.0-SNAPSHOT
     */
    @Contract("_, _, _ -> new")
    @ApiStatus.Internal
    @UnstableApi
    public static @NotNull Consumer<BukkitTask> convertHeliosToBukkitConsumer(
            final @NotNull ScheduledConsumer scheduledConsumer,
            final @NotNull TaskEventHandler taskEventHandler,
            final boolean isRepeatingTask
    ) {
        Objects.requireNonNull(scheduledConsumer, "scheduledConsumer");
        return new Consumer<>() {
            private HeliosTask heliosTask;

            @Override
            public void accept(BukkitTask bukkitTask) {
                if (heliosTask == null) {
                    heliosTask = HeliosTask.of(bukkitTask, taskEventHandler);
                }

                try {
                    scheduledConsumer.accept(heliosTask);

                    // repeating task never ends except exception or cancel
                    if (!isRepeatingTask) {
                        taskEventHandler.triggerOnFinish(heliosTask);
                    }
                } catch (Exception e) {
                    taskEventHandler.triggerOnException(heliosTask, e);
                }
            }
        };
    }
}
