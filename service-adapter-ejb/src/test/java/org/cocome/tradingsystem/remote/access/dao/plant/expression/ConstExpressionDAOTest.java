package org.cocome.tradingsystem.remote.access.dao.plant.expression;


import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.expression.ConstExpression;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Arrays;
import java.util.List;

public class ConstExpressionDAOTest {

    private ConstExpressionDAO dao = TestUtils.injectFakeEJB(ConstExpressionDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        final TradingEnterprise enterprise = new TradingEnterprise();
        em.persist(enterprise);

        final ProductionUnitClass puc = new ProductionUnitClass();
        puc.setEnterprise(enterprise);
        em.persist(puc);

        final ProductionUnitOperation op1 = new ProductionUnitOperation();
        op1.setProductionUnitClass(puc);
        op1.setOperationId("OP_1");
        em.persist(op1);

        final ProductionUnitOperation op2 = new ProductionUnitOperation();
        op2.setProductionUnitClass(puc);
        op2.setOperationId("OP_2");
        em.persist(op2);

        final Plant plant = new Plant();
        plant.setEnterprise(enterprise);
        em.persist(plant);

        final PlantOperation operation = new PlantOperation();
        operation.setPlant(plant);
        em.persist(operation);

        final ConstExpression constExp = new ConstExpression();
        constExp.setPlantOperation(operation);
        constExp.setOperations(Arrays.asList(op1, op1, op2));

        em.persist(constExp);
        tx.commit();

        final List<ConstExpression> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT exp from ConstExpression exp WHERE exp.id = " + constExp.getId(), ConstExpression.class).getResultList();

        final String expectedTableContent = String.format(
                          "PlantOperationId;ConstExpressionId;ConstExpressionOperations\n"
                        + "%d;%d;%d,%d,%d",
                operation.getId(),
                constExp.getId(),
                op1.getId(),
                op1.getId(),
                op2.getId());

        Assert.assertNotNull(queriedInstances);
        Assert.assertFalse(queriedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queriedInstances)));
    }
}