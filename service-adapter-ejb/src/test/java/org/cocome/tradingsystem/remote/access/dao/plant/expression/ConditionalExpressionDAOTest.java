package org.cocome.tradingsystem.remote.access.dao.plant.expression;

import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.expression.ConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.expression.ConstExpression;
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

        final ProductionUnitClass puc = new ProductionUnitClass();
        puc.setEnterprise(enterprise);

        final ProductionUnitOperation op1 = new ProductionUnitOperation();
        op1.setProductionUnitClass(puc);
        op1.setOperationId("OP_1");

        final ProductionUnitOperation op2 = new ProductionUnitOperation();
        op2.setProductionUnitClass(puc);
        op2.setOperationId("OP_2");

        final ProductionUnitOperation op3 = new ProductionUnitOperation();
        op3.setProductionUnitClass(puc);
        op3.setOperationId("OP_3");

        final Plant plant = new Plant();
        plant.setEnterprise(enterprise);
        final PlantOperation operation = new PlantOperation();
        operation.setPlant(plant);

        final ConstExpression constExp1 = new ConstExpression();
        constExp1.setPlantOperation(operation);
        constExp1.setOperations(Arrays.asList(op1, op1, op1));

        final ConstExpression constExp2 = new ConstExpression();
        constExp2.setPlantOperation(operation);
        constExp2.setOperations(Arrays.asList(op2, op1));

        final ConstExpression constExp3 = new ConstExpression();
        constExp3.setPlantOperation(operation);
        constExp3.setOperations(Arrays.asList(op2, op1, op3));

        final NorminalPlantOperationParameter param = new NorminalPlantOperationParameter();
        final PlantOperation op = new PlantOperation();
        op.setPlant(plant);
        param.setPlantOperation(op);

        final ConditionalExpression conditionExp = new ConditionalExpression();
        conditionExp.setPlantOperation(operation);
        conditionExp.setParameter(param);
        conditionExp.setParameterValue("BRAINS");
        conditionExp.setOnTrueExpressions(Arrays.asList(constExp1, constExp2));
        conditionExp.setOnFalseExpressions(Arrays.asList(constExp3, constExp3));

        em.persist(constExp1);
        em.persist(constExp2);
        em.persist(constExp3);
        em.persist(conditionExp);
        tx.commit();

        final List<ConditionalExpression> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT exp from ConditionalExpression exp", ConditionalExpression.class).getResultList();

        final String expectedTableContent = String.format(
                "PlantOperationId;PlantOperationParameterId;ConditionalExpressionId;"
                + "ConditionalExpressionParameterValue;ConditionalExpressionOnTrueExpressions;"
                + "ConditionalExpressionOnFalseExpressions\n"
                + "%d;%d;%d;BRAINS;%d,%d;%6$d,%6$d",
                operation.getId(),
                param.getId(),
                conditionExp.getId(),
                constExp1.getId(),
                constExp2.getId(),
                constExp3.getId());

        Assert.assertNotNull(queriedInstances);
        Assert.assertFalse(queriedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queriedInstances)));
    }
}