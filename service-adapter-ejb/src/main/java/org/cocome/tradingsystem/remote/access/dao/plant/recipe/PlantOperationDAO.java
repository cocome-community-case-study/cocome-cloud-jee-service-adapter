package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * DAO for {@link PlantOperation}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class PlantOperationDAO extends AbstractNameableEntityDAO<PlantOperation> {

    @Override
    public Class<PlantOperation> getEntityType() {
        return PlantOperation.class;
    }

}
