package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.plant.recipe.EntryPoint;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class EntryPointDAOTest {
    private EntryPointDAO dao = TestUtils.injectFakeEJB(EntryPointDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        final EntryPoint entryPoint = new EntryPoint();
        entryPoint.setName("Slot 1");
        em.persist(entryPoint);

        tx.commit();

        final List<EntryPoint> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT e from EntryPoint e WHERE e.id = " + entryPoint.getId(),
                        EntryPoint.class).getResultList();

        final String expectedTableContent = String.format("EntryPointId;EntryPointName\n" +
                        "%d;Slot 1",
                entryPoint.getId());

        Assert.assertNotNull(queriedInstances);
        Assert.assertFalse(queriedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queriedInstances)));
    }
}
