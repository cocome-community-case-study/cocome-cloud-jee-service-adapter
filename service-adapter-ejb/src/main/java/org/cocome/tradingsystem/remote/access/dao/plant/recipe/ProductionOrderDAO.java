package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import de.kit.ipd.java.utils.time.TimeUtils;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperationOrder;
import org.cocome.tradingsystem.inventory.data.plant.recipe.ProductionOrder;
import org.cocome.tradingsystem.inventory.data.store.Store;
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
public class ProductionOrderDAO extends AbstractDAO<ProductionOrder> {

    private static final String ID_COL = ProductionOrder.class.getSimpleName() + "Id";
    private static final String DELIVERY_DATE_COL = ProductionOrder.class.getSimpleName() + "DeliveryDate";
    private static final String ORDERING_DATE_COL = ProductionOrder.class.getSimpleName() + "OrderingDate";
    private static final String FINISHED_COL = ProductionOrder.class.getSimpleName() + "Finished";
    private static final String ENTERPRISE_COL = TradingEnterprise.class.getSimpleName() + "Id";
    private static final String STORE_COL = Store.class.getSimpleName() + "Id";

    @Override
    public Class<ProductionOrder> getEntityType() {
        return ProductionOrder.class;
    }

    @Override
    public Table<String> toTable(final List<ProductionOrder> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, DELIVERY_DATE_COL, ORDERING_DATE_COL, FINISHED_COL, ENTERPRISE_COL, STORE_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, TimeUtils.convertToStringDate(list.get(i).getDeliveryDate()));
            table.set(i, 2, TimeUtils.convertToStringDate(list.get(i).getOrderingDate()));
            table.set(i, 3, String.valueOf(list.get(i).isFinished()));
            table.set(i, 4, String.valueOf(list.get(i).getEnterprise().getId()));
            table.set(i, 5, String.valueOf(list.get(i).getStore().getId()));
        }
        return table;
    }

    @Override
    public List<ProductionOrder> fromTable(final EntityManager em,
                                           final Table<String> table,
                                           final Notification notification,
                                           final String sourceOperation) {
        final int len = table.size();
        final List<ProductionOrder> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colDeliveryDate = table.getColumnByName(i, DELIVERY_DATE_COL);
            final Column<String> colOrderingDate = table.getColumnByName(i, ORDERING_DATE_COL);
            final Column<String> colFinished = table.getColumnByName(i, FINISHED_COL);
            final Column<String> colEnterpriseId = table.getColumnByName(i, ENTERPRISE_COL);
            final Column<String> colStoreId = table.getColumnByName(i, STORE_COL);

            final ProductionOrder productionOrder = getOrCreateReferencedEntity(ProductionOrder.class, colId, em);
            productionOrder.setDeliveryDate(TimeUtils.convertToDateObject(colDeliveryDate.getValue()));
            productionOrder.setOrderingDate(TimeUtils.convertToDateObject(colOrderingDate.getValue()));
            productionOrder.setFinished(Boolean.valueOf(colFinished.getValue()));

            try {
                productionOrder.setEnterprise(getOrCreateReferencedEntity(TradingEnterprise.class,
                        colEnterpriseId, em));
                productionOrder.setStore(getOrCreateReferencedEntity(Store.class,
                        colStoreId, em));
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            list.add(productionOrder);
        }
        return list;
    }
}
