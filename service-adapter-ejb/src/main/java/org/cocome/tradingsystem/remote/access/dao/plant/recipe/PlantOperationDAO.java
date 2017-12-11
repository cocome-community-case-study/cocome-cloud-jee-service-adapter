package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
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
    private static final String MARKUP_ID_COL = PlantOperation.class.getSimpleName() + "Markup";
    private static final String NAME_COL = PlantOperation.class.getSimpleName() + "Name";

    @Override
    public Class<PlantOperation> getEntityType() {
        return PlantOperation.class;
    }

    @Override
    public Table<String> toTable(final List<PlantOperation> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, PLANT_ID_COL, MARKUP_ID_COL, NAME_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, String.valueOf(list.get(i).getPlant().getId()));
            table.set(i, 2, list.get(i).getMarkup());
            table.set(i, 3, list.get(i).getName());
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
            final Column<String> colMarkupId = table.getColumnByName(i, MARKUP_ID_COL);
            final Column<String> colNameId = table.getColumnByName(i, NAME_COL);

            final PlantOperation plantOperation = getOrCreateReferencedEntity(PlantOperation.class, colId, em);
            plantOperation.setName(colNameId.getValue());
            plantOperation.setMarkup(colMarkupId.getValue());
            try {
                plantOperation.setPlant(getReferencedEntity(
                        Plant.class,
                        colPlantId,
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
