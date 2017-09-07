package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.NameableEntity;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.expression.Expression;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Represents an operation provided by an plant
 * @author Rudolf Biczok
 */
@Entity
public class PlantOperation implements Serializable, NameableEntity {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private Plant plant;
    private Collection<Expression> expressions;
    private Collection<EntryPoint> inputEntryPoint;
    private Collection<EntryPoint> outputEntryPoint;

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
     * @param id
     *            a unique identifier of this Plant
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

    /**
     * @return all expressions used to control the plant-local production flow.
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Collection<Expression> getExpressions() {
        return expressions;
    }

    /**
     * @param expressions all expressions used to control the plant-local production flow.
     */
    public void setExpressions(Collection<Expression> expressions) {
        this.expressions = expressions;
    }

    /**
     * @return all material classes that are required for operation execution
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Collection<EntryPoint> getInputEntryPoint() {
        return inputEntryPoint;
    }

    /**
     * @param inputMaterial all material classes that are required for operation execution
     */
    public void setInputEntryPoint(Collection<EntryPoint> inputMaterial) {
        this.inputEntryPoint = inputMaterial;
    }

    /**
     * @return all material classes that results after the operation execution
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Collection<EntryPoint> getOutputEntryPoint() {
        return outputEntryPoint;
    }

    /**
     * @param outputMaterial all material classes that results after the operation execution
     */
    public void setOutputEntryPoint(Collection<EntryPoint> outputMaterial) {
        this.outputEntryPoint = outputMaterial;
    }

    /**
     * @return the plant that owns this production unit
     */
    @ManyToOne(cascade = CascadeType.ALL)
    public Plant getPlant() {
        return plant;
    }

    /**
     * @param plant the plant that owns this production unit
     */
    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}