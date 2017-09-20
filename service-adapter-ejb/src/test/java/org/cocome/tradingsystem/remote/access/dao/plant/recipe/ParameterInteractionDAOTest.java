package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.CustomProductParameter;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.parameter.PlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.ParameterInteraction;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ParameterInteractionDAOTest {
    private ParameterInteractionDAO dao = TestUtils.injectFakeEJB(ParameterInteractionDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        final CustomProduct product = new CustomProduct();
        product.setBarcode(11223344);
        em.persist(product);

        final CustomProductParameter customProductParameter = new CustomProductParameter();
        customProductParameter.setProduct(product);
        em.persist(customProductParameter);

        final TradingEnterprise enterprise = new TradingEnterprise();
        em.persist(enterprise);

        final Plant plant = new Plant();
        plant.setEnterprise(enterprise);
        em.persist(plant);

        final PlantOperation operation = new PlantOperation();
        operation.setPlant(plant);
        em.persist(operation);

        final PlantOperationParameter plantOperationParameter = new PlantOperationParameter();
        plantOperationParameter.setPlantOperation(operation);
        em.persist(plantOperationParameter);

        final ParameterInteraction parameterInteraction = new ParameterInteraction();
        parameterInteraction.setFrom(customProductParameter);
        parameterInteraction.setTo(plantOperationParameter);
        em.persist(parameterInteraction);

        tx.commit();

        final List<ParameterInteraction> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT pi from ParameterInteraction pi WHERE pi.id = "
                                + parameterInteraction.getId(),
                        ParameterInteraction.class).getResultList();

        final String expectedTableContent = String.format("ParameterInteractionId;ParameterInteractionToId;"
                        +"ParameterInteractionFromId\n"
                        + "%d;%d;%d",
                parameterInteraction.getId(),
                customProductParameter.getId(),
                plantOperationParameter.getId());

        Assert.assertNotNull(queriedInstances);
        Assert.assertFalse(queriedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queriedInstances)));
    }
}
