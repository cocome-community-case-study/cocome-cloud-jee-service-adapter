package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.plant.parameter.ProductionParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.ParameterInteraction;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;

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
            ProductionParameter<CustomProduct>,
            ProductionParameter<PlantOperation>,
            ParameterInteraction> {

    @Override
    public Class<ParameterInteraction> getEntityType() {
        return ParameterInteraction.class;
    }

}
