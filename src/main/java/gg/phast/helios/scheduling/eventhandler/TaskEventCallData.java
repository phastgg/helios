package gg.phast.helios.scheduling.eventhandler;

import gg.phast.helios.scheduling.HeliosTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Task Event Call Data used when {@link gg.phast.helios.scheduling.builders.TaskBuilder#onEvent(TaskEventType, Consumer)}
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public record TaskEventCallData<T>(@NotNull HeliosTask task, @Nullable T data) { }