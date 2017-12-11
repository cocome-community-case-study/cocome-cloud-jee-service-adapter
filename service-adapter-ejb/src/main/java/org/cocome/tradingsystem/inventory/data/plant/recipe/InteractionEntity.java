package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.NameableEntity;
import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;

import java.io.Serializable;

/**
 * Used as common interface for classes who connect two other entity types with each other
 *
 * @param <T> the interaction type
 * @author Rudolf Biczok
 */
public interface InteractionEntity<T extends NameableEntity>
        extends Serializable, QueryableById {

    /**
     * @return the database id
     */
    long getId();

    /**
     * @param id the database id
     */
    void setId(final long id);

    /**
     * @return the recipe operation
     */
    Recipe getRecipe();

    /**
     * @param operation the recipe operation
     */
    void setRecipe(Recipe operation);

    /**
     * @return the first / source instance
     */
    T getFrom();

    /**
     * @param from the first / source instance
     */
    void setFrom(T from);

    /**
     * @return the second / destination instance
     */
    T getTo();

    /**
     * @param to the second / destination instance
     */
    void setTo(T to);

    /**
     * @return the type of source instance
     */
    Class<T> getInteractionType();
}
