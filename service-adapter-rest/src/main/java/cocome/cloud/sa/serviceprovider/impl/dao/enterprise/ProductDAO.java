package cocome.cloud.sa.serviceprovider.impl.dao.enterprise;

import cocome.cloud.sa.serviceprovider.impl.dao.DataAccessObject;
import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.remote.access.DatabaseAccess;
import org.cocome.tradingsystem.remote.access.Notification;

import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link Product}
 * @author Rudolf Biczok
 */
@Dependent
public class ProductDAO implements DataAccessObject<Product> {

    @EJB
    private DatabaseAccess databaseAccess;

    @Override
    public String getEntityTypeName() {
        return "product";
    }

    @Override
    public Notification createEntities(List<Product> entities) throws IllegalArgumentException {
        return databaseAccess.createProducts(entities);
    }

    @Override
    public Notification updateEntities(List<Product> entities) throws IllegalArgumentException {
        return databaseAccess.updateProducts(entities);
    }

    @Override
    public Table<String> toTable(List<Product> list) {
        final Table<String> table = new Table<>();
        table.addHeader("ProductBarcode", "ProductName", "ProductPurchasePrice");
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.addColumn(i, 0, String.valueOf(list.get(i).getBarcode()), true);
            table.addColumn(i, 1, list.get(i).getName(), true);
            table.addColumn(i, 2, String.valueOf(list.get(i).getPurchasePrice()), true);
            // TODO supplier should also be added!
        }
        return table;
    }

    @Override
    public List<Product> fromTable(Table<String> table) {
        final List<Product> list = new ArrayList<>();
        final int len = table.size();
        Column<String> colBarcode;
        Column<String> colName;
        Column<String> colPurchasePrice;
        for (int i = 0; i < len; i++) {
            colBarcode = table.getColumnByName(i, "ProductBarcode");
            colName = table.getColumnByName(i, "ProductName");
            colPurchasePrice = table.getColumnByName(i, "ProductPurchasePrice");

            if (colBarcode == null) {
                System.out.println("Barcode column was null for row=" + i);
                continue;
            }
            final Product p = new Product();
            p.setBarcode(Long.parseLong(colBarcode.getValue()));
            if (colName != null) {
                p.setName(colName.getValue());
            }
            if (colPurchasePrice != null) {
                p.setPurchasePrice(Double.parseDouble(colPurchasePrice.getValue()));
            }
            list.add(p);
        }
        return list;
    }
}
