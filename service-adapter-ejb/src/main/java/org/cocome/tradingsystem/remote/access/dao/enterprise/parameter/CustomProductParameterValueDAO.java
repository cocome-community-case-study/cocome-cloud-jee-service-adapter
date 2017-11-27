package org.cocome.tradingsystem.remote.access.dao.enterprise.parameter;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.CustomProductParameter;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.CustomProductParameterValue;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperationParameterValue;
import org.cocome.tradingsystem.inventory.data.plant.recipe.ProductionOrderEntry;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link PlantOperationParameterValue}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class CustomProductParameterValueDAO extends AbstractDAO<CustomProductParameterValue> {

    private static final String ID_COL = CustomProductParameterValue.class.getSimpleName() + "Id";
    private static final String VALUE_COL = CustomProductParameterValue.class.getSimpleName() + "Value";
    private static final String PARAMETER_ID_COL = CustomProductParameter.class.getSimpleName() + "Id";
    private static final String PRODUCTION_ORDER_ENTRY_COL = ProductionOrderEntry.class.getSimpleName() + "Id";

    @Override
    public Class<CustomProductParameterValue> getEntityType() {
        return CustomProductParameterValue.class;
    }

    @Override
    public Table<String> toTable(final List<CustomProductParameterValue> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, VALUE_COL, PARAMETER_ID_COL, PRODUCTION_ORDER_ENTRY_COL);
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
    public List<CustomProductParameterValue> fromTable(final EntityManager em,
                                                       final Table<String> table,
                                                       final Notification notification,
                                                       final String sourceOperation) {
        final int len = table.size();
        final List<CustomProductParameterValue> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colValue = table.getColumnByName(i, VALUE_COL);
            final Column<String> colParameterId = table.getColumnByName(i, PARAMETER_ID_COL);
            final Column<String> colOrderEntryId = table.getColumnByName(i, PRODUCTION_ORDER_ENTRY_COL);

            final CustomProductParameterValue paramValue = getOrCreateReferencedEntity(
                    CustomProductParameterValue.class,
                    colId,
                    em);
            paramValue.setValue(colValue.getValue());

            try {
                paramValue.setParameter(getReferencedEntity(CustomProductParameter.class,
                        colParameterId, em));
                paramValue.setOrderEntry(getReferencedEntity(ProductionOrderEntry.class,
                        colOrderEntryId, em));
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s: not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            list.add(paramValue);
        }
        return list;
    }

}
