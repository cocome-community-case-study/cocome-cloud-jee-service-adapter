package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.NameableEntity;
import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;

import java.io.Serializable;

/**
 * Used as common interface for classes who connect two other entity types with each other
 *
 * @param <FromType> the type of the first interaction partner
 * @param <ToType>   the type of the second interaction partner
 * @author Rudolf Biczok
 */
public interface InteractionEntity<FromType extends NameableEntity,
        ToType extends NameableEntity> extends Serializable, QueryableById {

    /**
     * @return the database id
     */
    long getId();

    /**
     * @param id the database id
     */
    void setId(final long id);

    /**
     * @return the first / source instance
     */
    FromType getFrom();

    /**
     * @param from the first / source instance
     */
    void setFrom(FromType from);

    /**
     * @return the second / destination instance
     */
    ToType getTo();

    /**
     * @param to the second / destination instance
     */
    void setTo(ToType to);

    /**
     * @return the type of source instance
     */
    Class<FromType> getFromClass();

    /**
     * @return the type of the destination instance
     */
    Class<ToType> getToClass();

}
