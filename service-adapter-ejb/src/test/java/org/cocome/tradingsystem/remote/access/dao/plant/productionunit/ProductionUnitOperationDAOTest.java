package org.cocome.tradingsystem.remote.access.dao.plant.productionunit;

import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ProductionUnitOperationDAOTest {

    private ProductionUnitOperationDAO pucDAO = TestUtils.injectFakeEJB(ProductionUnitOperationDAO.class);

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

        final ProductionUnitOperation pucOp1 = new ProductionUnitOperation();
        pucOp1.setOperationId("_1_2_1_P2_O1");
        pucOp1.setName("Name_of_op1");
        pucOp1.setExpectedExecutionTime(10);
        pucOp1.setProductionUnitClass(puc);
        em.persist(pucOp1);

        final ProductionUnitOperation pucOp2 = new ProductionUnitOperation();
        pucOp2.setOperationId("_1_2_1_P2_O2");
        pucOp2.setName("Name_of_op2");
        pucOp2.setExpectedExecutionTime(10);
        pucOp2.setProductionUnitClass(puc);
        em.persist(pucOp2);

        tx.commit();

        final List<ProductionUnitOperation> queryedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT op from ProductionUnitOperation op WHERE op.productionUnitClass.id = "
                        + puc.getId(), ProductionUnitOperation.class).getResultList();

        final String expectedTableContent = String.format("ProductionUnitOperationId;ProductionUnitOperationName;" +
                "ProductionUnitOperationOID;ProductionUnitOperationExpectedTime;ProductionUnitClassId\n" +
                "%2$d;Name_of_op1;_1_2_1_P2_O1;10;%1$d\n" +
                "%3$d;Name_of_op2;_1_2_1_P2_O2;10;%1$d", puc.getId(), pucOp1.getId(), pucOp2.getId());

        Assert.assertNotNull(queryedInstances);
        Assert.assertFalse(queryedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(pucDAO.toTable(queryedInstances)));
    }
}
