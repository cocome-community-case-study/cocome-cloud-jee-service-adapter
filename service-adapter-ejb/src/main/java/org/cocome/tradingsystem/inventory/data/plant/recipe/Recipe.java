package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Represents the top-level recipe for producing a custom product.
 * @author Rudolf Biczok
 */
@Entity
public class Recipe implements Serializable, QueryableById {
    private static final long serialVersionUID = 1L;

    private long id;

    // Represent the vertices of the recipe graph
    private Collection<PlantOperation> operations;

    // Represent the edges of the recipe graph
    private Collection<ParameterInteraction> parameterInteractions;
    private Collection<EntryPointInteraction> inputInteractions;
    private Collection<EntryPointInteraction> outputInteractions;

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
     * @return the plant operation that is supposed to be executed within this interaction step
     */
    @OneToMany
    public Collection<PlantOperation> getOperations() {
        return operations;
    }

    /**
     * @param operations the plant operation that is supposed to be executed within this interaction step
     */
    public void setOperations(Collection<PlantOperation> operations) {
        this.operations = operations;
    }

    /**
     * @return the list of incoming interactions
     */
    @OneToMany
    public Collection<EntryPointInteraction> getInputInteractions() {
        return inputInteractions;
    }

    /**
     * @param inputInteractions the list of incoming interactions
     */
    public void setInputInteractions(Collection<EntryPointInteraction> inputInteractions) {
        this.inputInteractions = inputInteractions;
    }

    /**
     * @return the list of outgoing interactions
     */
    @OneToMany
    public Collection<EntryPointInteraction> getOutputInteractions() {
        return outputInteractions;
    }

    /**
     * @param outputInteractions the list of outgoing interaction
     */
    public void setOutputInteractions(Collection<EntryPointInteraction> outputInteractions) {
        this.outputInteractions = outputInteractions;
    }

    /**
     * @return the list of parameter bindings
     */
    @OneToMany
    public Collection<ParameterInteraction> getParameterInteractions() {
        return parameterInteractions;
    }

    /**
     * @param parameterInteractions the list of parameter bindings
     */
    public void setParameterInteractions(Collection<ParameterInteraction> parameterInteractions) {
        this.parameterInteractions = parameterInteractions;
    }
}
