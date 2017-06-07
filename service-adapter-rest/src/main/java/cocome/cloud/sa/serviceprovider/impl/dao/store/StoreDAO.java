package cocome.cloud.sa.serviceprovider.impl.dao.store;

import cocome.cloud.sa.serviceprovider.impl.dao.DataAccessObject;
import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.remote.access.DatabaseAccess;
import org.cocome.tradingsystem.remote.access.Notification;

import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link Store}
 * @author Rudolf Biczok
 */
@Dependent
public class StoreDAO implements DataAccessObject<Store> {

    @EJB
    private DatabaseAccess databaseAccess;

    @Override
    public String getEntityTypeName() {
        return "store";
    }

    @Override
    public Notification createEntities(List<Store> entities) throws IllegalArgumentException {
        return databaseAccess.createStore(entities);
    }

    @Override
    public Notification updateEntities(List<Store> entities) throws IllegalArgumentException {
        return databaseAccess.updateStore(entities);
    }

    @Override
    public Table<String> toTable(List<Store> list) {
        final Table<String> table = new Table<>();
        table.addHeader("EnterpriseName", "StoreId", "StoreName", "StoreLocation");
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.addColumn(i, 0, list.get(i).getEnterprise().getName(), true);
            table.addColumn(i, 1, String.valueOf(list.get(i).getId()), true);
            table.addColumn(i, 2, list.get(i).getName(), true);
            table.addColumn(i, 3, list.get(i).getLocation(), true);
        }
        return table;
    }

    @Override
    public List<Store> fromTable(Table<String> table) {
        final List<Store> list = new ArrayList<>();
        final int len = table.size();
        Column<String> colEnterprise;
        Column<String> colName;
        Column<String> colId;
        Column<String> colLocation;
        for (int i = 0; i < len; i++) {
            colEnterprise = table.getColumnByName(i, "EnterpriseName");
            final TradingEnterprise t = new TradingEnterprise();
            t.setName(colEnterprise.getValue());
            colId = table.getColumnByName(i, "StoreId");
            colName = table.getColumnByName(i, "StoreName");
            colLocation = table.getColumnByName(i, "StoreLocation");
            final Store s = new Store();
            if (colId != null) {
                s.setId(Long.parseLong(colId.getValue()));
            } else {
                s.setId(-1L);
            }
            s.setEnterprise(t);
            s.setLocation(colLocation.getValue());
            s.setName(colName.getValue());
            list.add(s);
        }
        return list;
    }
}
