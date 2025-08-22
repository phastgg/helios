package gg.phast.helios.scheduling.eventhandler;

import org.jetbrains.annotations.NotNull;

/**
 * TaskEventType is used to differentiate different Task's Event types
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public class TaskEventType<T> {

    /**
     * Finish type, called when task finishes up executing the code without any exception or being cancelled <br>
     * <strong>This event is not fired when using Repeating Task</strong>
     */
    public final static TaskEventType<Void> FINISH;
    /**
     * Cancel type, called when task is cancelled no matter what state it is in
     * <strong>It's recommended to check it's state</strong>
     */
    public final static TaskEventType<Void> CANCEL;
    /**
     * Exception type, called only when exception is thrown during execution
     */
    public final static TaskEventType<Exception> EXCEPTION;

    static {
        FINISH = new TaskEventType<>(void.class);
        CANCEL = new TaskEventType<>(void.class);
        EXCEPTION = new TaskEventType<>(Exception.class);
    }

    private final Class<T> dataClass;

    private TaskEventType(final @NotNull Class<T> dataClass) {
        this.dataClass = dataClass;
    }

    public Class<T> getDataClass() {
        return dataClass;
    }
}
