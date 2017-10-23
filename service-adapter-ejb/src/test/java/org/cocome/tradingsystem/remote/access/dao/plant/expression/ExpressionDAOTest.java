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

public class ExpressionDAOTest {

    private ExpressionDAO dao = TestUtils.injectFakeEJB(ExpressionDAO.class);

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
        op1.setExecutionDurationInMillis(10);
        em.persist(op1);

        final ProductionUnitOperation op2 = new ProductionUnitOperation();
        op2.setProductionUnitClass(puc);
        op2.setName("Name_of_op2");
        op2.setOperationId("OP_2");
        op2.setExecutionDurationInMillis(10);
        em.persist(op2);

        final ProductionUnitOperation op3 = new ProductionUnitOperation();
        op3.setProductionUnitClass(puc);
        op3.setName("Name_of_op3");
        op3.setOperationId("OP_3");
        op3.setExecutionDurationInMillis(10);
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

        final ConditionalExpression conditionExp2 = new ConditionalExpression();
        conditionExp2.setParameter(param);
        conditionExp2.setParameterValue("APPLES");
        conditionExp2.setOnTrueExpressions(Arrays.asList(op1, op1, op2, conditionExp));
        conditionExp2.setOnFalseExpressions(Arrays.asList(op2, op3));
        em.persist(conditionExp2);

        tx.commit();

        final String expectedTableContent = String.format(
                "ExpressionType;ExpressionBeginCol;ExpressionEndCol;ProductionUnitOperationId;ProductionUnitOperationName;"
                        + "ProductionUnitOperationOID;ProductionUnitOperationExpectedTime;ProductionUnitClassId;PlantOperationParameterId;"
                        + "ConditionalExpressionId;ConditionalExpressionParameterValue;"
                        + "ConditionalExpressionOnTrueExpressions;ConditionalExpressionOnFalseExpressions\n"
                        + "org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;"
                        + "3;7;%3$d;Name_of_op1;OP_1;10;%6$d;null;null;null;null;null\n"
                        + "org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;"
                        + "3;7;%3$d;Name_of_op1;OP_1;10;%6$d;null;null;null;null;null\n"
                        + "org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;"
                        + "3;7;%4$d;Name_of_op2;OP_2;10;%6$d;null;null;null;null;null\n"
                        + "org.cocome.tradingsystem.inventory.data.plant.expression.ConditionalExpression;"
                        + "8;12;null;null;null;null;null;%1$d;%2$d;BRAINS;%3$d,%3$d,%4$d;%4$d,%5$d",
                param.getId(),
                conditionExp.getId(),
                op1.getId(),
                op2.getId(),
                op3.getId(),
                puc.getId());

        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(conditionExp2.getOnTrueExpressions())));
    }
}