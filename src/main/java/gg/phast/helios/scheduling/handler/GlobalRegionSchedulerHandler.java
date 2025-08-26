package gg.phast.helios.scheduling.handler;

import gg.phast.helios.Helios;
import gg.phast.helios.java.holders.ObjectHolder;
import gg.phast.helios.scheduling.HeliosTask;
import gg.phast.helios.scheduling.eventhandler.TaskEventHandler;
import gg.phast.helios.scheduling.eventhandler.TaskEventType;
import gg.phast.helios.scheduling.internal.PaperHeliosTask;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Global region scheduler handler that runs tasks in the global region
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public class GlobalRegionSchedulerHandler extends TaskSchedulerHandler {

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
        Consumer<ScheduledTask> paperConsumer = (paperTask) -> {
            if (taskHolder.get() == null) {
                taskHolder.set(PaperHeliosTask.create(eventHandler, paperTask));
            }

            HeliosTask task = taskHolder.get();

            try {
                consumer.accept(task);

                // repeating task never ends except exception or cancel
                if (!paperTask.isRepeatingTask()) {
                    eventHandler.triggerEvent(task, TaskEventType.FINISH, null);
                }
            } catch (Exception exception) {
                Helios.getLogger().severe("Caught exception while executing task!");
                eventHandler.triggerEvent(task, TaskEventType.EXCEPTION, exception);
            }
        };

        GlobalRegionScheduler scheduler = Bukkit.getGlobalRegionScheduler();
        ScheduledTask paperTask;

        if (delay > 0) {
            if (period > 0) {
                paperTask = scheduler.runAtFixedRate(plugin, paperConsumer, delay, period);
            }
            else {
                paperTask = scheduler.runDelayed(plugin, paperConsumer, delay);
            }
        }
        else {
            paperTask = scheduler.run(plugin, paperConsumer);
        }

        taskHolder.set(PaperHeliosTask.create(eventHandler, paperTask));

        return taskHolder.get();
    }
}
