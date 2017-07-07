package org.cocome.tradingsystem.remote.access.dao.plant;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.DataAccessObject;
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
 * DAO for {@link Plant}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class PlantDAO implements DataAccessObject<Plant> {

    private static final String ENTERPRISE_ID_COL = "EnterpriseId";
    private static final String ID_COL = Plant.class.getSimpleName() + "Id";
    private static final String NAME_COL = Plant.class.getSimpleName() + "Name";
    private static final String LOCATION_COL = Plant.class.getSimpleName() + "Location";

    @EJB
    private TradingEnterpriseDAO tradingEnterpriseDAO;

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Override
    public String getEntityTypeName() {
        return Plant.class.getSimpleName().toLowerCase();
    }

    @Override
    public Notification createEntities(final List<Plant> entities) throws IllegalArgumentException {
        final Notification notification = new Notification();
        if (entities != null) {
            final EntityManager em = this.emf.createEntityManager();
            for (final Plant entity : entities) {
                final TradingEnterprise enterprise = tradingEnterpriseDAO.queryEnterpriseById(em, entity.getEnterprise());
                if (enterprise == null) {
                    notification.addNotification(
                            "createStore", Notification.FAILED,
                            "Creation Plant failed, Enterprise not available:"
                                    + entity.getEnterprise() + "," + entity);
                    continue;
                }
                final Plant plant = new Plant();
                plant.setName(entity.getName());
                plant.setLocation(entity.getLocation());
                plant.setEnterprise(enterprise);
                enterprise.getPlants().add(plant);
                em.persist(plant);
                em.merge(enterprise);
                notification.addNotification(
                        "createPlant", Notification.SUCCESS,
                        "Creation Plant:" + plant);
            }
            em.flush();
            em.close();
            return notification;
        }
        throw new IllegalArgumentException("[createPlant]given arguments are null");
    }

    @Override
    public Notification updateEntities(List<Plant> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();
        Plant plant;
        for (final Plant entity : entities) {
            plant = em.find(Plant.class, entity.getId());
            if (plant == null) {
                notification.addNotification(
                        "updatePlant", Notification.FAILED,
                        "Plant not available:" + entity);
                continue;
            }
            plant.setLocation(entity.getLocation());
            plant.setName(entity.getName());

            em.merge(plant);
            notification.addNotification(
                    "updatePlant", Notification.SUCCESS,
                    "Update Plant:" + plant);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Table<String> toTable(final List<Plant> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ENTERPRISE_ID_COL, ID_COL, NAME_COL, LOCATION_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getEnterprise().getId()));
            table.set(i, 1, String.valueOf(list.get(i).getId()));
            table.set(i, 2, list.get(i).getName());
            table.set(i, 3, list.get(i).getLocation());
        }
        return table;
    }

    @Override
    public List<Plant> fromTable(final Table<String> table) {
        final int len = table.size();
        final List<Plant> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colEnterprise = table.getColumnByName(i, ENTERPRISE_ID_COL);
            final TradingEnterprise t = new TradingEnterprise();
            t.setId(Long.parseLong(colEnterprise.getValue()));
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colName = table.getColumnByName(i, NAME_COL);
            final Column<String> colLocation = table.getColumnByName(i, LOCATION_COL);
            final Plant plant = new Plant();
            if (colId != null) {
                plant.setId(Long.parseLong(colId.getValue()));
            } else {
                plant.setId(-1L);
            }
            plant.setEnterprise(t);
            plant.setLocation(colLocation.getValue());
            plant.setName(colName.getValue());
            list.add(plant);
        }
        return list;
    }
}
