package gg.phast.helios.scheduling.builders;

import gg.phast.helios.scheduling.HeliosTask;
import gg.phast.helios.scheduling.eventhandler.TaskEventCallData;
import gg.phast.helios.scheduling.eventhandler.TaskEventHandler;
import gg.phast.helios.scheduling.eventhandler.TaskEventType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Abstract task builder class, which is being extended
 * by {@link InstantTaskScheduler}, {@link DelayedTaskScheduler} and {@link RepeatingTaskScheduler}
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public abstract class TaskBuilder {

    private final Map<TaskEventType<?>, List<Consumer<? extends TaskEventCallData<?>>>> eventTasks = new HashMap<>();
    private boolean scheduled = false;

    /**
     * Constructor
     * @since 1.0-SNAPSHOT
     */
    public TaskBuilder() {
    }

    /**
     * Listens to specific task event type and runs code whenever the event is triggered
     * @param eventType event type
     * @param consumer consumer
     * @return builder instance
     * @param <T> generic
     */
    @SuppressWarnings("unchecked")
    public <T> TaskBuilder onEvent(TaskEventType<T> eventType, Consumer<TaskEventCallData<T>> consumer) {
        List<Consumer<? extends TaskEventCallData<T>>> list = (List<Consumer<? extends TaskEventCallData<T>>>) (List<?>) eventTasks.getOrDefault(eventType, new ArrayList<>());
        list.add(consumer);
        eventTasks.put(eventType, (List<Consumer<? extends TaskEventCallData<?>>>) (List<?>) list);
        return this;
    }

    /**
     * Creates event handler based on data previously specified
     * @return task event handler
     */
    @SuppressWarnings("unchecked")
    protected TaskEventHandler createEventHandler() {
        return new TaskEventHandler(
                (Map<TaskEventType<?>, List<Consumer<TaskEventCallData<?>>>>)
                (Map<TaskEventType<?>, ?>)
                eventTasks
        );
    }

    /**
     * Schedules sync task according to settings in specific builder
     * @return bukkit task wrapper
     * @since 1.0-SNAPSHOT
     */
    public @NotNull HeliosTask schedule() {
        if (scheduled) {
            throw new IllegalStateException("TaskBuilder has already been used to schedule a task");
        }
        scheduled = true;
        return schedule(false);
    }

    /**
     * Schedules async task according to settings in specific builder
     * @return bukkit task wrapper
     * @since 1.0-SNAPSHOT
     */
    public @NotNull HeliosTask scheduleAsync() {
        if (scheduled) {
            throw new IllegalStateException("TaskBuilder has already been used to schedule a task");
        }
        scheduled = true;
        return schedule(true);
    }

    /**
     * Abstract schedule method used to determine schedule method in every builder
     * @param async whether task should be async
     * @return bukkit task wrapper
     * @since 1.0-SNAPSHOT
     */
    protected abstract @NotNull HeliosTask schedule(boolean async);
}

