package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.CustomProductParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.PlantOperationParameter;

import javax.persistence.*;

/**
 * Used to connect parameters from {@link CustomProduct} and {@link PlantOperation}.
 * Other subsystems are supposed to copy the customer's parameter values to the plant
 * operation based on this mapping
 *
 * @author Rudolf Biczok
 */
@Entity
public class ParameterInteraction extends InteractionEntity<
        CustomProductParameter,
        PlantOperationParameter> {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    @Transient
    @Override
    public Class<CustomProductParameter> getFromClass() {
        return CustomProductParameter.class;
    }

    @SuppressWarnings("unchecked")
    @Transient
    @Override
    public Class<PlantOperationParameter> getToClass() {
        return PlantOperationParameter.class;
    }
}
