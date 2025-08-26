package gg.phast.helios.java.holders;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 * A thread-safe generic container that holds a single object of type T.
 * This class provides both synchronous and asynchronous access methods
 * for getting and setting the contained value.
 *
 * <p>Thread Safety: This class is thread-safe and can be used safely
 * in concurrent environments. It uses a ReadWriteLock to allow multiple
 * concurrent readers while ensuring exclusive access for writers.</p>
 *
 * @param <T> the type of object to hold
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public class ObjectHolder<T> {

    private volatile T value;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Constructs an ObjectHolder with the specified initial value.
     *
     * @param value the initial value to hold (can be null)
     * @since 1.0-SNAPSHOT
     */
    public ObjectHolder(T value) {
        this.value = value;
    }

    /**
     * Constructs an ObjectHolder with a null initial value.
     *
     * @since 1.0-SNAPSHOT
     */
    public ObjectHolder() {
        this.value = null;
    }

    /**
     * Gets the current value held by this container.
     * This method is thread-safe and allows concurrent access by multiple threads.
     *
     * @return the current value (may be null)
     * @since 1.0-SNAPSHOT
     */
    public @Nullable T get() {
        lock.readLock().lock();
        try {
            return value;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Sets the value held by this container.
     * This method is thread-safe and ensures exclusive access during the update.
     *
     * @param value the new value to set (can be null)
     * @since 1.0-SNAPSHOT
     */
    public void set(@Nullable T value) {
        lock.writeLock().lock();
        try {
            this.value = value;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Checks if the container currently holds a null value.
     *
     * @return true if the value is null, false otherwise
     * @since 1.0-SNAPSHOT
     */
    public boolean isEmpty() {
        return get() == null;
    }

    /**
     * Checks if the container currently holds a non-null value.
     *
     * @return true if the value is not null, false otherwise
     * @since 1.0-SNAPSHOT
     */
    public boolean isPresent() {
        return get() != null;
    }

    /**
     * Returns a string representation of this ObjectHolder.
     *
     * @return string representation including the current value
     * @since 1.0-SNAPSHOT
     */
    @Override
    public String toString() {
        T currentValue = get();
        return String.format("ObjectHolder{value=%s}", currentValue);
    }

    /**
     * Compares this ObjectHolder with another object for equality.
     * Two ObjectHolders are equal if they hold equal values.
     *
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     * @since 1.0-SNAPSHOT
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ObjectHolder<?> other = (ObjectHolder<?>) obj;
        T thisValue = get();
        Object otherValue = other.get();

        return Objects.equals(thisValue, otherValue);
    }

    /**
     * Returns a hash code for this ObjectHolder based on its current value.
     *
     * @return hash code of the current value, or 0 if value is null
     * @since 1.0-SNAPSHOT
     */
    @Override
    public int hashCode() {
        T currentValue = get();
        return currentValue != null ? currentValue.hashCode() : 0;
    }
}
