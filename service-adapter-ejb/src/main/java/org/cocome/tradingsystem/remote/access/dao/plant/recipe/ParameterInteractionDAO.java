package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.parameter.CustomProductParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.PlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.ParameterInteraction;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * DAO for {@link ParameterInteraction}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ParameterInteractionDAO
        extends AbstractInteractionEntityDAO<
            CustomProductParameter,
            PlantOperationParameter,
            ParameterInteraction> {

    @Override
    public Class<ParameterInteraction> getEntityType() {
        return ParameterInteraction.class;
    }

}
