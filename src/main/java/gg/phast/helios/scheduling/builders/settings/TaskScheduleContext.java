package gg.phast.helios.scheduling.builders.settings;

import gg.phast.helios.java.holders.ObjectHolder;
import gg.phast.helios.scheduling.handler.TaskSchedulerHandler;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Internal task schedule context class suited for managing scheduling environment, not suited for outside use except of the builder
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public class TaskScheduleContext {

    private final TaskSchedulerHandler schedulerHandler;

    /**
     * Internal constructor which can be "accessed" through {@link #builder()}
     * @param schedulerHandler scheduler handler
     * @since 1.0-SNAPSHOT
     */
    private TaskScheduleContext(@NotNull TaskSchedulerHandler schedulerHandler) {
        this.schedulerHandler = schedulerHandler;
    }

    /**
     * Returns scheduler handler
     * @return scheduler handler
     * @since 1.0-SNAPSHOT
     */
    public TaskSchedulerHandler getSchedulerHandler() {
        return schedulerHandler;
    }

    /**
     * Creates new builder for creating custom context
     * @return context builder
     * @since 1.0-SNAPSHOT
     */
    @Contract(value = " -> new", pure = true)
    public static TaskScheduleContext.@NotNull Builder builder() {
        return new Builder();
    }

    /**
     * Creates and returns default context
     * @return default context
     * @since 1.0-SNAPSHOT
     */
    public static @NotNull TaskScheduleContext defaultContext() {
        return builder().build();
    }

    /**
     * Builder class for creating new context
     *
     * @author phastgg
     * @since 1.0-SNAPSHOT
     */
    public static class Builder {

        private TaskSchedulerHandler schedulerHandler;

        /**
         * Default constructor
         * @since 1.0-SNAPSHOT
         */
        public Builder() {
        }

        /**
         * Sets scheduler type's handler which is used for scheduling
         * @param type type
         * @param value value (e.g. specific entity for ENTITY handler type)
         * @param <T> generic
         * @return this builder instance
         * @since 1.0-SNAPSHOT
         */
        public <T> Builder setSchedulerHandler(TaskSchedulerHandler.@NotNull Type<T> type, T value) {
            if (!TaskSchedulerHandler.Type.isValidType(type)) {
                throw new IllegalArgumentException("Type is not valid!");
            }

            schedulerHandler = type.createInstance(value);
            return this;
        }

        /**
         * Sets scheduler type's handler which is used for scheduling without a value (e.g. for ASYNC handler type)
         * @param type type
         * @return this builder instance
         * @since 1.0-SNAPSHOT
         */
        public Builder setSchedulerHandler(TaskSchedulerHandler.@NotNull Type<?> type) {
            if (!TaskSchedulerHandler.Type.isValidType(type)) {
                throw new IllegalArgumentException("Type is not valid!");
            }

            if (type.getGeneric() != Void.TYPE) {
                throw new IllegalStateException("Cannot set scheduler handler without value for type '" + type.getName() + "'");
            }

            schedulerHandler = type.createInstance(null);
            return this;
        }

        /**
         * Builds new context based on the data in this builder
         * @return new context
         * @since 1.0-SNAPSHOT
         */
        public @NotNull TaskScheduleContext build() {
            TaskSchedulerHandler schedulerHandler = this.schedulerHandler == null ? TaskSchedulerHandler.Type.SYNC.createInstance(null) : this.schedulerHandler;

            return new TaskScheduleContext(schedulerHandler);
        }
    }
}
