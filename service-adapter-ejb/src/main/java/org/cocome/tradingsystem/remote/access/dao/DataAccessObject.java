package org.cocome.tradingsystem.remote.access.dao;


import de.kit.ipd.java.utils.framework.table.Table;

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;
import org.cocome.tradingsystem.inventory.data.plant.expression.ConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.parameter.ProductionParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
import org.cocome.tradingsystem.remote.access.Notification;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Objects;

/**
 * Used as interface for data retrieval and manipulation through the rest interface
 *
 * @param <E> the entity type to wrap
 * @author Rudolf Biczok
 */
public interface DataAccessObject<E> {

    String getEntityTypeName();

    Notification createEntities(final List<E> list);

    Notification updateEntities(final List<E> list);

    Table<String> toTable(final List<E> list);

    List<E> fromTable(final Table<String> list);

    default <T> T querySingleInstance(final TypedQuery<T> query) {
        assert query != null;
        T result;
        try {
            result = query.getSingleResult();
        } catch (final NoResultException e) {
            return null;
        }
        return result;
    }
}
