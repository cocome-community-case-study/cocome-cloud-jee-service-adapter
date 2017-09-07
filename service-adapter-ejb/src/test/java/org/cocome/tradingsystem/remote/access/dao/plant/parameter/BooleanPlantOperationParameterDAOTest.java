package org.cocome.tradingsystem.remote.access.dao.plant.parameter;

import org.cocome.tradingsystem.inventory.data.plant.parameter.BooleanPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class BooleanPlantOperationParameterDAOTest {

    private BooleanPlantOperationParameterDAO dao = TestUtils.injectFakeEJB(BooleanPlantOperationParameterDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        final PlantOperation product = new PlantOperation();

        final BooleanPlantOperationParameter param = new BooleanPlantOperationParameter();
        param.setPlantOperation(product);
        param.setCategory("Ingredients");
        param.setName("With Chocolate");

        tx.begin();
        em.persist(param);
        tx.commit();

        final List<BooleanPlantOperationParameter> queryedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT param from BooleanPlantOperationParameter param",
                        BooleanPlantOperationParameter.class).getResultList();

        final String expectedTableContent = String.format("PlantOperationId;BooleanPlantOperationParameterId;"
                        + "BooleanPlantOperationParameterName;BooleanPlantOperationParameterCategory\n"
                        + "%d;%d;With Chocolate;Ingredients",
                product.getId(),
                param.getId());

        Assert.assertNotNull(queryedInstances);
        Assert.assertFalse(queryedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queryedInstances)));
    }
}
