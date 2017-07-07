package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Used to connection ports between {@link PlantOperation}
 */
@Entity
public class EntryPoint implements Serializable, QueryableById {
    private static final long serialVersionUID = 1L;

    private long id;

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
    public void setId(final long id) {
        this.id = id;
    }
}
