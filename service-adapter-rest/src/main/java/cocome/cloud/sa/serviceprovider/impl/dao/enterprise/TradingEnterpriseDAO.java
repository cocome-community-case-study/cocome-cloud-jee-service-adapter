package cocome.cloud.sa.serviceprovider.impl.dao.enterprise;

import cocome.cloud.sa.serviceprovider.impl.dao.DataAccessObject;
import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.ProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.remote.access.DatabaseAccess;
import org.cocome.tradingsystem.remote.access.Notification;

import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * DAO for {@link TradingEnterprise}
 * @author Rudolf Biczok
 */
@Dependent
public class TradingEnterpriseDAO implements DataAccessObject<TradingEnterprise> {

    @EJB
    private DatabaseAccess databaseAccess;

    @Override
    public String getEntityTypeName() {
        return "tradingenterprise";
    }

    @Override
    public Notification createEntities(List<TradingEnterprise> entities) throws IllegalArgumentException {
        return databaseAccess.createEnterprises(entities);
    }

    @Override
    public Notification updateEntities(List<TradingEnterprise> entities) throws IllegalArgumentException {
        return databaseAccess.updateEnterprises(entities);
    }

    @Override
    public Table<String> toTable(List<TradingEnterprise> list) {
        final Table<String> table = new Table<>();
        table.addHeader("EnterpriseId", "EnterpriseName", "SupplierId", "SupplierName");
        int row = 0;
        for (final TradingEnterprise nextEnterprise : list) {
            final Collection<ProductSupplier> suppliers = nextEnterprise.getSuppliers();
            if (suppliers.isEmpty()) {
                table.addColumn(row, 0, String.valueOf(nextEnterprise.getId()), true);
                table.addColumn(row, 1, nextEnterprise.getName(), true);
                table.addColumn(row, 2, "N/A", true);
                table.addColumn(row, 3, "N/A", true);
                row++;
            }

            for (final ProductSupplier supplier : nextEnterprise.getSuppliers()) {
                table.addColumn(row, 0, String.valueOf(nextEnterprise.getId()), true);
                table.addColumn(row, 1, nextEnterprise.getName(), true);
                table.addColumn(row, 2, String.valueOf(supplier.getId()), true);
                table.addColumn(row, 3, supplier.getName(), true);
                row++;
            }
        }
        return table;
    }

    @Override
    public List<TradingEnterprise> fromTable(Table<String> table) {
        final List<TradingEnterprise> list = new ArrayList<>();
        final int len = table.size();
        Column<String> colName;
        Column<String> colId;
        for (int i = 0; i < len; i++) {
            colName = table.getColumnByName(i, "EnterpriseName");
            colId = table.getColumnByName(i, "EnterpriseId");
            final TradingEnterprise t = new TradingEnterprise();
            t.setName(colName.getValue());
            // t.setId(-1l); -> this probably causes the bug that only one enterprise can be created
            if (colId != null) {
                t.setId(Long.parseLong(colId.getValue()));
            }
            list.add(t);
        }
        return list;
    }
}
