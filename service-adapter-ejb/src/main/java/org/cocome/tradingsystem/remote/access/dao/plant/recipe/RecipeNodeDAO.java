package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.recipe.InteractionEntity;
import org.cocome.tradingsystem.inventory.data.plant.recipe.Recipe;
import org.cocome.tradingsystem.inventory.data.plant.recipe.RecipeNode;
import org.cocome.tradingsystem.inventory.data.plant.recipe.RecipeOperation;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract DAO for {@link InteractionEntity}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class RecipeNodeDAO extends AbstractDAO<RecipeNode> {

    private static final String ID_COL = RecipeNode.class.getSimpleName() + "Id";
    private static final String RECIPE_COL = Recipe.class.getSimpleName() + "Id";
    private static final String OPERATION_COL = RecipeOperation.class.getSimpleName() + "Id";

    @Override
    public Class<RecipeNode> getEntityType() {
        return RecipeNode.class;
    }

    @Override
    public Table<String> toTable(final List<RecipeNode> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, RECIPE_COL, OPERATION_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, String.valueOf(list.get(i).getRecipe().getId()));
            table.set(i, 2, String.valueOf(list.get(i).getRecipe().getId()));
        }
        return table;
    }

    @Override
    public List<RecipeNode> fromTable(final EntityManager em,
                                      final Table<String> table,
                                      final Notification notification,
                                      final String sourceOperation) {
        final int len = table.size();
        final List<RecipeNode> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colParent = table.getColumnByName(i, RECIPE_COL);
            final Column<String> colChild = table.getColumnByName(i, OPERATION_COL);

            final RecipeNode node = getOrCreateReferencedEntity(RecipeNode.class, colId, em);
            try {
                node.setRecipe(getReferencedEntity(
                        Recipe.class,
                        colParent,
                        em));
                node.setOperation(getReferencedEntity(
                        RecipeOperation.class,
                        colChild,
                        em));
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }
            list.add(node);
        }
        return list;
    }

}


