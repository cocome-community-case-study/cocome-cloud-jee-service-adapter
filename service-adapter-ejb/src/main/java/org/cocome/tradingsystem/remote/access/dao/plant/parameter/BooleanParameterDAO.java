package org.cocome.tradingsystem.remote.access.dao.plant.parameter;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.parameter.BooleanParameter;
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
 * DAO for {@link Plant}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class BooleanParameterDAO implements DataAccessObject<BooleanParameter> {

    private static final String ID_COL = BooleanParameter.class.getSimpleName() + "Id";
    private static final String NAME_COL = BooleanParameter.class.getSimpleName() + "Name";

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Override
    public String getEntityTypeName() {
        return BooleanParameter.class.getSimpleName().toLowerCase();
    }

    @Override
    public Notification createEntities(final List<BooleanParameter> entities) throws IllegalArgumentException {
        final Notification notification = new Notification();
        if (entities != null) {
            final EntityManager em = this.emf.createEntityManager();
            for (final BooleanParameter booleanParameter : entities) {
                final BooleanParameter param = new BooleanParameter();
                param.setName(booleanParameter.getName());
                em.persist(param);
                notification.addNotification(
                        "createBooleanParameter", Notification.SUCCESS,
                        "Creation BooleanParameter:" + param);
            }
            em.flush();
            em.close();
            return notification;
        }
        throw new IllegalArgumentException("[createBooleanParameter]given arguments are null");
    }

    @Override
    public Notification updateEntities(final List<BooleanParameter> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();
        for (final BooleanParameter entity : entities) {
            final BooleanParameter booleanParameter = this.queryBooleanParameterById(em, entity);
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
                    "updateBooleanParameter", Notification.SUCCESS,
                    "Update BooleanParameter:" + booleanParameter);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Table<String> toTable(final List<BooleanParameter> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, NAME_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, list.get(i).getName());
        }
        return table;
    }

    @Override
    public List<BooleanParameter> fromTable(final Table<String> table) {
        final int len = table.size();
        final List<BooleanParameter> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colName = table.getColumnByName(i, NAME_COL);
            final BooleanParameter param = new BooleanParameter();
            if (colId != null) {
                param.setId(Long.parseLong(colId.getValue()));
            } else {
                param.setId(-1L);
            }
            param.setName(colName.getValue());
            list.add(param);
        }
        return list;
    }

    BooleanParameter  queryBooleanParameterById(final EntityManager em, final BooleanParameter booleanParameter) {
        return querySingleInstance(em.createQuery(
                "SELECT p FROM BooleanParameter p WHERE p.id = :pId",
                BooleanParameter .class).setParameter("pId", booleanParameter.getId()));
    }
}
