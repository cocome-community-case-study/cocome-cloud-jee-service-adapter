package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperationOrder;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperationOrderEntry;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link PlantOperationOrderEntry}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class PlantOperationOrderEntryDAO extends AbstractDAO<PlantOperationOrderEntry> {

    private static final String ID_COL = PlantOperationOrderEntry.class.getSimpleName() + "Id";
    private static final String AMOUNT_COL = PlantOperationOrderEntry.class.getSimpleName() + "Amount";
    private static final String PLANT_OPERATION_ID_COL = PlantOperation.class.getSimpleName() + "Id";
    private static final String PLANT_OPERATION_ORDER_ID_COL = PlantOperationOrder.class.getSimpleName() + "Id";

    @Override
    public Class<PlantOperationOrderEntry> getEntityType() {
        return PlantOperationOrderEntry.class;
    }

    @Override
    public Table<String> toTable(final List<PlantOperationOrderEntry> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, AMOUNT_COL, PLANT_OPERATION_ID_COL, PLANT_OPERATION_ORDER_ID_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, String.valueOf(list.get(i).getAmount()));
            table.set(i, 2, String.valueOf(list.get(i).getOperation().getId()));
            table.set(i, 3, String.valueOf(list.get(i).getOrder().getId()));
        }
        return table;
    }

    @Override
    public List<PlantOperationOrderEntry> fromTable(final EntityManager em,
                                                    final Table<String> table,
                                                    final Notification notification,
                                                    final String sourceOperation) {
        final int len = table.size();
        final List<PlantOperationOrderEntry> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colAmount = table.getColumnByName(i, AMOUNT_COL);
            final Column<String> colPlantOperation = table.getColumnByName(i, PLANT_OPERATION_ID_COL);
            final Column<String> colPlantOperationOrder = table.getColumnByName(i, PLANT_OPERATION_ORDER_ID_COL);

            final PlantOperationOrderEntry orderEntry = getOrCreateReferencedEntity(PlantOperationOrderEntry.class, colId, em);
            orderEntry.setAmount(Long.valueOf(colAmount.getValue()));
            try {
                orderEntry.setOperation(getOrCreateReferencedEntity(PlantOperation.class,
                        colPlantOperation, em));
                orderEntry.setOrder(getOrCreateReferencedEntity(PlantOperationOrder.class,
                        colPlantOperationOrder, em));
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            list.add(orderEntry);
        }
        return list;
    }

}
