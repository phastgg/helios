package gg.phast.helios.events;

import org.bukkit.event.Event;

/**
 * Simple wrapper around event and handler providing clear options
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 * @param handler handler which handles event,
 *                provides methods for manipulation with listener
 * @param event called event
 * @param <T> specific event
 */
public record BukkitEventContext<T extends Event>(BukkitEventHandler<T> handler, T event) {
}
