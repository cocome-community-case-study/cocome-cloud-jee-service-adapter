package org.cocome.tradingsystem.remote.access.dao.plant.productionunit;

import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitClass;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ProductionUnitClassDAOTest {

    private ProductionUnitClassDAO pucDAO = TestUtils.injectFakeEJB(ProductionUnitClassDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        final TradingEnterprise enterprise = new TradingEnterprise();
        enterprise.setName("CoCoME SE");
        em.persist(enterprise);

        final Plant plant = new Plant();
        plant.setEnterprise(enterprise);
        em.persist(plant);

        final ProductionUnitClass puc = new ProductionUnitClass();
        puc.setPlant(plant);
        puc.setName("xPPU v 0.1 Beta");
        em.persist(puc);

        tx.commit();

        final List<ProductionUnitClass> queryedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT puc from ProductionUnitClass puc WHERE puc.plant.id = "
                        + plant.getId(), ProductionUnitClass.class).getResultList();

        final String expectedTableContent = String.format("PlantId;ProductionUnitClassId;ProductionUnitClassName"
                + System.lineSeparator()
                + "%d;%d;xPPU v 0.1 Beta", plant.getId(), puc.getId());

        Assert.assertNotNull(queryedInstances);
        Assert.assertFalse(queryedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(pucDAO.toTable(queryedInstances)));
    }
}
