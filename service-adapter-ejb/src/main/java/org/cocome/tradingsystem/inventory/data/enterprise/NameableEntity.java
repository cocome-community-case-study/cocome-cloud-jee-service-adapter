package org.cocome.tradingsystem.inventory.data.enterprise;

/**
 * Marker interface for entity classes that have an id and a name.
 * @author Rudolf Biczok
 */
public interface NameableEntity extends QueryableById {
    /**
     * @return The Entity name
     */
    String getName();

    /**
     * @param name the entity name
     */
    void setName(final String name);

}
