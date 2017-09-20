package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import org.cocome.tradingsystem.inventory.data.plant.recipe.EntryPoint;
import org.cocome.tradingsystem.inventory.data.plant.recipe.EntryPointInteraction;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class EntryPointInteractionDAOTest {
    private EntryPointInteractionDAO dao = TestUtils.injectFakeEJB(EntryPointInteractionDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        final EntryPoint e1 = new EntryPoint();
        e1.setName("Unit 1 Solt 1");
        em.persist(e1);

        final EntryPoint e2 = new EntryPoint();
        e2.setName("Unit 2 Solt 2b");
        em.persist(e2);

        final EntryPointInteraction entryPointInteraction = new EntryPointInteraction();
        entryPointInteraction.setFrom(e1);
        entryPointInteraction.setTo(e2);
        em.persist(entryPointInteraction);

        tx.commit();

        final List<EntryPointInteraction> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT ei from EntryPointInteraction ei WHERE ei.id = "
                                + entryPointInteraction.getId(),
                        EntryPointInteraction.class).getResultList();

        final String expectedTableContent = String.format("EntryPointInteractionId;EntryPointInteractionToId;"
                        + "EntryPointInteractionFromId\n" +
                        "%d;%d;%d",
                entryPointInteraction.getId(), e1.getId(), e2.getId());

        Assert.assertNotNull(queriedInstances);
        Assert.assertFalse(queriedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queriedInstances)));
    }
}
