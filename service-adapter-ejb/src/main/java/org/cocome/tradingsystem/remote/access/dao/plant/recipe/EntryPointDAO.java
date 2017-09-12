package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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

    @Override
    public Class<EntryPoint> getEntityType() {
        return EntryPoint.class;
    }

    @Override
    public Table<String> toTable(final List<EntryPoint> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, NAME_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, list.get(i).getName());
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

            final EntryPoint entryPoint = getOrCreateReferencedEntity(EntryPoint.class, colId, em);
            entryPoint.setName(colName.getValue());
            list.add(entryPoint);
        }
        return list;
    }

}
