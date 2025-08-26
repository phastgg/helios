package gg.phast.helios.scheduling.handler;

import gg.phast.helios.Helios;
import gg.phast.helios.java.holders.ObjectHolder;
import gg.phast.helios.scheduling.HeliosTask;
import gg.phast.helios.scheduling.eventhandler.TaskEventHandler;
import gg.phast.helios.scheduling.eventhandler.TaskEventType;
import gg.phast.helios.scheduling.internal.BukkitHeliosTask;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.scheduler.CraftScheduler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Synchronized (sync) scheduler handler that runs tasks on the main thread
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public class SynchronizedSchedulerHandler extends TaskSchedulerHandler {

    /**
     * Schedules consumer with specific parameters, for internal API use
     * @param plugin plugin
     * @param consumer consumer
     * @param eventHandler event handler
     * @param delay delay
     * @param period period
     * @return helios task
     * @since 1.0-SNAPSHOT
     */
    @Override
    public HeliosTask schedule(@NotNull JavaPlugin plugin, @NotNull Consumer<HeliosTask> consumer, TaskEventHandler eventHandler, long delay, long period) {
        ensureSchedulingSafety(plugin, consumer, eventHandler, delay, period);

        ObjectHolder<HeliosTask> taskHolder = new ObjectHolder<>();
        Consumer<BukkitTask> bukkitConsumer = (bukkitTask) -> {
            if (taskHolder.get() == null) {
                taskHolder.set(BukkitHeliosTask.create(eventHandler, bukkitTask));
            }

            HeliosTask task = taskHolder.get();

            try {
                consumer.accept(task);

                // repeating task never ends except exception or cancel
                if (period >= 0) {
                    eventHandler.triggerEvent(task, TaskEventType.FINISH, null);
                }
            } catch (Exception exception) {
                Helios.getLogger().severe("Caught exception while executing task!");
                eventHandler.triggerEvent(task, TaskEventType.EXCEPTION, exception);
            }
        };
        BukkitTask bukkitTask = ((CraftScheduler) Bukkit.getScheduler()).runTaskTimer(plugin, (Object) bukkitConsumer, delay, period);

        taskHolder.set(BukkitHeliosTask.create(eventHandler, bukkitTask));

        return taskHolder.get();
    }
}
