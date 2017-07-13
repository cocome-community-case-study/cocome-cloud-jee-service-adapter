package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.NameableEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Used to connection ports between {@link PlantOperation}
 */
@Entity
public class EntryPoint implements Serializable, NameableEntity {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;

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
     * Returns the name of the Plant.
     *
     * @return Plant name.
     */
    @Override
    @Basic
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     *            the name of the Plant
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

}
