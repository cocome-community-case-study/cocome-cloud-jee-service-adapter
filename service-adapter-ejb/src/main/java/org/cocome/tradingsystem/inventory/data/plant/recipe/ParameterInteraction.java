package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.plant.parameter.ProductionParameter;

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
        ProductionParameter<CustomProduct>,
        ProductionParameter<PlantOperation> > {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    @Transient
    @Override
    public Class<ProductionParameter<CustomProduct>> getFromClass() {
        Class<? extends ProductionParameter> c = ProductionParameter.class;
        return (Class<ProductionParameter<CustomProduct>>)c;
    }

    @SuppressWarnings("unchecked")
    @Transient
    @Override
    public Class<ProductionParameter<PlantOperation>> getToClass() {
        Class<? extends ProductionParameter> c = ProductionParameter.class;
        return (Class<ProductionParameter<PlantOperation>>)c;
    }
}
