package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.parameter.Parameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.ParameterValue;
import org.cocome.tradingsystem.inventory.data.plant.recipe.RecipeOperationOrderEntry;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link ParameterValue}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ParameterValueDAO extends AbstractDAO<ParameterValue> {

    private static final String ID_COL = ParameterValue.class.getSimpleName() + "Id";
    private static final String VALUE_COL = ParameterValue.class.getSimpleName() + "Value";
    private static final String PARAMETER_ID_COL = Parameter.class.getSimpleName() + "Id";
    private static final String ORDER_ID_COL = RecipeOperationOrderEntry.class.getSimpleName() + "Id";

    @Override
    public Class<ParameterValue> getEntityType() {
        return ParameterValue.class;
    }

    @Override
    public Table<String> toTable(final List<ParameterValue> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, VALUE_COL, PARAMETER_ID_COL, ORDER_ID_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, list.get(i).getValue());
            table.set(i, 2, String.valueOf(list.get(i).getParameter().getId()));
            table.set(i, 3, String.valueOf(list.get(i).getOrderEntry().getId()));
        }
        return table;
    }

    @Override
    public List<ParameterValue> fromTable(final EntityManager em,
                                          final Table<String> table,
                                          final Notification notification,
                                          final String sourceOperation) {
        final int len = table.size();
        final List<ParameterValue> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colValue = table.getColumnByName(i, VALUE_COL);
            final Column<String> colParameterId = table.getColumnByName(i, PARAMETER_ID_COL);
            final Column<String> colOrderEntryId = table.getColumnByName(i, ORDER_ID_COL);

            final ParameterValue paramValue = getOrCreateReferencedEntity(
                    ParameterValue.class,
                    colId,
                    em);
            paramValue.setValue(colValue.getValue());

            try {
                paramValue.setParameter(getOrCreateReferencedEntity(Parameter.class,
                        colParameterId, em));
                paramValue.setOrderEntry(getOrCreateReferencedEntity(RecipeOperationOrderEntry.class,
                        colOrderEntryId, em));
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            list.add(paramValue);
        }
        return list;
    }

}
