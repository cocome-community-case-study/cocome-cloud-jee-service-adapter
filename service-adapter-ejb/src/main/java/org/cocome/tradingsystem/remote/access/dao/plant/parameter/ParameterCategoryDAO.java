package org.cocome.tradingsystem.remote.access.dao.plant.parameter;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.inventory.data.plant.parameter.NorminalParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.ParameterCategory;
import org.cocome.tradingsystem.inventory.data.plant.parameter.ProductionParameter;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.DataAccessObject;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for {@link ParameterCategory}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ParameterCategoryDAO implements DataAccessObject<ParameterCategory> {

    private static final String ID_COL = NorminalParameter.class.getSimpleName() + "Id";
    private static final String NAME_COL = NorminalParameter.class.getSimpleName() + "Name";
    private static final String PARAM_ID_COL = ProductionParameter.class.getSimpleName() + "Id";
    private static final String PARAM_NAME_COL = ProductionParameter.class.getSimpleName() + "Name";
    private static final String PARAM_TYPE_COL = ProductionParameter.class.getSimpleName() + "Type";

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Override
    public String getEntityTypeName() {
        return ParameterCategory.class.getSimpleName().toLowerCase();
    }

    @Override
    public Notification createEntities(final List<ParameterCategory> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();
        for (final ParameterCategory<?> entity : entities) {
            em.persist(entity);
            notification.addNotification(
                    "createParameterCategory", Notification.SUCCESS,
                    "Creation ParameterCategory:" + entity);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Notification updateEntities(List<ParameterCategory> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();

        for (final ParameterCategory<?> entity : entities) {
            if (em.find(ParameterCategory.class, entity.getId()) == null) {
                notification.addNotification(
                        "updateParameterCategory", Notification.FAILED,
                        "ParameterCategory not available:" + entity);
                continue;
            }
            em.merge(entity);
            notification.addNotification(
                    "updateNorminalParameter", Notification.SUCCESS,
                    "Update NorminalParameter:" + entity);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Table<String> toTable(final List<ParameterCategory> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL,
                        NAME_COL,
                        PARAM_ID_COL,
                        PARAM_NAME_COL,
                        PARAM_TYPE_COL);
        int row = 0;
        for (final ParameterCategory<?> param : list) {
            for (final ProductionParameter option : param.getProductionParameters()) {
                table.set(row, 0, String.valueOf(param.getId()));
                table.set(row, 1, param.getName());
                table.set(row, 2, String.valueOf(option.getId()));
                table.set(row, 3, option.getName());
                table.set(row, 4, option.getClass().getName());
                row++;
            }
        }
        return table;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ParameterCategory> fromTable(final Table<String> table) {
        final Map<String, ParameterCategory> map = new HashMap<>();
        final int len = table.size();

        for (int i = 0; i < len; i++) {
            final Column<String> colCategoryId = table.getColumnByName(i, ID_COL);
            final Column<String> colCategoryName = table.getColumnByName(i, NAME_COL);
            final Column<String> colParamId = table.getColumnByName(i, PARAM_ID_COL);
            final Column<String> colParamName = table.getColumnByName(i, PARAM_NAME_COL);
            final Column<String> colParamType = table.getColumnByName(i, PARAM_TYPE_COL);

            ParameterCategory category = map.get(colCategoryId.getValue());
            if (category == null) {
                category = new ParameterCategory<>();
                category.setId(Long.parseLong(colCategoryId.getValue()));
                category.setName(colCategoryName.getValue());
                category.setProductionParameters(new ArrayList<>());
                map.put(colParamId.getValue(), category);
            }

            final ProductionParameter param;
            try {
                final Class<?> clazz = Class.forName(colParamType.getValue());
                if (!ProductionParameter.class.isAssignableFrom(clazz)) {
                    continue;
                }
                final Class<? extends ProductionParameter> paramClass;
                paramClass = (Class<? extends ProductionParameter>) clazz;
                param = paramClass.newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                continue;
            }
            param.setId(Long.parseLong(colCategoryId.getValue()));
            param.setName(colParamName.getValue());
            category.getProductionParameters().add(param);
        }

        return new ArrayList<>(map.values());
    }
}
