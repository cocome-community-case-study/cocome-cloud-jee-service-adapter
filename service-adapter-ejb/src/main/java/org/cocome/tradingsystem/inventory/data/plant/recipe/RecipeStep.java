package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Represents an production step required to produce a product.
 * It encapsulates exactly one {@link PlantOperation} by specifying
 * all its parametrization and the incoming {@link EntryPoint}
 * It also specifies what to do with tne outgoing {@link EntryPoint} (if any).
 * @author Rudolf Biczok
 */
@Entity
public class RecipeStep implements Serializable, QueryableById {
    private static final long serialVersionUID = 1L;

    private long id;
    private PlantOperation operation;
    private Collection<StepInteraction> inputInteractions;
    private Collection<StepInteraction> outputInteractions;
    private Collection<ParameterInteraction> parameterInteractions;

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
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return the plant operation that is supposed to be executed within this interaction step
     */
    @OneToOne(cascade = CascadeType.ALL)
    public PlantOperation getOperation() {
        return operation;
    }

    /**
     * @param operation the plant operation that is supposed to be executed within this interaction step
     */
    public void setOperation(PlantOperation operation) {
        this.operation = operation;
    }

    /**
     * @return the list of incoming interactions
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Collection<StepInteraction> getInputInteractions() {
        return inputInteractions;
    }

    /**
     * @param inputInteractions the list of incoming interactions
     */
    public void setInputInteractions(Collection<StepInteraction> inputInteractions) {
        this.inputInteractions = inputInteractions;
    }

    /**
     * @return the list of outgoing interactions
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Collection<StepInteraction> getOutputInteractions() {
        return outputInteractions;
    }

    /**
     * @param outputInteractions the list of outgoing interaction
     */
    public void setOutputInteractions(Collection<StepInteraction> outputInteractions) {
        this.outputInteractions = outputInteractions;
    }

    /**
     * @return the list of parameter bindings
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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
