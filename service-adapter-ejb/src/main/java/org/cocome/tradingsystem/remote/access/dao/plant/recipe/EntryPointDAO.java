package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import org.cocome.tradingsystem.inventory.data.plant.recipe.EntryPoint;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * DAO for {@link EntryPoint}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class EntryPointDAO extends AbstractNameableEntityDAO<EntryPoint> {

    @Override
    public Class<EntryPoint> getEntityType() {
        return EntryPoint.class;
    }

}
