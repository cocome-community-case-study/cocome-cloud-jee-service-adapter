package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;
import org.cocome.tradingsystem.inventory.data.plant.parameter.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Holds a value for a particular {@link Parameter}
 *
 * @author Rudolf Biczok
 */
@Entity
public class ParameterValue implements QueryableById, Serializable {

    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String value;
    private Parameter parameter;
    private RecipeOperationOrderEntry orderEntry;

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
    @NotNull
    @Basic
    public String getValue() {
        return value;
    }

    /**
     * @param value The parameter value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the plant operation parameter
     */
    @NotNull
    @ManyToOne
    public Parameter getParameter() {
        return parameter;
    }

    /**
     * @param parameter the plant operation parameter
     */
    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    /**
     * @return the order entry this parameter setting belongs to
     */
    @NotNull
    @ManyToOne
    public RecipeOperationOrderEntry getOrderEntry() {
        return orderEntry;
    }

    /**
     * @param orderEntry the order entry this parameter setting belongs to
     */
    public void setOrderEntry(RecipeOperationOrderEntry orderEntry) {
        this.orderEntry = orderEntry;
    }
}
