package org.cocome.tradingsystem.remote.access.dao.plant.productionunit;

import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ProductionUnitDAOTest {

    private ProductionUnitDAO puDAO = TestUtils.injectFakeEJB(ProductionUnitDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final TradingEnterprise enterprise = new TradingEnterprise();
        enterprise.setName("CoCoME SE");
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        final Plant plant1 = new Plant();
        plant1.setEnterprise(enterprise);

        final Plant plant2 = new Plant();
        plant2.setEnterprise(enterprise);

        final ProductionUnitClass puc = new ProductionUnitClass();
        puc.setEnterprise(enterprise);
        puc.setName("xPPU v 0.1 Beta");

        final ProductionUnitOperation pucOp1 = new ProductionUnitOperation();
        pucOp1.setOperationId("_1_2_1_P2_O1");
        pucOp1.setProductionUnitClass(puc);

        final ProductionUnit pu1 = new ProductionUnit();
        pu1.setPlant(plant1);
        pu1.setProductionUnitClass(puc);
        pu1.setLocation("Room 1");
        pu1.setInterfaceUrl("pu1.mystery.com");

        final ProductionUnit pu2 = new ProductionUnit();
        pu2.setPlant(plant1);
        pu2.setProductionUnitClass(puc);
        pu2.setLocation("Room 2");
        pu2.setInterfaceUrl("pu1.mystery.com");


        final ProductionUnit pu3 = new ProductionUnit();
        pu3.setPlant(plant2);
        pu3.setProductionUnitClass(puc);
        pu3.setLocation("Room 3");
        pu3.setInterfaceUrl("pu3.mystery.com");

        tx.begin();
        em.persist(pu1);
        em.persist(pu2);
        em.persist(pu3);
        em.persist(pucOp1);
        tx.commit();

        final List<ProductionUnit> queryedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT pu from ProductionUnit pu WHERE pu.plant.id = "
                        + plant1.getId(), ProductionUnit.class).getResultList();

        final String expectedTableContent = String.format("ProductionUnitId;ProductionUnitLocation;ProductionUnitInterfaceURL;"
                + "PlantId;ProductionUnitClassId\n"
                + "%3$d;Room 1;pu1.mystery.com;%1$d;%2$d\n"
                + "%4$d;Room 2;pu1.mystery.com;%1$d;%2$d",
                plant1.getId(), puc.getId(), pu1.getId(), pu2.getId());

        Assert.assertNotNull(queryedInstances);
        Assert.assertFalse(queryedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(puDAO.toTable(queryedInstances)));
    }
}