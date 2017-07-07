package org.cocome.tradingsystem.remote.access;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.cocome.tradingsystem.inventory.data.IData;

@Stateless
@LocalBean
public class DatabaseAccess {

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    /**
     * Trigger a book sale.
     */
    public Notification bookSale(final Object o) {
        // TODO Implement this.
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Object> query(final String query) {
        if ((query != null) && !query.isEmpty()) {
            return this.emf.createEntityManager().createQuery(query).getResultList();
        } else {
            throw new IllegalArgumentException("[query]given arguments are null");
        }
    }
}
