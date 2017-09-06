package org.cocome.tradingsystem.remote.access.dao.plant.parameter;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.parameter.BooleanPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link BooleanPlantOperationParameter}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class BooleanPlantOperationParameterDAO extends AbstractDAO<BooleanPlantOperationParameter> {

    private static final String PLANT_OPERATION_ID_COL = PlantOperation.class.getSimpleName() + "Id";
    private static final String ID_COL = BooleanPlantOperationParameter.class.getSimpleName() + "Id";
    private static final String NAME_COL = BooleanPlantOperationParameter.class.getSimpleName() + "Name";
    private static final String CATEGORY_COL = BooleanPlantOperationParameter.class.getSimpleName() + "Category";

    @Override
    public Class<BooleanPlantOperationParameter> getEntityType() {
        return BooleanPlantOperationParameter.class;
    }

    @Override
    public Table<String> toTable(final List<BooleanPlantOperationParameter> list) {
        final Table<String> table = new Table<>();
        table.addHeader(PLANT_OPERATION_ID_COL, ID_COL, NAME_COL, CATEGORY_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getPlantOperation().getId()));
            table.set(i, 1, String.valueOf(list.get(i).getId()));
            table.set(i, 2, list.get(i).getName());
            table.set(i, 3, list.get(i).getCategory());
        }
        return table;
    }

    @Override
    public List<BooleanPlantOperationParameter> fromTable(final EntityManager em,
                                                          final Table<String> table,
                                                          final Notification notification,
                                                          final String sourceOperation) {
        final int len = table.size();
        final List<BooleanPlantOperationParameter> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colProductId = table.getColumnByName(i, PLANT_OPERATION_ID_COL);
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colName = table.getColumnByName(i, NAME_COL);
            final Column<String> colCategory = table.getColumnByName(i, CATEGORY_COL);

            final PlantOperation plantOpr;
            try {
                plantOpr = getReferencedEntity(
                        PlantOperation.class,
                        Long.valueOf(colProductId.getValue()),
                        em);
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            final BooleanPlantOperationParameter param = getOrCreateReferencedEntity(
                    BooleanPlantOperationParameter.class, colId, em);
            param.setPlantOperation(plantOpr);
            param.setName(colName.getValue());
            param.setCategory(colCategory.getValue());
            list.add(param);
        }
        return list;
    }
}
