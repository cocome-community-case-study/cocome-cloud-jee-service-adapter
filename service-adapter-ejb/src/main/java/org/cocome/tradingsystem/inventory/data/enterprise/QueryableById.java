package org.cocome.tradingsystem.inventory.data.enterprise;

/**
 * Marker interface for entity classes that have an id.
 * @author Rudolf Biczok
 */
public interface QueryableById {
    /**
     * Gets identifier value
     *
     * @return The id.
     */
    long getId();

    /**
     * @param id the identifier value
     */
    void setId(final long id);
}
