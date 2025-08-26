package gg.phast.helios.scheduling.handler;

import gg.phast.helios.scheduling.HeliosTask;
import gg.phast.helios.scheduling.eventhandler.TaskEventHandler;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Abstract task scheduler handler class, used for handling different scheduling types
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public abstract class TaskSchedulerHandler {

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
    @ApiStatus.Internal
    public abstract HeliosTask schedule(@NotNull JavaPlugin plugin, @NotNull Consumer<HeliosTask> consumer, TaskEventHandler eventHandler, long delay, long period);

    /**
     * Ensures scheduling safety before actually scheduling (by checking parameters)
     * @param plugin plugin
     * @param consumer consumer
     * @param eventHandler event handler
     * @param delay delay
     * @param period period
     * @since 1.0-SNAPSHOT
     */
    protected void ensureSchedulingSafety(JavaPlugin plugin, Consumer<HeliosTask> consumer, TaskEventHandler eventHandler, long delay, long period) {
        Objects.requireNonNull(plugin, "plugin null");
        Objects.requireNonNull(consumer, "consumer null");
        Objects.requireNonNull(eventHandler, "eventHandler null");
        if (delay < 0) {
            throw new IllegalArgumentException("Delay cannot be negative");
        }

        if (period <= 0 && period != -1) {
            throw new IllegalArgumentException("Period cannot be negative except -1");
        }
    }

    /**
     * Type class used for determining which abstract implementation to use
     *
     * @author phastgg
     * @since 1.0-SNAPSHOT
     */
    public static abstract class Type<D> {
        public final static Type<Void> SYNC; // Sync, runs on main thread
        public final static Type<Void> ASYNC; // Async, runs on side thread
        public final static Type<Void> GLOBAL_REGION; // Global Region, runs on global region in case server implementation is Folia,
        public final static Type<Location> REGION; // Region, runs on the region in case server implementation is Folia, otherwise as Sync
        public final static Type<Entity> ENTITY; // Entity, runs in the region the entity is in case server implementation is Folia, otherwise as Sync

        static {
            SYNC = new Type<>(Void.TYPE, "sync") {
                @Override
                public TaskSchedulerHandler createInstance(Void data) {
                    return new SynchronizedSchedulerHandler();
                }
            };
            ASYNC = new Type<>(Void.TYPE, "async") {
                @Override
                public TaskSchedulerHandler createInstance(Void data) {
                    return new AsynchronizedSchedulerHandler();
                }
            };
            GLOBAL_REGION = new Type<>(Void.TYPE, "global_region") {
                @Override
                public TaskSchedulerHandler createInstance(Void data) {
                    return new GlobalRegionSchedulerHandler();
                }
            };
            REGION = new Type<>(Location.class, "region") {
                @Override
                public TaskSchedulerHandler createInstance(Location data) {
                    return new RegionSchedulerHandler(data);
                }
            };
            ENTITY = new Type<>(Entity.class, "entity") {
                @Override
                public TaskSchedulerHandler createInstance(Entity data) {
                    return new EntitySchedulerHandler(data);
                }
            };
        }

        private final Class<D> generic;
        private final String name;

        /**
         * Constructor
         * @param generic generic of value that may be used when creating new instance
         * @param name identifier
         * @since 1.0-SNAPSHOT
         */
        private Type(Class<D> generic, String name) {
            this.generic = generic;
            this.name = name;
        }

        /**
         * Returns generic class
         * @return generic class
         * @since 1.0-SNAPSHOT
         */
        public Class<D> getGeneric() {
            return generic;
        }

        /**
         * Returns name
         * @return name
         * @since 1.0-SNAPSHOT
         */
        public String getName() {
            return name;
        }

        /**
         * Abstract method for creating instance
         * @param data additional data
         * @return task scheduler handler
         * @since 1.0-SNAPSHOT
         */
        public abstract TaskSchedulerHandler createInstance(D data);

        /**
         * Ensures type is valid
         * @param type type
         * @return whether type is valid
         * @since 1.0-SNAPSHOT
         */
        public static boolean isValidType(@NotNull TaskSchedulerHandler.Type<?> type) {
            return type == SYNC || type == ASYNC || type == GLOBAL_REGION || type == REGION || type == ENTITY;
        }
    }
}
