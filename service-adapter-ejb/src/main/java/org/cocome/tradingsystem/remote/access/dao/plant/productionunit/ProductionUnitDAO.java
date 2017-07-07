package org.cocome.tradingsystem.remote.access.dao.plant.productionunit;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitClass;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.DataAccessObject;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link ProductionUnit}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ProductionUnitDAO implements DataAccessObject<ProductionUnit> {

    private static final String ID_COL = ProductionUnit.class.getSimpleName() + "Id";
    private static final String LOCATION_COL = ProductionUnit.class.getSimpleName() + "Location";
    private static final String INTERFACE_URL_COL = ProductionUnit.class.getSimpleName() + "InterfaceURL";
    private static final String PROD_CLASS_ID_COL = ProductionUnitClass.class.getSimpleName() + "Id";
    private static final String PROD_CLASS_NAME_COL = ProductionUnitClass.class.getSimpleName() + "Name";

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Override
    public String getEntityTypeName() {
        return ProductionUnit.class.getSimpleName().toLowerCase();
    }

    @Override
    public Notification createEntities(final List<ProductionUnit> entities) throws IllegalArgumentException {
        final Notification notification = new Notification();
        if (entities == null) {
            throw new IllegalArgumentException("[createProductionUnit]given arguments are null");
        }
        final EntityManager em = this.emf.createEntityManager();
        for (final ProductionUnit entity : entities) {
            final ProductionUnitClass enterprise = em.find(ProductionUnitClass.class,
                    entity.getProductionUnitClass().getId());
            if (enterprise == null) {
                notification.addNotification(
                        "createProductionUnit", Notification.FAILED,
                        "Creation of ProductionUnit failed, ProductionUnitClass not available:"
                                + entity.getProductionUnitClass().getId() + "," + entity.getId());
                continue;
            }
            entity.setProductionUnitClass(enterprise);
            em.persist(entity);
            notification.addNotification(
                    "createProductionUnit", Notification.SUCCESS,
                    "Creation of ProductionUnit:" + entity.getId());
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Notification updateEntities(List<ProductionUnit> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();

        for (final ProductionUnit entity : entities) {
            if (this.queryProductionUnitById(em, entity) == null) {
                notification.addNotification(
                        "updateProductionUnit", Notification.FAILED,
                        "ProductionUnit not available:" + entity);
                continue;
            }
            em.merge(entity);
            notification.addNotification(
                    "updateProductionUnit", Notification.SUCCESS,
                    "Update ProductionUnit:" + entity);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Table<String> toTable(final List<ProductionUnit> entities) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL,
                LOCATION_COL,
                INTERFACE_URL_COL,
                PROD_CLASS_ID_COL,
                PROD_CLASS_NAME_COL);

        int row = 0;
        for (final ProductionUnit entity : entities) {
            table.set(row, 0, String.valueOf(entity.getId()));
            table.set(row, 1, entity.getLocation());
            table.set(row, 2, entity.getInterfaceUrl());
            table.set(row, 3, String.valueOf(entity.getProductionUnitClass().getId()));
            table.set(row, 4, entity.getProductionUnitClass().getName());
            row++;
        }
        return table;
    }

    @Override
    public List<ProductionUnit> fromTable(final Table<String> table) {
        final int len = table.size();
        final List<ProductionUnit> entities = new ArrayList<>(len);

        for (int i = 0; i < len; i++) {
            final Column<String> colPUId = table.getColumnByName(i, ID_COL);
            final Column<String> colPULocation = table.getColumnByName(i, LOCATION_COL);
            final Column<String> colPUInterfaceURL = table.getColumnByName(i, INTERFACE_URL_COL);
            final Column<String> colPUCId = table.getColumnByName(i, PROD_CLASS_ID_COL);
            final Column<String> colPUCName = table.getColumnByName(i, PROD_CLASS_NAME_COL);

            final ProductionUnitClass puc = new ProductionUnitClass();
            puc.setId(Long.parseLong(colPUCId.getValue()));
            puc.setName(colPUCName.getValue());

            final ProductionUnit pu = new ProductionUnit();
            pu.setId(Long.parseLong(colPUId.getValue()));
            pu.setLocation(colPULocation.getValue());
            pu.setInterfaceUrl(colPUInterfaceURL.getValue());
            pu.setProductionUnitClass(puc);

            entities.add(pu);
        }

        return entities;
    }

    ProductionUnit queryProductionUnitById(final EntityManager em, final ProductionUnit pu) {
        return querySingleInstance(em.createQuery(
                "SELECT pu FROM ProductionUnit pu WHERE pu.id = :puId",
                ProductionUnit.class).setParameter("puId", pu.getId()));
    }
}
