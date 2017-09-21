package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.expression.Expression;
import org.cocome.tradingsystem.inventory.data.plant.recipe.EntryPoint;
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
 * DAO for {@link PlantOperation}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class PlantOperationDAO extends AbstractDAO<PlantOperation> {

    private static final String ID_COL = PlantOperation.class.getSimpleName() + "Id";
    private static final String PLANT_ID_COL = Plant.class.getSimpleName() + "Id";
    private static final String EXPRESSION_ID_COL = Expression.class.getSimpleName() + "Id";
    private static final String NAME_COL = PlantOperation.class.getSimpleName() + "Name";
    private static final String EP_IN_ID_COL = EntryPoint.class.getSimpleName() + "InputId";
    private static final String EP_OUT_ID_COL = EntryPoint.class.getSimpleName() + "OutputId";

    @Override
    public Class<PlantOperation> getEntityType() {
        return PlantOperation.class;
    }

    @Override
    public Table<String> toTable(final List<PlantOperation> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, PLANT_ID_COL, EXPRESSION_ID_COL, NAME_COL, EP_IN_ID_COL, EP_OUT_ID_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, String.valueOf(list.get(i).getPlant().getId()));
            table.set(i, 2, joinValues(list.get(i).getExpressions()));
            table.set(i, 3, list.get(i).getName());
            table.set(i, 4, joinValues(list.get(i).getInputEntryPoint()));
            table.set(i, 5, joinValues(list.get(i).getOutputEntryPoint()));
        }
        return table;
    }

    @Override
    public List<PlantOperation> fromTable(final EntityManager em,
                                          final Table<String> table,
                                          final Notification notification,
                                          final String sourceOperation) {
        final int len = table.size();
        final List<PlantOperation> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colPlantId = table.getColumnByName(i, PLANT_ID_COL);
            final Column<String> colExpressionId = table.getColumnByName(i, EXPRESSION_ID_COL);
            final Column<String> colNameId = table.getColumnByName(i, NAME_COL);
            final Column<String> colEpInId = table.getColumnByName(i, EP_IN_ID_COL);
            final Column<String> colEpOutId = table.getColumnByName(i, EP_OUT_ID_COL);

            final PlantOperation plantOperation = getOrCreateReferencedEntity(PlantOperation.class, colId, em);
            plantOperation.setName(colNameId.getValue());
            try {
                plantOperation.setPlant(getReferencedEntity(
                        Plant.class,
                        colPlantId,
                        em));
                plantOperation.setExpressions(getReferencedEntities(
                        Expression.class,
                        colExpressionId,
                        em));
                plantOperation.setInputEntryPoint(getReferencedEntities(
                        EntryPoint.class,
                        colEpInId,
                        em));
                plantOperation.setOutputEntryPoint(getReferencedEntities(
                        EntryPoint.class,
                        colEpOutId,
                        em));
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            list.add(plantOperation);
        }
        return list;
    }

}
