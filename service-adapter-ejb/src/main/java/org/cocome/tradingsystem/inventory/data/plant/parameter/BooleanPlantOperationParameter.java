package org.cocome.tradingsystem.inventory.data.plant.parameter;

import org.cocome.tradingsystem.inventory.data.enterprise.parameter.IBooleanParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;

import javax.persistence.Entity;

/**
 * Abstract class of {@link IBooleanParameter} for {@link PlantOperation}
 *
 * @author Rudolf Biczok
 */
@Entity
public class BooleanPlantOperationParameter extends PlantOperationParameter implements IBooleanParameter {
    private static final long serialVersionUID = -2577328715744776645L;
}
