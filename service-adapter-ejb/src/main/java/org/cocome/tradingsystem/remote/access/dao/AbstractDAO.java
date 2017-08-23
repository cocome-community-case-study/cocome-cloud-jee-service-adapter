package org.cocome.tradingsystem.remote.access.dao;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;
import org.cocome.tradingsystem.remote.access.Notification;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceUnit;
import java.util.List;

/**
 * Abstract class for data access objects
 *
 * @param <E> the entity type to wrap
 * @author Rudolf Biczok
 */
public abstract class AbstractDAO<E extends QueryableById> implements DataAccessObject<E> {

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Override
    public String getEntityTypeName() {
        return this.getEntityType().getSimpleName().toLowerCase();
    }

    @Override
    public Notification createEntities(final Table<String> table) {
        assert table != null;
        final Notification notification = new Notification();
        final EntityManager em = this.emf.createEntityManager();
        final List<E> entities = fromTable(em, table, notification, "createEntities");
        for (final E entity : entities) {
            final QueryableById entityFromDB = em.find(entity.getClass(), entity.getId());
            if (entityFromDB != null) {
                notification.addNotification(
                        "createEntities",
                        Notification.SUCCESS,
                        String.format("%s already exists: %d", entity.getClass().getSimpleName(),
                                entityFromDB.getId()));
                continue;
            }
            em.persist(entity);
            notification.addNotification(
                    "createEntities",
                    Notification.SUCCESS,
                    String.format("Creation %s: %d", getEntityType().getSimpleName(), entity.getId()));
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Notification updateEntities(final Table<String> table) {
        assert table != null;
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();
        final List<E> entities = fromTable(em, table, notification, "updateEntities");
        for (final E entity : entities) {
            try {
                getReferencedEntity(entity, em);
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        "updateEntities",
                        Notification.SUCCESS,
                        String.format("%s not available: %d", entity.getClass().getSimpleName(),
                                entity.getId()));
                continue;
            }
            em.merge(entity);
            notification.addNotification(
                    "updateEntities",
                    Notification.SUCCESS,
                    String.format("Update %s: %d", getEntityType().getSimpleName(), entity.getId()));
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Notification deleteEntities(final Table<String> table) {
        assert table != null;
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();
        final List<E> entities = fromTable(em, table, notification, "deleteEntities");
        for (final E entity : entities) {
            final E managedEntity;
            try {
                managedEntity = getReferencedEntity(entity, em);
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        "deleteEntities",
                        Notification.SUCCESS,
                        String.format("%s not available: %d", entity.getClass().getSimpleName(),
                                entity.getId()));
                continue;
            }
            em.remove(managedEntity);
            notification.addNotification(
                    "deleteEntities",
                    Notification.SUCCESS,
                    String.format("Delete %s: %d", getEntityType().getSimpleName(), entity.getId()));
        }
        em.flush();
        em.close();
        return notification;
    }

    private <T extends QueryableById> T getReferencedEntity(final T entity,
                                                            final EntityManager em) {
        assert entity != null;
        assert em != null;
        @SuppressWarnings("unchecked") final Class<T> entityType = (Class<T>) entity.getClass();
        return getReferencedEntity(entityType, entity.getId(), em);
    }

    protected <T> T getReferencedEntity(final Class<T> entityClass,
                                        final Column<String> colId,
                                        final EntityManager em) {
        if (colId == null) {
            throw new EntityNotFoundException("colId == null");
        }
        return getReferencedEntity(entityClass, Long.valueOf(colId.getValue()), em);
    }

    protected <T> T getReferencedEntity(final Class<T> entityClass,
                                        final long id,
                                        final EntityManager em) {
        assert entityClass != null;
        assert em != null;
        final T queriedEntity = em.find(entityClass, id);
        if (queriedEntity == null) {
            throw new EntityNotFoundException(String.valueOf(id));
        }
        return queriedEntity;
    }

    protected <T extends QueryableById> T getOrCreateReferencedEntity(final Class<T> entityClass,
                                                                      final Column<String> colId,
                                                                      final EntityManager em) {
        final Long id = colId != null ? Long.parseLong(colId.getValue()) : null;
        final T entity = id != null ? em.find(entityClass, id) : null;
        return returnOrCreateEntity(entityClass, entity);
    }

    protected abstract Class<E> getEntityType();

    protected abstract List<E> fromTable(final EntityManager em,
                                         final Table<String> table,
                                         final Notification notification,
                                         final String sourceOperation);

    private <T> T returnOrCreateEntity(final Class<T> entityClass, T entity) {
        if (entity == null) {
            final T instance;
            try {
                instance = entityClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException("Could not instantiate entity class: "
                        + entityClass.getName());
            }
            return instance;
        }
        return entity;
    }
}
