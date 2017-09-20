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
public class ParameterInteraction implements InteractionEntity<
        CustomProductParameter,
        PlantOperationParameter> {
    private static final long serialVersionUID = 1L;

    private long id;

    private CustomProductParameter from;
    private PlantOperationParameter to;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

    @Override
    @OneToOne(fetch = FetchType.EAGER)
    public CustomProductParameter getFrom() {
        return from;
    }

    @Override
    public void setFrom(CustomProductParameter from) {
        this.from = from;
    }

    @Override
    @OneToOne(fetch = FetchType.EAGER)
    public PlantOperationParameter getTo() {
        return to;
    }

    @Override
    public void setTo(PlantOperationParameter to) {
        this.to = to;
    }

    @Transient
    @Override
    public Class<CustomProductParameter> getFromClass() {
        return CustomProductParameter.class;
    }

    @Transient
    @Override
    public Class<PlantOperationParameter> getToClass() {
        return PlantOperationParameter.class;
    }
}
