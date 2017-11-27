package org.cocome.tradingsystem.remote.access.dao.store;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
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
 * DAO for {@link StockItem}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class StockItemDAO extends AbstractDAO<StockItem> {

    private static final String ID_COL = StockItem.class.getSimpleName() + "Id";
    private static final String STORE_ID_COL = Store.class.getSimpleName() + "Id";
    private static final String PRODUCT_BARCODE_COL = Product.class.getSimpleName() + "Barcode";
    private static final String MIN_STOCK_COL = StockItem.class.getSimpleName() + "MinStock";
    private static final String MAX_STOCK_COL = StockItem.class.getSimpleName() + "MaxStock";
    private static final String INCOMING_AMOUNT_COL = StockItem.class.getSimpleName() + "IncomingAmount";
    private static final String AMOUNT_COL = StockItem.class.getSimpleName() + "Amount";
    private static final String SALES_PRICE_COL = StockItem.class.getSimpleName() + "SalesPrice";

    @Override
    public Class<StockItem> getEntityType() {
        return StockItem.class;
    }

    @Override
    public Table<String> toTable(final List<StockItem> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, STORE_ID_COL, PRODUCT_BARCODE_COL, MIN_STOCK_COL,
                MAX_STOCK_COL, INCOMING_AMOUNT_COL, AMOUNT_COL, SALES_PRICE_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, String.valueOf(list.get(i).getStore().getId()));
            table.set(i, 2, String.valueOf(list.get(i).getProduct().getBarcode()));
            table.set(i, 3, String.valueOf(list.get(i).getMinStock()));
            table.set(i, 4, String.valueOf(list.get(i).getMaxStock()));
            table.set(i, 5, String.valueOf(list.get(i).getIncomingAmount()));
            table.set(i, 6, String.valueOf(list.get(i).getAmount()));
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
            final Column<String> colMinStock = table.getColumnByName(i, MIN_STOCK_COL);
            final Column<String> colMaxStock = table.getColumnByName(i, MAX_STOCK_COL);
            final Column<String> colIncomingAmount = table.getColumnByName(i, INCOMING_AMOUNT_COL);
            final Column<String> colAmount = table.getColumnByName(i, AMOUNT_COL);
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

            recipe.setMinStock(Long.valueOf(colMinStock.getValue()));
            recipe.setMaxStock(Long.valueOf(colMaxStock.getValue()));
            recipe.setIncomingAmount(Long.valueOf(colIncomingAmount.getValue()));
            recipe.setAmount(Long.valueOf(colAmount.getValue()));
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
