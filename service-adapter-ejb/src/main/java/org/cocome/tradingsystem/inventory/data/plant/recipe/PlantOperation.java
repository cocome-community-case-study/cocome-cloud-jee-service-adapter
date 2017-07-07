package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;
import org.cocome.tradingsystem.inventory.data.plant.parameter.ProductionParameter;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.expression.Expression;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Represents an operation provided by an plant
 * @author Rudolf Biczok
 */
@Entity
public class PlantOperation implements Serializable, QueryableById {
    private static final long serialVersionUID = 1L;

    private long id;
    private Collection<ProductionParameter<PlantOperation>> parameters;
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
    public void setId(final long id) {
        this.id = id;
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
     * @return all available operations of this production unit type
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Collection<ProductionParameter<PlantOperation>> getProductionParameters() {
        return parameters;
    }

    /**
     * @param parameters all available operations of this production unit type
     */
    public void setProductionParameters(Collection<ProductionParameter<PlantOperation>> parameters) {
        this.parameters = parameters;
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

}
