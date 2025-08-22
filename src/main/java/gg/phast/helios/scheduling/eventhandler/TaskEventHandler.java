package gg.phast.helios.scheduling.eventhandler;

import gg.phast.helios.scheduling.HeliosTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Event handler designed for being used when scheduling task
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public final class TaskEventHandler {

    private final Map<TaskEventType<?>, List<Consumer<TaskEventCallData<?>>>> map;

    /**
     * Constructor
     * @param map map containing all event consumers
     * @since 1.0-SNAPSHOT
     */
    public TaskEventHandler(Map<TaskEventType<?>, List<Consumer<TaskEventCallData<?>>>> map) {
        this.map = map == null ? new HashMap<>() : map;
    }

    public <T> void triggerEvent(@NotNull final HeliosTask task, @NotNull TaskEventType<T> eventType, @Nullable final T data) {
        TaskEventCallData<T> callData = new TaskEventCallData<>(task, data);
        map.get(eventType).forEach(caller -> caller.accept(callData));
    }
}
