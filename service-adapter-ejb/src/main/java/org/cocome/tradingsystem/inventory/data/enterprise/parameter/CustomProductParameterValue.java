package org.cocome.tradingsystem.inventory.data.enterprise.parameter;

import org.cocome.tradingsystem.inventory.data.enterprise.parameter.CustomProductParameter;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.IParameterValue;
import org.cocome.tradingsystem.inventory.data.plant.recipe.ProductionOrderEntry;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Holds a value for a particular {@link CustomProductParameter}
 *
 * @author Rudolf Biczok
 */
@Entity
public class CustomProductParameterValue implements IParameterValue<CustomProductParameter> {

    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String value;
    private CustomProductParameter parameter;
    private ProductionOrderEntry orderEntry;

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
     * @return the custom product parameter
     */
    @Override
    @NotNull
    @ManyToOne
    public CustomProductParameter getParameter() {
        return parameter;
    }

    /**
     * @param parameter the custom product parameter
     */
    @Override
    public void setParameter(CustomProductParameter parameter) {
        this.parameter = parameter;
    }

    /**
     * @return the order entry this parameter setting belongs to
     */
    @NotNull
    @ManyToOne
    public ProductionOrderEntry getOrderEntry() {
        return orderEntry;
    }

    /**
     * @param orderEntry the order entry this parameter setting belongs to
     */
    public void setOrderEntry(ProductionOrderEntry orderEntry) {
        this.orderEntry = orderEntry;
    }
}
