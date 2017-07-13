package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.NameableEntity;
import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Used as common interface for classes who connect two other entity types with each other
 * @param <FromType> the type of the first interaction partner
 * @param <ToType> the type of the second interaction partner
 * @author Rudolf Biczok
 */
@Entity
public abstract class InteractionEntity<FromType extends NameableEntity,
                                        ToType extends NameableEntity> implements Serializable, QueryableById {
    private static final long serialVersionUID = 1L;

    private long id;
    private FromType from;
    private ToType to;

    /**
     * @return the database id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    /**
     * @param id
     *            the database id
     */
    @Override
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return the first / source instance
     */
    @OneToOne(fetch = FetchType.EAGER)
    public FromType getFrom() {
        return from;
    }

    /**
     * @param from the first / source instance
     */
    public void setFrom(FromType from) {
        this.from = from;
    }

    /**
     * @return the second / destination instance
     */
    @OneToOne(fetch = FetchType.EAGER)
    public ToType getTo() {
        return to;
    }

    /**
     * @param to the second / destination instance
     */
    public void setTo(ToType to) {
        this.to = to;
    }

    /**
     * @return the type of source instance
     */
    @Transient
    public abstract Class<FromType> getFromClass();

    /**
     * @return the type of the destination instance
     */
    @Transient
    public abstract Class<ToType> getToClass();

}
