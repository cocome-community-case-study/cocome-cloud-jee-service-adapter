package org.cocome.tradingsystem.remote.access.dao.enterprise;

import org.cocome.tradingsystem.inventory.data.DataFactory;
import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistence;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContext;
import org.cocome.tradingsystem.remote.access.LoggerConfig;
import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.ProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.LegacyDataAccessObject;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * DAO for {@link TradingEnterprise}
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class TradingEnterpriseDAO implements LegacyDataAccessObject<TradingEnterprise> {

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Override
    public String getEntityTypeName() {
        return "tradingenterprise";
    }

    @Override
    public Notification createEntities(List<TradingEnterprise> entities) throws IllegalArgumentException {
        final Notification notification = new Notification();
        if (entities != null) {
            final EntityManager em = this.emf.createEntityManager();

            for (final TradingEnterprise enterprise : entities) {
                final TradingEnterprise tmpEnterprise = this.queryEnterprise(em, enterprise);
                if (tmpEnterprise != null) {
                    notification.addNotification(
                            "createEnterprise", Notification.FAILED,
                            "Enterprise already available:" + enterprise);
                    return notification;
                }

                final TradingEnterprise _enterprise = new TradingEnterprise();
                _enterprise.setName(enterprise.getName());
                this._persist(enterprise);
                notification.addNotification(
                        "createEnterprise", Notification.SUCCESS,
                        "Enterprise creation:" + _enterprise);
            }
            em.flush();
            em.close();
            return notification;
        }
        throw new IllegalAccessError("[createEnterprie]argument is null");
    }

    @Override
    public Notification updateEntities(List<TradingEnterprise> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();

        TradingEnterprise _enterprise;
        for (final TradingEnterprise nextEnterprise : entities) {
            _enterprise = this.queryEnterpriseById(em, nextEnterprise);
            if (_enterprise == null) {
                notification.addNotification(
                        "updateEnterprise", Notification.FAILED,
                        "Enterprise not available:" + nextEnterprise);
                continue;
            }
            _enterprise.setName(nextEnterprise.getName());
            em.merge(_enterprise);
            notification.addNotification(
                    "updateEnterprise", Notification.SUCCESS,
                    "Update Enterprise:" + nextEnterprise);
        }
        em.flush();
        em.close();
        return notification;
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

    public TradingEnterprise queryEnterprise(final EntityManager em, final TradingEnterprise enterprise) {
        return querySingleInstance(em.createQuery(
                "SELECT te FROM TradingEnterprise te WHERE te.name LIKE :teName",
                TradingEnterprise.class).setParameter("teName", enterprise.getName()));
    }

    public TradingEnterprise queryEnterpriseById(final EntityManager em, final TradingEnterprise enterprise) {
        return querySingleInstance(em.createQuery(
                "SELECT te FROM TradingEnterprise te WHERE te.id = :teId",
                TradingEnterprise.class).setParameter("teId", enterprise.getId()));
    }

    /**
     * Persist the given object in the order they are given.<br>
     * No argument check is done.
     *
     * @param objects the objects to persist
     */
    private void _persist(final Object... objects) {
        final IData data = DataFactory.getInstance(this.emf);
        final IPersistence pm = data.getPersistenceManager();
        final IPersistenceContext pctx = pm.getPersistenceContext();
        try {
            for (Object o : objects) {
                pctx.makePersistent(o);
            }
        } catch (final Exception e) {
            e.printStackTrace();
            if (LoggerConfig.ON) {
                System.out.println(e.getMessage());
            }
        }
    }
}
