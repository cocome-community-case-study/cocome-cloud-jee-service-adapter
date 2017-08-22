package org.cocome.tradingsystem.inventory.data.enterprise;

/**
 * Marker interface for entity classes that have an embedded id.
 * @param <T> embedded id type
 * @author Rudolf Biczok
 */
public interface QueryableByIdClass<T> {
    /**
     * Gets identifier value
     *
     * @return The id.
     */
    T getId();

    /**
     * @param id the identifier value
     */
    void setId(final T id);
}
