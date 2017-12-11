package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.parameter.Parameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.ParameterInteraction;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.Recipe;
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

        final TradingEnterprise enterprise = new TradingEnterprise();
        em.persist(enterprise);

        final CustomProduct product = new CustomProduct();
        product.setBarcode(11223344);
        em.persist(product);

        final Recipe recipe = new Recipe();
        recipe.setCustomProduct(product);
        recipe.setName("RecipeName");
        recipe.setEnterprise(enterprise);
        em.persist(recipe);

        final Parameter recipeParameter = new Parameter();
        recipeParameter.setOperation(recipe);
        em.persist(recipeParameter);

        final Plant plant = new Plant();
        plant.setEnterprise(enterprise);
        em.persist(plant);

        final PlantOperation operation = new PlantOperation();
        operation.setPlant(plant);
        operation.setName("PlantOperationName");
        em.persist(operation);

        final Parameter plantOperationParameter = new Parameter();
        plantOperationParameter.setOperation(operation);
        em.persist(plantOperationParameter);

        final ParameterInteraction parameterInteraction = new ParameterInteraction();
        parameterInteraction.setFrom(recipeParameter);
        parameterInteraction.setTo(plantOperationParameter);
        parameterInteraction.setRecipe(recipe);
        em.persist(parameterInteraction);

        tx.commit();

        final List<ParameterInteraction> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT pi from ParameterInteraction pi WHERE pi.id = "
                                + parameterInteraction.getId(),
                        ParameterInteraction.class).getResultList();

        final String expectedTableContent = String.format("ParameterInteractionId;ParameterInteractionToId;"
                        + "ParameterInteractionFromId;ParameterInteractionRecipe\n"
                        + "%d;%d;%d;%d",
                parameterInteraction.getId(),
                plantOperationParameter.getId(),
                recipeParameter.getId(),
                recipe.getId());

        Assert.assertNotNull(queriedInstances);
        Assert.assertFalse(queriedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queriedInstances)));
    }
}
