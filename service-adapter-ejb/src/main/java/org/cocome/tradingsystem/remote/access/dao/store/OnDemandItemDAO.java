package org.cocome.tradingsystem.remote.access.dao.store;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.store.OnDemandItem;
import org.cocome.tradingsystem.inventory.data.store.StockItem;
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
 * DAO for {@link OnDemandItem}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class OnDemandItemDAO extends AbstractDAO<StockItem> {

    private static final String ID_COL = OnDemandItem.class.getSimpleName() + "Id";
    private static final String STORE_ID_COL = Store.class.getSimpleName() + "Id";
    private static final String PRODUCT_BARCODE_COL = Product.class.getSimpleName() + "Barcode";
    private static final String SALES_PRICE_COL = OnDemandItem.class.getSimpleName() + "SalesPrice";

    @Override
    public Class<StockItem> getEntityType() {
        return StockItem.class;
    }

    @Override
    public Table<String> toTable(final List<StockItem> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, STORE_ID_COL, PRODUCT_BARCODE_COL, SALES_PRICE_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, String.valueOf(list.get(i).getStore().getId()));
            table.set(i, 2, String.valueOf(list.get(i).getProduct().getBarcode()));
            table.set(i, 7, String.valueOf(list.get(i).getSalesPrice()));
        }
        return table;
    }

    @Override
    public List<StockItem> fromTable(final EntityManager em,
                                     final Table<String> table,
                                     final Notification notification,
                                     final String sourceOperation) {
        final int len = table.size();
        final List<StockItem> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colStoreId = table.getColumnByName(i, STORE_ID_COL);
            final Column<String> colProductBarcode = table.getColumnByName(i, PRODUCT_BARCODE_COL);
            final Column<String> colSalesPrice = table.getColumnByName(i, SALES_PRICE_COL);

            final StockItem recipe = getOrCreateReferencedEntity(StockItem.class, colId, em);
            try {
                recipe.setStore(getReferencedEntity(
                        Store.class,
                        colStoreId,
                        em));
                recipe.setProduct(queryProductByBarcode(em,
                        Long.valueOf(colProductBarcode.getValue())));
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s: not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            recipe.setSalesPrice(Double.valueOf(colSalesPrice.getValue()));

            list.add(recipe);
        }
        return list;
    }

    public Product queryProductByBarcode(final EntityManager em, final long barcode) {
        return querySingleInstance(em.createQuery(
                "SELECT p FROM Product p WHERE p.barcode = :barcode",
                Product.class).setParameter("barcode", barcode));
    }
}
