package org.cocome.tradingsystem.inventory.data.plant.productionunit;

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Represents an atomic operation on a production unit
 * @author Rudolf Biczok
 */
@Entity
public class ProductionUnitOperation implements Serializable, QueryableById {

    private static final long serialVersionUID = 1L;

    private long id;

    private String operationId;

    /**
     * Gets identifier value
     *
     * @return The identifier value.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    private ProductionUnitClass productionUnitClass;


    /**
     * Sets identifier.
     *
     * @param id
     *            Identifier value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The operation id unique to the production plant
     */
    @NotNull
    @Basic
    public String getOperationId() {
        return operationId;
    }

    /**
     * @param operationId
     *            The operation id unique to the production plant
     */
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return the associated {@link ProductionUnitClass}
     */
    @NotNull
    @ManyToOne
    public ProductionUnitClass getProductionUnitClass() {
        return productionUnitClass;
    }

    /**
     * @param productionUnitClass the associated production unit class
     */
    public void setProductionUnitClass(ProductionUnitClass productionUnitClass) {
        this.productionUnitClass = productionUnitClass;
    }
}
