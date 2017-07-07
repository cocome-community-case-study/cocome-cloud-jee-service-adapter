package org.cocome.tradingsystem.remote.access.dao;


import de.kit.ipd.java.utils.framework.table.Table;

import org.cocome.tradingsystem.remote.access.Notification;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Used as interface for data retrieval and manipulation through the rest interface
 * @param <E> the entity type to wrap
 * @author Rudolf Biczok
 */
public interface DataAccessObject<E> {

    String getEntityTypeName();

    Notification createEntities(final List<E> list)
            throws IllegalArgumentException;

    Notification updateEntities(final List<E> list)
            throws IllegalArgumentException;

    Table<String> toTable(final List<E> list);

    List<E> fromTable(final Table<String> list);

    default <T> T querySingleInstance(final TypedQuery<T> query) {
        T result;
        try {
            result = query.getSingleResult();
        } catch (final NoResultException e) {
            return null;
        }
        return result;
    }

}
