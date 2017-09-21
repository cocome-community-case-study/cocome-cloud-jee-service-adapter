package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.NameableEntity;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.expression.Expression;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Represents an operation provided by an plant
 *
 * @author Rudolf Biczok
 */
@Entity
public class PlantOperation implements Serializable, NameableEntity {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private Plant plant;

    private List<Expression> expressions;

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


    /**
     * @return the plant that owns this production unit
     */
    @NotNull
    @ManyToOne
    public Plant getPlant() {
        return plant;
    }

    /**
     * @param plant the plant that owns this production unit
     */
    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    /**
     * @return the expressions executed within this operation
     */
    //Make sure the execution order of the specified operations is maintained by the DB
    @OrderColumn(name = "EXEC_ORDER")
    //Needed to allow multiple uses of the same operation in the same const expression
    @JoinTable(
            name = "PLANTOPERATION_EXPRESSION",
            joinColumns = {
                    @JoinColumn(name = "EXEC_ORDER"),
                    @JoinColumn(name = "EXPRESSION_ID"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "PLANTOPERATION_ID"),
            })
    @OneToMany(cascade = CascadeType.PERSIST)
    public List<Expression> getExpressions() {
        return expressions;
    }

    /**
     * @param expressions the expressions executed within this operation
     */
    public void setExpressions(List<Expression> expressions) {
        this.expressions = expressions;
    }

    /**
     * @return all material classes that are required for operation execution
     */
    @JoinTable(
            name = "PLANTOPERATION_INPUTENTRYPOINT",
            joinColumns = {
                    @JoinColumn(name = "ENTRYPOINT_ID"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "PLANTOPERATION_ID"),
            })
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
    @JoinTable(
            name = "PLANTOPERATION_OUTPUTENTRYPOINT",
            joinColumns = {
                    @JoinColumn(name = "ENTRYPOINT_ID"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "PLANTOPERATION_ID"),
            })
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
}
