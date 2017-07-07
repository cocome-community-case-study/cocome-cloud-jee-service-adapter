package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;
import org.cocome.tradingsystem.inventory.data.plant.parameter.ProductionParameter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Used to connect parameters from {@link CustomProduct} and {@link PlantOperation}.
 * Other subsystems are supposed to copy the customer's parameter values to the plant
 * operation based on this mapping
 *
 * @author Rudolf Biczok
 */
@Entity
public class ParameterInteraction implements Serializable, QueryableById {
    private static final long serialVersionUID = 1L;

    private long id;
    private ProductionParameter<CustomProduct> customProductParameter;
    private ProductionParameter<PlantOperation> plantOperationParameter;

    /**
     * @return The database id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    /**
     * @param id The database id
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return the customer parameter from where the operation can extract the customer value from
     */
    @OneToOne(fetch = FetchType.EAGER)
    public ProductionParameter<CustomProduct> getCustomProductParameter() {
        return customProductParameter;
    }

    /**
     * @param customProductParameter the customer parameter from where the operation can extract
     *                               the customer value from
     */
    public void setCustomProductParameter(ProductionParameter<CustomProduct> customProductParameter) {
        this.customProductParameter = customProductParameter;
    }

    /**
     * @return the operation parameter which is supposed to receive the customer value from the
     * corresponding customer parameter
     */
    @OneToOne(fetch = FetchType.EAGER)
    public ProductionParameter<PlantOperation> getPlantOperationParameter() {
        return plantOperationParameter;
    }

    public void setPlantOperationParameter(ProductionParameter<PlantOperation> plantOperationParameter) {
        this.plantOperationParameter = plantOperationParameter;
    }
}
