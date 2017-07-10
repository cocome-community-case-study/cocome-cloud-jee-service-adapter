package org.cocome.tradingsystem.remote.access.dao.plant.parameter;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.parameter.BooleanParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.NorminalParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.ParameterOption;
import org.cocome.tradingsystem.inventory.data.plant.parameter.ProductionParameter;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for {@link ProductionParameter}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ProductionParameterDAO extends AbstractDAO<ProductionParameter> {

    private static final String ID_COL = ProductionParameter.class.getSimpleName() + "Id";
    private static final String NAME_COL = ProductionParameter.class.getSimpleName() + "Name";
    private static final String CATEGORY_COL = ProductionParameter.class.getSimpleName() + "Category";
    private static final String TYPE_COL = ProductionParameter.class.getSimpleName() + "Type";
    private static final String OPTION_ID_COL = ParameterOption.class.getSimpleName() + "Id";
    private static final String OPTION_NAME_COL = ParameterOption.class.getSimpleName() + "Name";

    @Override
    public Class<ProductionParameter> getEntityType() {
        return ProductionParameter.class;
    }

    @Override
    public Table<String> toTable(final List<ProductionParameter> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL,
                NAME_COL,
                CATEGORY_COL,
                TYPE_COL,
                OPTION_ID_COL,
                OPTION_NAME_COL);
        final int len = list.size();
        int row = 0;
        for (final ProductionParameter<?> entity : list) {
            if (entity.getClass() == NorminalParameter.class) {
                final NorminalParameter<?> param = (NorminalParameter<?>) entity;
                for (final ParameterOption option : param.getOptions()) {
                    table.set(row, 0, String.valueOf(param.getId()));
                    table.set(row, 1, param.getName());
                    table.set(row, 2, param.getCategory());
                    table.set(row, 3, param.getClass().getName());
                    table.set(row, 4, String.valueOf(option.getId()));
                    table.set(row, 5, option.getName());
                    row++;
                }
            } else if(entity.getClass() == BooleanParameter.class) {
                final BooleanParameter<?> param = (BooleanParameter<?>) entity;
                table.set(row, 0, String.valueOf(param.getId()));
                table.set(row, 1, param.getName());
                table.set(row, 2, param.getCategory());
                table.set(row, 3, param.getClass().getName());
                table.set(row, 4, "");
                table.set(row, 5, "");
                row++;
            } else {
                throw new UnsupportedOperationException("Unknown class: " + entity.getClass());
            }
        }

        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, list.get(i).getName());
            table.set(i, 2, list.get(i).getClass().getName());
        }
        return table;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProductionParameter> fromTable(final EntityManager em,
                                               final Table<String> table,
                                               final Notification notification,
                                               final String sourceOperation) {
        final Map<String, ProductionParameter> map = new HashMap<>();
        final int len = table.size();

        for (int i = 0; i < len; i++) {

            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colName = table.getColumnByName(i, NAME_COL);
            final Column<String> colCategory = table.getColumnByName(i, CATEGORY_COL);
            final Column<String> colType = table.getColumnByName(i, TYPE_COL);
            final Column<String> colOptionId = table.getColumnByName(i, OPTION_ID_COL);
            final Column<String> colOptionName = table.getColumnByName(i, OPTION_NAME_COL);

            if (colType.getValue().equals(NorminalParameter.class.getName())) {
                NorminalParameter<?> param = (NorminalParameter) map.get(colId.getValue());
                if (param == null) {
                    param = getOrCreateReferencedEntity(NorminalParameter.class,
                            Long.parseLong(colId.getValue()),
                            em);
                    param.setName(colName.getValue());
                    param.setCategory(colCategory.getValue());
                    param.setOptions(new ArrayList<>());
                    map.put(colId.getValue(), param);
                }
                final ParameterOption option = getOrCreateReferencedEntity(ParameterOption.class,
                        Long.parseLong(colOptionId.getValue()),
                        em);
                option.setName(colOptionName.getValue());
                param.getOptions().add(option);
            } else if (colType.getValue().equals(BooleanParameter.class.getName())) {
                final BooleanParameter param = getOrCreateReferencedEntity(BooleanParameter.class,
                        Long.parseLong(colId.getValue()),
                        em);
                param.setName(colName.getValue());
                param.setCategory(colCategory.getValue());
                map.put(colId.getValue(), param);
            } else {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("Unknown parameter type: %s", colType.getValue()));
            }
        }
        return new ArrayList<>(map.values());
    }
}
