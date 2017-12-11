package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.recipe.EntryPoint;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
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

        final TradingEnterprise enterprise = new TradingEnterprise();
        enterprise.setName("TestEnterprise");
        em.persist(enterprise);

        final Plant plant = new Plant();
        plant.setName("TestPlant");
        plant.setEnterprise(enterprise);
        em.persist(plant);

        final PlantOperation opr = new PlantOperation();
        opr.setMarkup("DerDieDasWerWieWas");
        opr.setName("ASDF");
        opr.setPlant(plant);
        em.persist(opr);

        final EntryPoint entryPoint = new EntryPoint();
        entryPoint.setName("Slot 1");
        entryPoint.setOperation(opr);
        entryPoint.setDirection(EntryPoint.Direction.INPUT);
        em.persist(entryPoint);

        tx.commit();

        final List<EntryPoint> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT e from EntryPoint e WHERE e.id = " + entryPoint.getId(),
                        EntryPoint.class).getResultList();

        final String expectedTableContent = String.format("EntryPointId;EntryPointName;RecipeOperationId;EntryPointDirection\n" +
                        "%d;Slot 1;%d;INPUT",
                entryPoint.getId(),opr.getId());

        Assert.assertNotNull(queriedInstances);
        Assert.assertFalse(queriedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queriedInstances)));
    }
}
