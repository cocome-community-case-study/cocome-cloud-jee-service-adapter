package org.cocome.tradingsystem.remote.access.dao.plant.parameter;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.parameter.NominalParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.RecipeOperation;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * DAO for {@link NominalParameter}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class NominalParameterDAO extends AbstractDAO<NominalParameter> {

    private static final String RECIPE_OPERATION_COL = RecipeOperation.class.getSimpleName() + "Id";
    private static final String ID_COL = NominalParameter.class.getSimpleName() + "Id";
    private static final String NAME_COL = NominalParameter.class.getSimpleName() + "Name";
    private static final String CATEGORY_COL = NominalParameter.class.getSimpleName() + "Category";
    private static final String OPTONS_COL = NominalParameter.class.getSimpleName() + "Options";

    @Override
    public Class<NominalParameter> getEntityType() {
        return NominalParameter.class;
    }

    @Override
    public Table<String> toTable(final List<NominalParameter> list) {
        final Table<String> table = new Table<>();
        table.addHeader(RECIPE_OPERATION_COL, ID_COL, NAME_COL, CATEGORY_COL, OPTONS_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getOperation().getId()));
            table.set(i, 1, String.valueOf(list.get(i).getId()));
            table.set(i, 2, list.get(i).getName());
            table.set(i, 3, list.get(i).getCategory());
            table.set(i, 4, joinStringValues(list.get(i).getOptions()));
        }
        return table;
    }

    @Override
    public List<NominalParameter> fromTable(final EntityManager em,
                                            final Table<String> table,
                                            final Notification notification,
                                            final String sourceOperation) {
        final int len = table.size();
        final List<NominalParameter> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colProductId = table.getColumnByName(i, RECIPE_OPERATION_COL);
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colName = table.getColumnByName(i, NAME_COL);
            final Column<String> colCategory = table.getColumnByName(i, CATEGORY_COL);
            final Column<String> colOptions = table.getColumnByName(i, OPTONS_COL);

            final NominalParameter param = getOrCreateReferencedEntity(
                    NominalParameter.class, colId, em);
            try {
                param.setOperation(getReferencedEntity(
                        RecipeOperation.class,
                        Long.valueOf(colProductId.getValue()),
                        em));
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            param.setName(colName.getValue());
            param.setCategory(colCategory.getValue());
            param.setOptions(new HashSet<>(Arrays.asList(colOptions.getValue().split(SET_DELIMITER))));
            list.add(param);
        }
        return list;
    }
}
