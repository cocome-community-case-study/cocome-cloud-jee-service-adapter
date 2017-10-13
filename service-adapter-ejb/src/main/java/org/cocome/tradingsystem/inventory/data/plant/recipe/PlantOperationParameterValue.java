package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.parameter.IParameterValue;
import org.cocome.tradingsystem.inventory.data.plant.parameter.PlantOperationParameter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Holds a value for a particular {@link PlantOperationParameter}
 *
 * @author Rudolf Biczok
 */
@Entity
public class PlantOperationParameterValue implements IParameterValue<PlantOperationParameter> {

    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String value;
    private PlantOperationParameter parameter;
    private PlantOperationOrderEntry orderEntry;
    /**
     * @return The id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    /**
     * @param id Identifier value.
     */
    @Override
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The parameter value
     */
    @Override
    @NotNull
    @Basic
    public String getValue() {
        return value;
    }

    /**
     * @param value The parameter value
     */
    @Override
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the plant operation parameter
     */
    @Override
    @NotNull
    @ManyToOne
    public PlantOperationParameter getParameter() {
        return parameter;
    }

    /**
     * @param parameter the plant operation parameter
     */
    @Override
    public void setParameter(PlantOperationParameter parameter) {
        this.parameter = parameter;
    }

    /**
     * @return the order entry this parameter setting belongs to
     */
    @NotNull
    @ManyToOne
    public PlantOperationOrderEntry getOrderEntry() {
        return orderEntry;
    }

    /**
     * @param orderEntry the order entry this parameter setting belongs to
     */
    public void setOrderEntry(PlantOperationOrderEntry orderEntry) {
        this.orderEntry = orderEntry;
    }
}
