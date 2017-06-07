package cocome.cloud.sa.serviceprovider.impl.dao.store;

import cocome.cloud.sa.serviceprovider.impl.dao.DataAccessObject;
import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import de.kit.ipd.java.utils.time.TimeUtils;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.store.OrderEntry;
import org.cocome.tradingsystem.inventory.data.store.ProductOrder;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.remote.access.DatabaseAccess;
import org.cocome.tradingsystem.remote.access.Notification;

import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for {@link ProductOrder}
 * @author Rudolf Biczok
 */
@Dependent
public class ProductOrderDAO implements DataAccessObject<ProductOrder> {

    @EJB
    private DatabaseAccess databaseAccess;

    @Override
    public String getEntityTypeName() {
        return "productorder";
    }

    @Override
    public Notification createEntities(List<ProductOrder> entities) throws IllegalArgumentException {
        return databaseAccess.createProductOrder(entities);
    }

    @Override
    public Notification updateEntities(List<ProductOrder> entities) throws IllegalArgumentException {
        return databaseAccess.updateProductOrder(entities);
    }

    @Override
    public Table<String> toTable(List<ProductOrder> list) {
        final Table<String> table = new Table<>();
        table.addHeader("ProductOrderId", "EnterpriseName",
                "StoreName", "StoreLocation", "ProductBarcode",
                "OrderDeliveryDate", "OrderOrderingDate",
                "OrderAmount");
        int row = 0;
        for (final ProductOrder nextOrder : list) {
            for (final OrderEntry nextOrderEntry : nextOrder.getOrderEntries()) {
                table.addColumn(row, 0, String.valueOf(nextOrder.getId()), true);
                table.addColumn(row, 1, nextOrder.getStore().getEnterprise().getName(), true);
                table.addColumn(row, 2, nextOrder.getStore().getName(), true);
                table.addColumn(row, 3, nextOrder.getStore().getLocation(), true);
                table.addColumn(row, 4, String.valueOf(nextOrderEntry.getProduct().getBarcode()), true);
                table.addColumn(row, 5, TimeUtils.convertToStringDate(nextOrder.getDeliveryDate()), true);
                table.addColumn(row, 6, TimeUtils.convertToStringDate(nextOrder.getOrderingDate()), true);
                table.addColumn(row, 7, String.valueOf(nextOrderEntry.getAmount()), true);
                row++;
            }
        }
        return table;
    }

    @Override
    public List<ProductOrder> fromTable(Table<String> table) {
        final Map<String, ProductOrder> map = new HashMap<>();

        final int len = table.size();
        Column<String> colProductOrderId;
        Column<String> colStoreId;
        Column<String> colProductBarcode;
        Column<String> colOrderDeliveryDate;
        Column<String> colOrderOrderingDate;
        Column<String> colOrderAmount;

        for (int i = 0; i < len; i++) {
            colProductOrderId = table.getColumnByName(i, "ProductOrderId");
            colStoreId = table.getColumnByName(i, "StoreId");
            colProductBarcode = table.getColumnByName(i, "ProductBarcode");
            colOrderDeliveryDate = table.getColumnByName(i, "OrderDeliveryDate");
            colOrderOrderingDate = table.getColumnByName(i, "OrderOrderingDate");
            colOrderAmount = table.getColumnByName(i, "OrderAmount");

            ProductOrder productOrder = map.get(colProductOrderId.getValue());
            if (productOrder == null) {
                productOrder = new ProductOrder();
                productOrder.setId(Long.parseLong(colProductOrderId.getValue()));
                productOrder.setOrderEntries(new ArrayList<>());

                final Store store = new Store();
                store.setId(Long.parseLong(colStoreId.getValue()));
                productOrder.setStore(store);

                productOrder.setDeliveryDate(TimeUtils.convertToDateObject(
                        colOrderDeliveryDate.getValue()));
                productOrder.setOrderingDate(TimeUtils.convertToDateObject(
                        colOrderOrderingDate.getValue()));

                map.put(colProductOrderId.getValue(), productOrder);
            }

            final Product product = new Product();
            product.setBarcode(Long.parseLong(colProductBarcode.getValue()));

            final OrderEntry orderentry = new OrderEntry();
            orderentry.setOrder(productOrder);

            orderentry.setAmount(Long.parseLong(colOrderAmount.getValue()));
            orderentry.setProduct(product);
            productOrder.getOrderEntries().add(orderentry);
        }

        final List<ProductOrder> list = new ArrayList<>(map.values());
        System.out.println("Product Order List:" + list.size());
        return list;
    }
}
