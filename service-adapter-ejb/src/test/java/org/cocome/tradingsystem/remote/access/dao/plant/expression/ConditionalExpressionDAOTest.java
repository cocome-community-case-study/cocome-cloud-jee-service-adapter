package org.cocome.tradingsystem.remote.access.dao.plant.expression;

import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.expression.ConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.parameter.NorminalPlantOperationParameter;
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

public class ConditionalExpressionDAOTest {

    private ConditionalExpressionDAO dao = TestUtils.injectFakeEJB(ConditionalExpressionDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        final TradingEnterprise enterprise = new TradingEnterprise();
        em.persist(enterprise);

        final Plant plant = new Plant();
        plant.setEnterprise(enterprise);
        em.persist(plant);

        final ProductionUnitClass puc = new ProductionUnitClass();
        puc.setPlant(plant);
        em.persist(puc);

        final ProductionUnitOperation op1 = new ProductionUnitOperation();
        op1.setProductionUnitClass(puc);
        op1.setName("Name_of_op1");
        op1.setOperationId("OP_1");
        op1.setExpectedExecutionTime(10);
        em.persist(op1);

        final ProductionUnitOperation op2 = new ProductionUnitOperation();
        op2.setProductionUnitClass(puc);
        op2.setName("Name_of_op2");
        op2.setOperationId("OP_2");
        op2.setExpectedExecutionTime(10);
        em.persist(op2);

        final ProductionUnitOperation op3 = new ProductionUnitOperation();
        op3.setProductionUnitClass(puc);
        op3.setName("Name_of_op3");
        op3.setOperationId("OP_3");
        op3.setExpectedExecutionTime(10);
        em.persist(op3);

        final PlantOperation operation = new PlantOperation();
        operation.setPlant(plant);
        em.persist(operation);

        final NorminalPlantOperationParameter param = new NorminalPlantOperationParameter();
        operation.setPlant(plant);
        param.setPlantOperation(operation);
        em.persist(param);

        final ConditionalExpression conditionExp = new ConditionalExpression();
        conditionExp.setParameter(param);
        conditionExp.setParameterValue("BRAINS");
        conditionExp.setOnTrueExpressions(Arrays.asList(op1, op1, op2));
        conditionExp.setOnFalseExpressions(Arrays.asList(op2, op3));
        em.persist(conditionExp);

        tx.commit();

        final List<ConditionalExpression> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT exp from ConditionalExpression exp WHERE exp.id = " + conditionExp.getId(),
                        ConditionalExpression.class).getResultList();

        final String expectedTableContent = String.format(
                "PlantOperationParameterId;ConditionalExpressionId;"
                        + "ConditionalExpressionParameterValue;ConditionalExpressionOnTrueExpressions;"
                        + "ConditionalExpressionOnFalseExpressions\n"
                        + "%1$d;%2$d;BRAINS;%3$d,%3$d,%4$d;%4$d,%5$d",
                param.getId(),
                conditionExp.getId(),
                op1.getId(),
                op2.getId(),
                op3.getId());

        Assert.assertNotNull(queriedInstances);
        Assert.assertFalse(queriedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queriedInstances)));
    }
}