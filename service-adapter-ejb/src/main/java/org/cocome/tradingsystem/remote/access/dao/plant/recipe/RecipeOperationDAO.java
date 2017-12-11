package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import org.cocome.tradingsystem.inventory.data.plant.parameter.Parameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.RecipeOperation;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;
import org.cocome.tradingsystem.remote.access.dao.AbstractInheritanceTreeDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

/**
 * DAO for {@link Parameter}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class RecipeOperationDAO extends AbstractInheritanceTreeDAO<RecipeOperation> {

    @Inject
    private PlantOperationDAO plantOperationDAO;

    @Inject
    private RecipeDAO recipeDAO;

    @Override
    protected List<AbstractDAO<? extends RecipeOperation>> getSubClasseDAOs() {
        return Arrays.asList(plantOperationDAO,
                recipeDAO);
    }

    @Override
    protected Class<RecipeOperation> getEntityType() {
        return RecipeOperation.class;
    }
}
