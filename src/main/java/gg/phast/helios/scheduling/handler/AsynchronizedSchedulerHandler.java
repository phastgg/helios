package gg.phast.helios.scheduling.handler;

import gg.phast.helios.Helios;
import gg.phast.helios.java.holders.ObjectHolder;
import gg.phast.helios.scheduling.HeliosTask;
import gg.phast.helios.scheduling.eventhandler.TaskEventHandler;
import gg.phast.helios.scheduling.eventhandler.TaskEventType;
import gg.phast.helios.scheduling.internal.PaperHeliosTask;
import io.papermc.paper.threadedregions.scheduler.FoliaAsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Asynchronized (async) scheduler handler that runs tasks on the side thread
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public class AsynchronizedSchedulerHandler extends TaskSchedulerHandler {

    private final static Method SCHEDULE_METHOD;

    static {
        try {
            SCHEDULE_METHOD = FoliaAsyncScheduler.class.getDeclaredMethod("scheduleTimerTask", Plugin.class, Consumer.class, long.class, long.class, TimeUnit.class);
            SCHEDULE_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

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

        ScheduledTask paperTask;
        try {
            paperTask = (ScheduledTask) SCHEDULE_METHOD.invoke(Bukkit.getAsyncScheduler(), plugin, paperConsumer, delay * 50L, period * 50L, TimeUnit.MILLISECONDS);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        taskHolder.set(PaperHeliosTask.create(eventHandler, paperTask));

        return taskHolder.get();
    }
}
