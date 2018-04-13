package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.recipe.EntryPoint;
import org.cocome.tradingsystem.inventory.data.plant.recipe.RecipeOperation;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link EntryPoint}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class EntryPointDAO extends AbstractDAO<EntryPoint> {

    private static final String ID_COL = EntryPoint.class.getSimpleName() + "Id";
    private static final String NAME_COL = EntryPoint.class.getSimpleName() + "Name";
    private static final String OPERATION_COL = RecipeOperation.class.getSimpleName() + "Id";
    private static final String DIRECTION_COL = EntryPoint.class.getSimpleName() + "Direction";

    @Override
    public Class<EntryPoint> getEntityType() {
        return EntryPoint.class;
    }

    @Override
    public Table<String> toTable(final List<EntryPoint> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, NAME_COL, OPERATION_COL, DIRECTION_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, list.get(i).getName());
            table.set(i, 2, String.valueOf(list.get(i).getOperation().getId()));
            table.set(i, 3, list.get(i).getDirection().name());
        }
        return table;
    }

    @Override
    public List<EntryPoint> fromTable(final EntityManager em,
                                      final Table<String> table,
                                      final Notification notification,
                                      final String sourceOperation) {
        final int len = table.size();
        final List<EntryPoint> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colName = table.getColumnByName(i, NAME_COL);
            final Column<String> colOperation = table.getColumnByName(i, OPERATION_COL);
            final Column<String> colDirection = table.getColumnByName(i, DIRECTION_COL);

            final EntryPoint entryPoint = getOrCreateReferencedEntity(EntryPoint.class, colId, em);
            try {
                entryPoint.setOperation(getReferencedEntity(
                        RecipeOperation.class,
                        colOperation,
                        em));
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }
            entryPoint.setName(colName.getValue());
            entryPoint.setDirection(EntryPoint.Direction.valueOf(colDirection.getValue()));
            list.add(entryPoint);
        }
        return list;
    }
}
