package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.NameableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Represents an operation provided by an plant
 *
 * @author Rudolf Biczok
 */
@Entity
public class RecipeOperation implements Serializable, NameableEntity {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;

    /**
     * @return A unique identifier of this Plant.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    /**
     * @param id a unique identifier of this Plant
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
    @NotNull
    @Basic
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name of the Plant
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }
}
