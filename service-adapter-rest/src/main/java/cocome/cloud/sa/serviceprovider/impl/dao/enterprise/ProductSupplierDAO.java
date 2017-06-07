package cocome.cloud.sa.serviceprovider.impl.dao.enterprise;

import cocome.cloud.sa.serviceprovider.impl.dao.DataAccessObject;
import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.ProductSupplier;
import org.cocome.tradingsystem.remote.access.DatabaseAccess;
import org.cocome.tradingsystem.remote.access.Notification;

import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link ProductSupplier}
 * @author Rudolf Biczok
 */
@Dependent
public class ProductSupplierDAO implements DataAccessObject<ProductSupplier> {

    @EJB
    private DatabaseAccess databaseAccess;

    @Override
    public String getEntityTypeName() {
        return "productsupplier";
    }

    @Override
    public Notification createEntities(List<ProductSupplier> entities) throws IllegalArgumentException {
        return databaseAccess.createProductSupplier(entities);
    }

    @Override
    public Notification updateEntities(List<ProductSupplier> entities) throws IllegalArgumentException {
        return databaseAccess.updateProductSupplier(entities);
    }

    @Override
    public Table<String> toTable(List<ProductSupplier> list) {
        final Table<String> table = new Table<>();
        table.addHeader("ProductSupplierId", "ProductSupplierName");
        int row = 0;
        for (final ProductSupplier nextProductSupplier : list) {
            table.addColumn(row, 0, String.valueOf(nextProductSupplier.getId()), true);
            table.addColumn(row, 1, nextProductSupplier.getName(), true);
            row++;
        }
        return table;
    }

    @Override
    public List<ProductSupplier> fromTable(Table<String> table) {
        final List<ProductSupplier> list = new ArrayList<>();
        final int len = table.size();
        Column<String> colName;
        Column<String> colId;
        for (int i = 0; i < len; i++) {
            colName = table.getColumnByName(i, "ProductSupplierName");
            colId = table.getColumnByName(i, "ProductSupplierId");
            if (colName == null) {
                // TODO message error!
                System.out.println("Name column was null for row=" + i);
                continue;
            }

            final ProductSupplier p = new ProductSupplier();
            if (colId != null) {
                p.setId(Long.parseLong(colId.getValue()));
            }
            p.setName(colName.getValue());
            list.add(p);
        }
        return list;
    }
}
