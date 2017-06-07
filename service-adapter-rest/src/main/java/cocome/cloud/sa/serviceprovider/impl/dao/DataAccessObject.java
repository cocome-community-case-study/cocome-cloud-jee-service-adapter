package cocome.cloud.sa.serviceprovider.impl.dao;


import de.kit.ipd.java.utils.framework.table.Table;

import org.cocome.tradingsystem.remote.access.Notification;

import java.util.List;

/**
 * Used as interface for data retrieval and manipulation through the rest interface
 * @param <E> the entity type to wrap
 * @author Rudolf Biczok
 */
public interface DataAccessObject<E> {

    String getEntityTypeName();

    Notification createEntities(List<E> list)
            throws IllegalArgumentException;

    Notification updateEntities(List<E> list)
            throws IllegalArgumentException;

    Table<String> toTable(final List<E> list);

    List<E> fromTable(final Table<String> list);

}
