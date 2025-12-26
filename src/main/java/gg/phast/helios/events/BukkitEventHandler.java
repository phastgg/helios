package gg.phast.helios.events;

import gg.phast.helios.Helios;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Event handler enabling us to easily handle events
 * easily with this wrapper, to start, create builder using
 * {@link #builder(Class, Consumer)}
 *
 * @since 1.0-SNAPSHOT
 * @author phastgg
 * @param <T> specific event
 */
public class BukkitEventHandler<T extends Event> {

    private final Class<T> eventClass;
    private final Consumer<BukkitEventContext<T>> consumer;
    private final EventPriority eventPriority;
    private final boolean ignoreCancelled;
    private final EventExecutor eventExecutor;
    private final Listener listener;

    /**
     * Constructor, to create new event handler, use builder {@link #builder(Class, Consumer)}
     * @param eventClass event class
     * @param consumer consumer (specifies how is event used)
     * @param eventPriority priority of the event (def: NORMAL)
     * @param ignoreCancelled whether is event handled even after being already cancelled (def: false)
     * @since 1.0-SNAPSHOT
     */
    protected BukkitEventHandler(
            final @NotNull Class<T> eventClass,
            final @NotNull Consumer<BukkitEventContext<T>> consumer,
            final @NotNull EventPriority eventPriority,
            final boolean ignoreCancelled
    ) {
        Listener listener = new Listener() {
            @EventHandler
            public void onEvent(final @NotNull T event) {
                handle(event);
            }
        };
        Method eventMethod;
        try {
            eventMethod = listener.getClass().getMethod("onEvent", Event.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        EventExecutor eventExecutor = EventExecutor.create(eventMethod, Event.class);

        this.eventClass = eventClass;
        this.consumer = consumer;
        this.eventPriority = eventPriority;
        this.ignoreCancelled = ignoreCancelled;
        this.eventExecutor = eventExecutor;
        this.listener = listener;
        register();
    }

    /**
     * Wraps a wrapper around event and then calls it to consumer
     * @since 1.0-SNAPSHOT
     * @param event event
     */
    private void handle(final @NotNull T event) {
        consumer.accept(new BukkitEventContext<>(this, event));
    }

    /**
     * Registers event, wrapper is already automatically registered in constructor
     * @since 1.0-SNAPSHOT
     */
    public void register() {
        Bukkit.getPluginManager().registerEvent(eventClass, listener, eventPriority, eventExecutor, Helios.getPlugin(), ignoreCancelled);
    }

    /**
     * Un-registers event
     * @since 1.0-SNAPSHOT
     */
    public void unregister() {
        HandlerList.unregisterAll(listener);
    }

    /**
     * Creates new builder
     * @since 1.0-SNAPSHOT
     * @param eventClass event class
     * @param consumer consumer for handling event
     * @return new builder instance
     * @param <T> specific event
     */
    @Contract(value = "_,_ -> new", pure = true)
    public static <T extends Event> BukkitEventHandler.@NotNull Builder<T> builder(final @NotNull Class<T> eventClass, final @NotNull Consumer<BukkitEventContext<T>> consumer) {
        return new BukkitEventHandler.Builder<>(eventClass, consumer);
    }

    /**
     * Builder which helps us create the handler
     * @since 1.0-SNAPSHOT
     * @param <T> specific event
     */
    public static class Builder<T extends Event> {

        private final Class<T> eventClass;
        private final Consumer<BukkitEventContext<T>> consumer;
        private EventPriority eventPriority = EventPriority.NORMAL;
        private boolean ignoreCancelled = false;

        /**
         * Constructor, creates new builder with required parameters, others are optional
         * @since 1.0-SNAPSHOT
         * @param eventClass event class
         * @param consumer consumer
         */
        protected Builder(final Class<T> eventClass, final @NotNull Consumer<BukkitEventContext<T>> consumer) {
            this.eventClass = eventClass;
            this.consumer = consumer;
        }

        /**
         * Specifies priority of handling event,
         * LOW is handled first and HIGHEST last,
         * if you use MONITOR which is after HIGHEST,
         * <br>DO NOT MAKE MODIFICATIONS TO THE EVENT!
         * @since 1.0-SNAPSHOT
         * @param eventPriority event priority (Default: NORMAL)
         * @return builder instance
         */
        public Builder<T> priority(final @NotNull EventPriority eventPriority) {
            Objects.requireNonNull(eventPriority, "eventPriority");
            this.eventPriority = eventPriority;
            return this;
        }

        /**
         * Specifies whether event is handled even
         * after being cancelled
         * @since 1.0-SNAPSHOT
         * @param ignoreCancelled whether to handle cancelled event
         * @return builder instance
         */
        public Builder<T> ignoreCancelled(final boolean ignoreCancelled) {
            this.ignoreCancelled = ignoreCancelled;
            return this;
        }

        /**
         * Creates new bukkit event handler,
         * it is important to call this method otherwise,
         * nothing will be registered!
         * @since 1.0-SNAPSHOT
         * @return new bukkit event handler
         */
        public BukkitEventHandler<T> listen() {
            return new BukkitEventHandler<>(eventClass, consumer, eventPriority, ignoreCancelled);
        }
    }
}
