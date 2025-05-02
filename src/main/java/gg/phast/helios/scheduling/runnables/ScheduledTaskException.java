package gg.phast.helios.scheduling.runnables;

import gg.phast.helios.scheduling.HeliosTask;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Consumer;

/**
 * Functional interface used for working with excepting in
 * {@link gg.phast.helios.scheduling.eventhandler.TaskEventHandler#triggerOnException(HeliosTask, Exception)},
 * functional method is inherited method {@link Consumer#accept(Object)}
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
@FunctionalInterface
public interface ScheduledTaskException extends Consumer<Pair<HeliosTask, Exception>> {
}
