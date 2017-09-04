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

public class ProductionUnitOperationTest {

    private ProductionUnitOperationDAO pucDAO = TestUtils.injectFakeEJB(ProductionUnitOperationDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final TradingEnterprise enterprise = new TradingEnterprise();
        enterprise.setName("CoCoME SE");
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        final ProductionUnitClass puc = new ProductionUnitClass();
        puc.setEnterprise(enterprise);
        puc.setName("xPPU v 0.1 Beta");

        final ProductionUnitOperation pucOp1 = new ProductionUnitOperation();
        pucOp1.setOperationId("_1_2_1_P2_O1");
        pucOp1.setProductionUnitClass(puc);

        final ProductionUnitOperation pucOp2 = new ProductionUnitOperation();
        pucOp2.setOperationId("_1_2_1_P2_O2");
        pucOp2.setProductionUnitClass(puc);

        tx.begin();
        em.persist(pucOp1);
        em.persist(pucOp2);
        tx.commit();

        final List<ProductionUnitOperation> queryedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT op from ProductionUnitOperation op WHERE op.productionUnitClass.id = "
                        + puc.getId(), ProductionUnitOperation.class).getResultList();

        final String expectedTableContent = "ProductionUnitOperationId;ProductionUnitOperationOID;ProductionUnitClassId\n" +
                "1;_1_2_1_P2_O1;2\n" +
                "4;_1_2_1_P2_O2;2";

        Assert.assertNotNull(queryedInstances);
        Assert.assertFalse(queryedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(pucDAO.toTable(queryedInstances)));
    }
}
