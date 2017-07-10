package org.cocome.tradingsystem.remote.access.dao.plant.parameter;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.inventory.data.plant.parameter.NorminalParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.ParameterCategory;
import org.cocome.tradingsystem.inventory.data.plant.parameter.ProductionParameter;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.ReflectionUtil;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;
import org.cocome.tradingsystem.remote.access.dao.DataAccessObject;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
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
public class ParameterCategoryDAO extends AbstractDAO<ParameterCategory> {

    private static final String ID_COL = NorminalParameter.class.getSimpleName() + "Id";
    private static final String NAME_COL = NorminalParameter.class.getSimpleName() + "Name";
    private static final String PARAM_ID_COL = ProductionParameter.class.getSimpleName() + "Id";
    private static final String PARAM_NAME_COL = ProductionParameter.class.getSimpleName() + "Name";
    private static final String PARAM_TYPE_COL = ProductionParameter.class.getSimpleName() + "Type";

    @Override
    protected Class<ParameterCategory> getEntityType() {
        return ParameterCategory.class;
    }

    @Override
    protected void syncEntity(EntityManager em, ParameterCategory entity) {
        throw new UnsupportedOperationException();
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

            final ProductionParameter param = ReflectionUtil.createInstance(
                    ProductionParameter.class,
                    colParamType.getValue());
            param.setId(Long.parseLong(colCategoryId.getValue()));
            param.setName(colParamName.getValue());
            category.getProductionParameters().add(param);
        }

        return new ArrayList<>(map.values());
    }
}
