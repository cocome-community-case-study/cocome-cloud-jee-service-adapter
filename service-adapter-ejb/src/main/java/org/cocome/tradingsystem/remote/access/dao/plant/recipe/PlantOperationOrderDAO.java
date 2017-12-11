package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import de.kit.ipd.java.utils.time.TimeUtils;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperationOrder;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link PlantOperationOrder}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class PlantOperationOrderDAO extends AbstractDAO<PlantOperationOrder> {

    private static final String ID_COL = PlantOperationOrder.class.getSimpleName() + "Id";
    private static final String DELIVERY_DATE_COL = PlantOperationOrder.class.getSimpleName() + "DeliveryDate";
    private static final String ORDERING_DATE_COL = PlantOperationOrder.class.getSimpleName() + "OrderingDate";
    private static final String ENTERPRISE_COL = TradingEnterprise.class.getSimpleName() + "Id";
    private static final String PLANT_COL = Plant.class.getSimpleName() + "Id";

    @Override
    public Class<PlantOperationOrder> getEntityType() {
        return PlantOperationOrder.class;
    }

    @Override
    public Table<String> toTable(final List<PlantOperationOrder> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, DELIVERY_DATE_COL, ORDERING_DATE_COL, ENTERPRISE_COL, PLANT_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, TimeUtils.convertToStringDate(list.get(i).getDeliveryDate()));
            table.set(i, 2, TimeUtils.convertToStringDate(list.get(i).getOrderingDate()));
            table.set(i, 3, String.valueOf(list.get(i).getEnterprise().getId()));
            table.set(i, 4, String.valueOf(list.get(i).getPlant().getId()));
        }
        return table;
    }

    @Override
    public List<PlantOperationOrder> fromTable(final EntityManager em,
                                               final Table<String> table,
                                               final Notification notification,
                                               final String sourceOperation) {
        final int len = table.size();
        final List<PlantOperationOrder> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colDeliveryDate = table.getColumnByName(i, DELIVERY_DATE_COL);
            final Column<String> colOrderingDate = table.getColumnByName(i, ORDERING_DATE_COL);
            final Column<String> colEnterpriseId = table.getColumnByName(i, ENTERPRISE_COL);
            final Column<String> colPlantId = table.getColumnByName(i, PLANT_COL);

            final PlantOperationOrder plantOperationOrder = getOrCreateReferencedEntity(PlantOperationOrder.class, colId, em);
            plantOperationOrder.setDeliveryDate(TimeUtils.convertToDateObject(colDeliveryDate.getValue()));
            plantOperationOrder.setOrderingDate(TimeUtils.convertToDateObject(colOrderingDate.getValue()));

            try {
                plantOperationOrder.setEnterprise(getOrCreateReferencedEntity(TradingEnterprise.class,
                        colEnterpriseId, em));
                plantOperationOrder.setPlant(getOrCreateReferencedEntity(Plant.class,
                        colPlantId, em));
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            list.add(plantOperationOrder);
        }
        return list;
    }
}
