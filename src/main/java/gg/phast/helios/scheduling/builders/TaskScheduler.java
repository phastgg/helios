package gg.phast.helios.scheduling.builders;

import gg.phast.helios.scheduling.HeliosTask;
import gg.phast.helios.scheduling.builders.settings.TaskScheduleContext;
import gg.phast.helios.scheduling.eventhandler.TaskEventCallData;
import gg.phast.helios.scheduling.eventhandler.TaskEventHandler;
import gg.phast.helios.scheduling.eventhandler.TaskEventType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Abstract task scheduler class, which is being extended
 * by {@link InstantTaskScheduler}, {@link DelayedTaskScheduler} and {@link RepeatingTaskScheduler}
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public abstract class TaskScheduler {

    private final Map<TaskEventType<?>, List<Consumer<? extends TaskEventCallData<?>>>> eventTasks = new HashMap<>();

    /**
     * Constructor
     * @since 1.0-SNAPSHOT
     */
    public TaskScheduler() {
    }

    /**
     * Listens to specific task event type and runs code whenever the event is triggered
     * @param eventType event type
     * @param consumer consumer
     * @return builder instance
     * @param <T> generic
     * @since 1.0-SNAPSHOT
     */
    @SuppressWarnings("unchecked")
    public <T> TaskScheduler onEventContext(TaskEventType<T> eventType, Consumer<TaskEventCallData<T>> consumer) {
        List<Consumer<? extends TaskEventCallData<T>>> list = (List<Consumer<? extends TaskEventCallData<T>>>) (List<?>) eventTasks.getOrDefault(eventType, new ArrayList<>());
        list.add(consumer);
        eventTasks.put(eventType, (List<Consumer<? extends TaskEventCallData<?>>>) (List<?>) list);
        return this;
    }

    /**
     * Schedules task to run without any context, running on default settings
     * @return helios task
     * @since 1.0-SNAPSHOT
     */
    public @NotNull HeliosTask schedule() {
        return schedule(null);
    }

    /**
     * Abstract schedule method with context consumer
     * @return helios task
     * @since 1.0-SNAPSHOT
     */
    public abstract @NotNull HeliosTask schedule(@Nullable TaskScheduleContext context);

    /**
     * Creates event handler based on data previously specified
     * @return task event handler
     * @since 1.0-SNAPSHOT
     */
    @SuppressWarnings("unchecked")
    protected TaskEventHandler createEventHandler() {
        return new TaskEventHandler(
                (Map<TaskEventType<?>, List<Consumer<TaskEventCallData<?>>>>)
                (Map<TaskEventType<?>, ?>)
                eventTasks
        );
    }
}

