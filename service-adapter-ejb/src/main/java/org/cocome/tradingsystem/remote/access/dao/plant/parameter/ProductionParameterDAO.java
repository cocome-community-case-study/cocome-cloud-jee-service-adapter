package org.cocome.tradingsystem.remote.access.dao.plant.parameter;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.inventory.data.plant.parameter.ProductionParameter;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.ReflectionUtil;
import org.cocome.tradingsystem.remote.access.dao.DataAccessObject;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link ProductionParameter}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ProductionParameterDAO implements DataAccessObject<ProductionParameter> {

    private static final String ID_COL = ProductionParameter.class.getSimpleName() + "Id";
    private static final String NAME_COL = ProductionParameter.class.getSimpleName() + "Name";
    private static final String TYPE_COL = ProductionParameter.class.getSimpleName() + "Type";

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Override
    public String getEntityTypeName() {
        return ProductionParameter.class.getSimpleName().toLowerCase();
    }

    @Override
    public Notification createEntities(final List<ProductionParameter> entities) throws IllegalArgumentException {
        final Notification notification = new Notification();
        if (entities == null) {
            throw new IllegalArgumentException("[createProductionParameter]given arguments are null");
        }
        final EntityManager em = this.emf.createEntityManager();
        for (final ProductionParameter entity : entities) {
            em.persist(entity);
            notification.addNotification(
                    "createProductionParameter", Notification.SUCCESS,
                    "Creation ProductionParameter:" + entity);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Notification updateEntities(final List<ProductionParameter> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();
        for (final ProductionParameter entity : entities) {
            final ProductionParameter booleanParameter = em.find(ProductionParameter.class, entity.getId());
            if (booleanParameter == null) {
                notification.addNotification(
                        "updateBooleanParameter", Notification.FAILED,
                        "BooleanParameter not available:" + entity);
                continue;
            }
            // update name
            booleanParameter.setName(entity.getName());

            em.merge(booleanParameter);
            notification.addNotification(
                    "updateProductionParameter", Notification.SUCCESS,
                    "Update ProductionParameter:" + booleanParameter);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Table<String> toTable(final List<ProductionParameter> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, NAME_COL, TYPE_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, list.get(i).getName());
            table.set(i, 2, list.get(i).getClass().getName());
        }
        return table;
    }

    @Override
    public List<ProductionParameter> fromTable(final Table<String> table) {
        final int len = table.size();
        final List<ProductionParameter> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colName = table.getColumnByName(i, NAME_COL);
            final Column<String> colType = table.getColumnByName(i, TYPE_COL);

            final ProductionParameter param = ReflectionUtil.createInstance(
                    ProductionParameter.class,
                    colType.getValue());
            param.setId(Long.parseLong(colId.getValue()));
            param.setName(colName.getValue());
            list.add(param);
        }
        return list;
    }
}
