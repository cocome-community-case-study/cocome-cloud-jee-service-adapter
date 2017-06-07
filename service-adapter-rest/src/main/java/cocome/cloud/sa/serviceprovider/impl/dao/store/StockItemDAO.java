package cocome.cloud.sa.serviceprovider.impl.dao.store;

import cocome.cloud.sa.serviceprovider.impl.dao.DataAccessObject;
import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.store.StockItem;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.remote.access.DatabaseAccess;
import org.cocome.tradingsystem.remote.access.Notification;

import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link StockItem}
 * @author Rudolf Biczok
 */
@Dependent
public class StockItemDAO implements DataAccessObject<StockItem> {

    @EJB
    private DatabaseAccess databaseAccess;

    @Override
    public String getEntityTypeName() {
        return "stockitem";
    }

    @Override
    public Notification createEntities(List<StockItem> entities) throws IllegalArgumentException {
        return databaseAccess.createStockItem(entities);
    }

    @Override
    public Notification updateEntities(List<StockItem> entities) throws IllegalArgumentException {
        return databaseAccess.updateStockItems(entities);
    }

    @Override
    public Table<String> toTable(List<StockItem> list) {
        final Table<String> table = new Table<>();
        table.addHeader("EnterpriseName",
                "StoreName", "StoreLocation", "ProductBarcode",
                "StockItemId", "StockItemMinStock", "StockItemMaxStock",
                "StockItemIncomingAmount", "StockItemAmount",
                "StockItemSalesPrice");
        int row = 0;
        for (final StockItem nextStockItem : list) {
            table.addColumn(row, 0, nextStockItem.getStore().getEnterprise().getName(), true);
            table.addColumn(row, 1, nextStockItem.getStore().getName(), true);
            table.addColumn(row, 2, nextStockItem.getStore().getLocation(), true);
            table.addColumn(row, 3, String.valueOf(nextStockItem.getProduct().getBarcode()), true);
            table.addColumn(row, 4, String.valueOf(nextStockItem.getId()), true);
            table.addColumn(row, 5, String.valueOf(nextStockItem.getMinStock()), true);
            table.addColumn(row, 6, String.valueOf(nextStockItem.getMaxStock()), true);
            table.addColumn(row, 7, String.valueOf(nextStockItem.getIncomingAmount()), true);
            table.addColumn(row, 8, String.valueOf(nextStockItem.getAmount()), true);
            table.addColumn(row, 9, String.valueOf(nextStockItem.getSalesPrice()), true);
            row++;
        }
        return table;
    }

    @Override
    public List<StockItem> fromTable(Table<String> table) {
        final List<StockItem> list = new ArrayList<>();
        final int len = table.size();
        Column<String> colStoreId;
        Column<String> colProductBarcode;
        Column<String> colStockItemMinStock;
        Column<String> colStockItemMaxStock;
        Column<String> colStockItemIncomingAmount;
        Column<String> colStockItemAmount;
        Column<String> colStockItemSalesPrice;
        for (int i = 0; i < len; i++) {
            colStoreId = table.getColumnByName(i, "StoreId");
            colProductBarcode = table.getColumnByName(i, "ProductBarcode");
            colStockItemMinStock = table.getColumnByName(i, "StockItemMinStock");
            colStockItemMaxStock = table.getColumnByName(i, "StockItemMaxStock");
            colStockItemIncomingAmount = table.getColumnByName(i, "StockItemIncomingAmount");
            colStockItemAmount = table.getColumnByName(i, "StockItemAmount");
            colStockItemSalesPrice = table.getColumnByName(i, "StockItemSalesPrice");

            final Store store = new Store();

            if (colStoreId != null) {
                store.setId(Long.parseLong(colStoreId.getValue()));
            } else {
                store.setId(-1L);
            }

            final Product product = new Product();
            product.setBarcode(Long.parseLong(colProductBarcode.getValue()));

            final StockItem stockItem = new StockItem();
            stockItem.setStore(store);
            stockItem.setProduct(product);
            stockItem.setIncomingAmount(Long.parseLong(colStockItemIncomingAmount.getValue()));
            stockItem.setMaxStock(Long.parseLong(colStockItemMaxStock.getValue()));
            stockItem.setMinStock(Long.parseLong(colStockItemMinStock.getValue()));
            stockItem.setAmount(Long.parseLong(colStockItemAmount.getValue()));
            stockItem.setSalesPrice(Double.parseDouble(colStockItemSalesPrice.getValue()));

            list.add(stockItem);
        }
        return list;
    }
}
