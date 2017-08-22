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

public class ProductionUnitClassDAOTest {

    private ProductionUnitClassDAO pucDAO = TestUtils.injectFakeEJB(ProductionUnitClassDAO.class);

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
        puc.getOperations().add(pucOp1);

        final ProductionUnitOperation pucOp2 = new ProductionUnitOperation();
        pucOp2.setOperationId("_1_2_1_P2_O2");
        puc.getOperations().add(pucOp2);

        tx.begin();
        em.persist(puc);
        tx.commit();

        final List<ProductionUnitClass> queryedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT puc from ProductionUnitClass puc WHERE puc.enterprise.id = "
                        + enterprise.getId(), ProductionUnitClass.class).getResultList();

        final String expectedTableContent = "ProductionUnitClassId;ProductionUnitClassName;"
                + "TradingEnterpriseId;ProductionUnitOperationId;ProductionUnitOperationOperationId\n"
                + "1;xPPU v 0.1 Beta;2;3;_1_2_1_P2_O1\n"
                + "1;xPPU v 0.1 Beta;2;4;_1_2_1_P2_O2";

        Assert.assertNotNull(queryedInstances);
        Assert.assertFalse(queryedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(pucDAO.toTable(queryedInstances)));

        final String updateTableContent = "ProductionUnitClassId;ProductionUnitClassName;"
                + "TradingEnterpriseId;ProductionUnitOperationId;ProductionUnitOperationOperationId\n"
                + "1;xPPU v 0.1 Beta;2;3;_1_2_1_P2_O1\n"
                + "1;xPPU v 0.1 Beta;2;4;_1_2_1_P2_O2\n"
                + "1;xPPU v 0.1 Beta;2;-1;_1_2_1_P2_O3";

        pucDAO.updateEntities(TestUtils.fromCSV(updateTableContent));
        System.out.println(TestUtils.TEST_EMF.createEntityManager().createQuery("select o from ProductionUnitOperation o", ProductionUnitOperation.class).getResultList());
    }
}
