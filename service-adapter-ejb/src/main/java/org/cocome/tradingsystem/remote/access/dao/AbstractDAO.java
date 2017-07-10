package org.cocome.tradingsystem.remote.access.dao;

import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;
import org.cocome.tradingsystem.remote.access.Notification;

import javax.persistence.*;
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
        return this.getEntityType().getClass().getSimpleName().toLowerCase();
    }

    @Override
    public Notification createEntities(final List<E> entities) {
        final Notification notification = new Notification();
        assert entities != null;
        final EntityManager em = this.emf.createEntityManager();
        for (final E entity : entities) {
            final QueryableById entityFromDB = em.find(entity.getClass(), entity.getId());
            if(entityFromDB != null) {
                notification.addNotification(
                        String.format("create%s", getEntityType().getSimpleName()),
                        Notification.SUCCESS,
                        String.format("%s already exists: %d", entity.getClass().getSimpleName(),
                                entityFromDB.getId()));
                continue;
            }
            try {
                syncEntity(em, entity);
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        String.format("create%s", getEntityType().getSimpleName()),
                        Notification.SUCCESS,
                        String.format("%s not available: %s", entity.getClass().getSimpleName(),
                                entity));
                continue;
            }
            em.persist(entity);
            notification.addNotification(
                    String.format("create%s", getEntityType().getSimpleName()),
                    Notification.SUCCESS,
                    String.format("Creation %s: %d", getEntityType().getSimpleName(), entity.getId()));
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Notification updateEntities(final List<E> entities) {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();
        for (final E entity : entities) {
            try {
                getReferencedEntity(entity, em);
                syncEntity(em, entity);
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        String.format("update%s", getEntityType().getSimpleName()),
                        Notification.SUCCESS,
                        String.format("%s not available: %d", entity.getClass().getSimpleName(),
                                entity.getId()));
                continue;
            }
            em.merge(entity);
            notification.addNotification(
                    String.format("update%s", getEntityType().getSimpleName()),
                    Notification.SUCCESS,
                    String.format("Update %s: %d", getEntityType().getSimpleName(), entity.getId()));
        }
        em.flush();
        em.close();
        return notification;
    }

    protected <T extends QueryableById> T getReferencedEntity(final T entity,
                                                              final EntityManager em) {
        assert entity != null;
        assert em != null;
        @SuppressWarnings("unchecked") final Class<T> queryClass = (Class<T>) entity.getClass();
        final T queriedEntity = em.find(queryClass, entity.getId());
        if (queriedEntity == null) {
            throw new EntityNotFoundException(String.valueOf(entity.getId()));
        }
        return queriedEntity;
    }

    protected abstract Class<E> getEntityType();

    protected abstract void syncEntity(final EntityManager em, final E entity);
}
