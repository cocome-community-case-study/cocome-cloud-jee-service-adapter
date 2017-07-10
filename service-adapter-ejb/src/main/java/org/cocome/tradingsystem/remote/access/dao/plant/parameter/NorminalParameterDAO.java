package org.cocome.tradingsystem.remote.access.dao.plant.parameter;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.inventory.data.plant.parameter.NorminalParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.ParameterOption;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.LegacyDataAccessObject;

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
 * DAO for {@link NorminalParameter}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class NorminalParameterDAO implements LegacyDataAccessObject<NorminalParameter> {

    private static final String ID_COL = NorminalParameter.class.getSimpleName() + "Id";
    private static final String NAME_COL = NorminalParameter.class.getSimpleName() + "Name";
    private static final String OPTION_ID_COL = ParameterOption.class.getSimpleName() + "Id";
    private static final String OPTION_NAME_COL = ParameterOption.class.getSimpleName() + "Name";

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Override
    public String getEntityTypeName() {
        return NorminalParameter.class.getSimpleName().toLowerCase();
    }

    @Override
    public Notification createEntities(final List<NorminalParameter> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();
        for (final NorminalParameter entity : entities) {
            em.persist(entity);
            notification.addNotification(
                    "createNorminalParameter", Notification.SUCCESS,
                    "Creation NorminalParameter:" + entity);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Notification updateEntities(List<NorminalParameter> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();

        for (final NorminalParameter entity : entities) {
            if (em.find(NorminalParameter.class, entity.getId()) == null) {
                notification.addNotification(
                        "updateNorminalParameter", Notification.FAILED,
                        "NorminalParameter not available:" + entity);
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
    public Table<String> toTable(final List<NorminalParameter> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, NAME_COL, OPTION_ID_COL, OPTION_NAME_COL);
        int row = 0;
        for (final NorminalParameter<?> param : list) {
            for (final ParameterOption option : param.getOptions()) {
                table.set(row, 0, String.valueOf(param.getId()));
                table.set(row, 1, param.getName());
                table.set(row, 2, String.valueOf(option.getId()));
                table.set(row, 3, option.getName());
                row++;
            }
        }
        return table;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NorminalParameter> fromTable(final Table<String> table) {
        final Map<String, NorminalParameter> map = new HashMap<>();
        final int len = table.size();

        for (int i = 0; i < len; i++) {
            final Column<String> colParamId = table.getColumnByName(i, ID_COL);
            final Column<String> colParamName = table.getColumnByName(i, NAME_COL);
            final Column<String> colOptId = table.getColumnByName(i, OPTION_ID_COL);
            final Column<String> colOptName = table.getColumnByName(i, OPTION_NAME_COL);

            NorminalParameter param = map.get(colParamId.getValue());
            if (param == null) {
                param = new NorminalParameter<>();
                param.setId(Long.parseLong(colParamId.getValue()));
                param.setName(colParamName.getValue());
                param.setOptions(new ArrayList());
                map.put(colParamId.getValue(), param);
            }

            final ParameterOption option = new ParameterOption();
            option.setId(Long.parseLong(colOptId.getValue()));
            option.setName(colOptName.getValue());
            param.getOptions().add(option);
        }

        return new ArrayList<>(map.values());
    }
}
