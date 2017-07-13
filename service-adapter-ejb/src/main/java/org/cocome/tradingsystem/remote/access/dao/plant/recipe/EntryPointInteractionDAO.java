package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import org.cocome.tradingsystem.inventory.data.plant.recipe.EntryPoint;
import org.cocome.tradingsystem.inventory.data.plant.recipe.EntryPointInteraction;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * DAO for {@link EntryPointInteraction}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class EntryPointInteractionDAO
        extends AbstractInteractionEntityDAO<EntryPoint, EntryPoint, EntryPointInteraction> {

    @Override
    public Class<EntryPointInteraction> getEntityType() {
        return EntryPointInteraction.class;
    }

}
