package org.cocome.tradingsystem.remote.access.dao.plant.parameter;

import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.parameter.BooleanParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class BooleanParameterDAOTest {

    private BooleanParameterDAO dao = TestUtils.injectFakeEJB(BooleanParameterDAO.class);

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

        final PlantOperation operation = new PlantOperation();
        operation.setName("PlantOperationName");
        operation.setPlant(plant);
        em.persist(operation);

        final BooleanParameter param = new BooleanParameter();
        param.setOperation(operation);
        param.setCategory("Ingredients");
        param.setName("With Chocolate");
        em.persist(param);

        tx.commit();

        final List<BooleanParameter> queryedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT param from BooleanParameter param",
                        BooleanParameter.class).getResultList();

        final String expectedTableContent = String.format("RecipeOperationId;BooleanParameterId;"
                        + "BooleanParameterName;BooleanParameterCategory\n"
                        + "%d;%d;With Chocolate;Ingredients",
                operation.getId(),
                param.getId());

        Assert.assertNotNull(queryedInstances);
        Assert.assertFalse(queryedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queryedInstances)));
    }
}
