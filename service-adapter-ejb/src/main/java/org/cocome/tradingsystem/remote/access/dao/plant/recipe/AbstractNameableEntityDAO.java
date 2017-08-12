package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.NameableEntity;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for realy simple entity types
 *
 * @author Rudolf Biczok
 */
public abstract class AbstractNameableEntityDAO<T extends NameableEntity> extends AbstractDAO<T> {

    private static final String ID_COL = "Id";
    private static final String NAME_COL = "Name";

    @Override
    public Table<String> toTable(final List<T> list) {
        final Table<String> table = new Table<>();
        table.addHeader(this.getEntityType().getSimpleName() + ID_COL,
                this.getEntityType().getSimpleName() + NAME_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, list.get(i).getName());
        }
        return table;
    }

    @Override
    public List<T> fromTable(final EntityManager em,
                             final Table<String> table,
                             final Notification notification,
                             final String sourceOperation) {
        final int len = table.size();
        final List<T> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, this.getEntityType().getSimpleName() + ID_COL);
            final Column<String> colName = table.getColumnByName(i, this.getEntityType().getSimpleName() + NAME_COL);

            final T entity = getOrCreateReferencedEntity(this.getEntityType(), colId, em);
            entity.setName(colName.getValue());
            list.add(entity);
        }
        return list;
    }
}
