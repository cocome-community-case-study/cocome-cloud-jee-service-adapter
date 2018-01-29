package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.recipe.ProductionOrder;
import org.cocome.tradingsystem.inventory.data.plant.recipe.ProductionOrderEntry;
import org.cocome.tradingsystem.inventory.data.plant.recipe.Recipe;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link ProductionOrderEntry}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ProductionOrderEntryDAO extends AbstractDAO<ProductionOrderEntry> {

    private static final String ID_COL = ProductionOrderEntry.class.getSimpleName() + "Id";
    private static final String AMOUNT_COL = ProductionOrderEntry.class.getSimpleName() + "Amount";
    private static final String FINISHED_COL = ProductionOrderEntry.class.getSimpleName() + "Finished";
    private static final String PLANT_RECIPE_ID_COL = Recipe.class.getSimpleName() + "Id";
    private static final String PLANT_PRODUCTION_ORDER_ID_COL = ProductionOrder.class.getSimpleName() + "Id";

    @Override
    public Class<ProductionOrderEntry> getEntityType() {
        return ProductionOrderEntry.class;
    }

    @Override
    public Table<String> toTable(final List<ProductionOrderEntry> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, AMOUNT_COL, FINISHED_COL, PLANT_RECIPE_ID_COL, PLANT_PRODUCTION_ORDER_ID_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, String.valueOf(list.get(i).getAmount()));
            table.set(i, 2, String.valueOf(list.get(i).isFinished()));
            table.set(i, 3, String.valueOf(list.get(i).getRecipe().getId()));
            table.set(i, 4, String.valueOf(list.get(i).getOrder().getId()));
        }
        return table;
    }

    @Override
    public List<ProductionOrderEntry> fromTable(final EntityManager em,
                                                final Table<String> table,
                                                final Notification notification,
                                                final String sourceOperation) {
        final int len = table.size();
        final List<ProductionOrderEntry> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colAmount = table.getColumnByName(i, AMOUNT_COL);
            final Column<String> colFinished = table.getColumnByName(i, FINISHED_COL);
            final Column<String> colRecipe = table.getColumnByName(i, PLANT_RECIPE_ID_COL);
            final Column<String> colProductionOrder = table.getColumnByName(i, PLANT_PRODUCTION_ORDER_ID_COL);

            final ProductionOrderEntry orderEntry = getOrCreateReferencedEntity(ProductionOrderEntry.class, colId, em);
            orderEntry.setAmount(Long.valueOf(colAmount.getValue()));
            orderEntry.setFinished(Boolean.valueOf(colFinished.getValue()));

            try {
                orderEntry.setRecipe(getOrCreateReferencedEntity(Recipe.class,
                        colRecipe, em));
                orderEntry.setOrder(getOrCreateReferencedEntity(ProductionOrder.class,
                        colProductionOrder, em));
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s: not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            list.add(orderEntry);
        }
        return list;
    }

}
