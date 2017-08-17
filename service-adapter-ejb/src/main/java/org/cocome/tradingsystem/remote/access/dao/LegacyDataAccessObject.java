package org.cocome.tradingsystem.remote.access.dao;


import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;
import org.cocome.tradingsystem.remote.access.Notification;

import java.util.List;

/**
 * Interface for legacy code
 *
 * @param <E> the entity type to wrap
 * @author Rudolf Biczok
 */
public interface LegacyDataAccessObject<E extends QueryableById> extends DataAccessObject<E> {

    default Notification createEntities(Table<String> table) {
        return createEntities(fromTable(table));
    }

    default Notification updateEntities(Table<String> table) {
        return updateEntities(fromTable(table));
    }

    default Notification deleteEntities(Table<String> table) {
        return deleteEntities(fromTable(table));
    }

    Notification createEntities(final List<E> list);

    Notification updateEntities(final List<E> list);

    Notification deleteEntities(final List<E> list);

    List<E> fromTable(final Table<String> list);
}
