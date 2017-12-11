package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.NameableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Used to connection ports between {@link PlantOperation}
 */
@Entity
public class EntryPoint implements Serializable, NameableEntity {
    public enum Direction {INPUT, OUTPUT}

    private static final long serialVersionUID = 1L;

    private long id;
    private String name;

    private RecipeOperation operation;
    private Direction direction;

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
     * @param id the database id
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
     * @param name the name of the Plant
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @NotNull
    @ManyToOne
    public RecipeOperation getOperation() {
        return operation;
    }

    public void setOperation(RecipeOperation operation) {
        this.operation = operation;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
