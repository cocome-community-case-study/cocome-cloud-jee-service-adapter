package cocome.cloud.sa.serviceprovider.impl.dao.plant;

import cocome.cloud.sa.serviceprovider.impl.dao.DataAccessObject;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.DataFactory;
import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistence;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContext;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.remote.access.LoggerConfig;
import org.cocome.tradingsystem.remote.access.Notification;

import javax.enterprise.context.Dependent;
import javax.persistence.*;
import java.util.List;

/**
 * DAO for {@link Plant}
 *
 * @author Rudolf Biczok
 */
@Dependent
public class PlantDAO implements DataAccessObject<Plant> {

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Override
    public String getEntityTypeName() {
        return "plant";
    }

    @Override
    public Notification createEntities(final List<Plant> entities) throws IllegalArgumentException {
        final Notification notification = new Notification();
        if (entities == null) {
            throw new IllegalAccessError("[createEntities]argument is null");
        }
        final EntityManager em = this.emf.createEntityManager();

        for (final Plant enterprise : entities) {
            final Plant tmpEnterprise = this._queryEnterpriseById(em, enterprise);
            if (tmpEnterprise != null) {
                notification.addNotification(
                        "createEnterprise", Notification.FAILED,
                        "Enterprise already available:" + enterprise);
                return notification;
            }

            final Plant _enterprise = new Plant();
            _enterprise.setName(enterprise.getName());
            this._persist(enterprise);
            notification.addNotification(
                    "createEnterprise", Notification.SUCCESS,
                    "Enterprise creation:" + _enterprise);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Notification updateEntities(final List<Plant> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();

        for (final Plant nextEnterprise : entities) {
            Plant _enterprise = this._queryEnterpriseById(em, nextEnterprise);
            if (_enterprise == null) {
                notification.addNotification(
                        "updateEnterprise", Notification.FAILED,
                        "Enterprise not available:" + nextEnterprise);
                continue;
            }
            _enterprise.setName(nextEnterprise.getName());
            em.merge(_enterprise);
            notification.addNotification(
                    "updateEnterprise", Notification.SUCCESS,
                    "Update Enterprise:" + nextEnterprise);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Table<String> toTable(final List<Plant> list) {
        return null;
    }

    @Override
    public List<Plant> fromTable(final Table<String> table) {
        return null;
    }

    private Plant _queryEnterpriseById(final EntityManager em, final Plant instance) {
        return _querySingleInstance(em.createQuery(
                "SELECT instance FROM Plant instance WHERE instance.id = :instanceId",
                Plant.class).setParameter("instanceId", instance.getId()));
    }

    private <T> T _querySingleInstance(final TypedQuery<T> query) {
        T result;
        try {
            result = query.getSingleResult();
        } catch (final NoResultException e) {
            return null;
        }
        return result;
    }

    private void _persist(final Object... objects) {
        final IData data = DataFactory.getInstance(this.emf);
        final IPersistence pm = data.getPersistenceManager();
        final IPersistenceContext pctx = pm.getPersistenceContext();
        try {
            for (Object o : objects) {
                pctx.makePersistent(o);
            }
        } catch (final Exception e) {
            e.printStackTrace();
            if (LoggerConfig.ON) {
                System.out.println(e.getMessage());
            }
        }
    }
}
