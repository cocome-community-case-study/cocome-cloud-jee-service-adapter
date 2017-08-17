package org.cocome.tradingsystem.remote.access.dao.store;

import org.cocome.tradingsystem.inventory.data.IData;
import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.LegacyDataAccessObject;
import org.cocome.tradingsystem.remote.access.dao.enterprise.TradingEnterpriseDAO;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link Store}
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class StoreDAO implements LegacyDataAccessObject<Store> {

    @EJB
    private TradingEnterpriseDAO tradingEnterpriseDAO;

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Override
    public String getEntityTypeName() {
        return "store";
    }

    @Override
    public Notification createEntities(List<Store> entities) throws IllegalArgumentException {
        final Notification notification = new Notification();
        if (entities != null) {
            final EntityManager em = this.emf.createEntityManager();
            for (final Store nextStore : entities) {
                final TradingEnterprise _enterprise = tradingEnterpriseDAO.queryEnterprise(em, nextStore.getEnterprise());
                if (_enterprise == null) {
                    notification.addNotification(
                            "createStore", Notification.FAILED,
                            "Creation Store failed, Enterprise not available:"
                                    + nextStore.getEnterprise() + "," + nextStore);
                    continue;
                }
                final Store store = new Store();
                store.setName(nextStore.getName());
                store.setLocation(nextStore.getLocation());
                store.setEnterprise(_enterprise);
                _enterprise.getStores().add(store);
                em.persist(store);
                em.merge(_enterprise);
                notification.addNotification(
                        "createStore", Notification.SUCCESS,
                        "Creation Store:" + store);
            }
            em.flush();
            em.close();
            return notification;
        }
        throw new IllegalArgumentException("[createStore]given arguments are null");
    }

    @Override
    public Notification deleteEntities(final List<Store> entities) {
        final Notification notification = new Notification();
        if (entities != null) {
            final EntityManager em = this.emf.createEntityManager();

            for (final Store entity : entities) {
                final Store managedEntity = this.queryStore(em, entity);
                if (managedEntity == null) {
                    notification.addNotification(
                            "createEntities", Notification.FAILED,
                            "Entity not available:" + entity);
                    return notification;
                }

                em.remove(managedEntity);
                notification.addNotification(
                        "deleteEntities", Notification.SUCCESS,
                        "Entity deleted:" + entity);
            }
            em.flush();
            em.close();
            return notification;
        }
        throw new IllegalAccessError("[deleteEntity]argument is null");
    }

    @Override
    public Notification updateEntities(List<Store> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();
        Store _store;
        for (final Store nextStore : entities) {
            _store = em.find(Store.class, nextStore.getId());
            if (_store == null) {
                notification.addNotification(
                        "updateStore", Notification.FAILED,
                        "Store not available:" + nextStore);
                continue;
            }
            // update location
            _store.setLocation(nextStore.getLocation());
            // update name
            _store.setName(nextStore.getName());

            em.merge(_store);
            notification.addNotification(
                    "updateStore", Notification.SUCCESS,
                    "Update Store:" + _store);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Table<String> toTable(List<Store> list) {
        final Table<String> table = new Table<>();
        table.addHeader("EnterpriseName", "StoreId", "StoreName", "StoreLocation");
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, list.get(i).getEnterprise().getName());
            table.set(i, 1, String.valueOf(list.get(i).getId()));
            table.set(i, 2, list.get(i).getName());
            table.set(i, 3, list.get(i).getLocation());
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

    private Store queryStore(final EntityManager em, final Store store) {
        return querySingleInstance(em.createQuery(
                "SELECT s FROM Store s WHERE s.id = :storeId",
                Store.class).setParameter("storeId", store.getId()));
    }
}
