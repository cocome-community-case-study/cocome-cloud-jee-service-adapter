package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.parameter.Parameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class RecipeDAOTest {
    private RecipeDAO dao = TestUtils.injectFakeEJB(RecipeDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        final TradingEnterprise enterprise = new TradingEnterprise();
        em.persist(enterprise);

        final CustomProduct product = new CustomProduct();
        product.setBarcode(123123);
        em.persist(product);

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

        final EntryPoint in1 = new EntryPoint();
        in1.setName("Slot 1a");
        in1.setDirection(EntryPoint.Direction.INPUT);
        in1.setOperation(operation);
        em.persist(in1);

        final EntryPoint in2 = new EntryPoint();
        in2.setName("Slot 1b");
        in2.setDirection(EntryPoint.Direction.INPUT);
        in2.setOperation(operation);
        em.persist(in2);

        final EntryPoint out = new EntryPoint();
        out.setName("Slot 2");
        out.setDirection(EntryPoint.Direction.OUTPUT);
        out.setOperation(operation);
        em.persist(out);

        final Recipe recipe = new Recipe();
        recipe.setCustomProduct(product);
        recipe.setName("RecipeName");
        recipe.setEnterprise(enterprise);
        em.persist(recipe);

        final RecipeNode node = new RecipeNode();
        node.setRecipe(recipe);
        node.setOperation(operation);

        final Parameter recipeParameter = new Parameter();
        recipeParameter.setOperation(recipe);
        em.persist(recipeParameter);

        final ParameterInteraction parameterInteraction = new ParameterInteraction();
        parameterInteraction.setFrom(recipeParameter);
        parameterInteraction.setTo(plantOperationParameter);
        parameterInteraction.setRecipe(recipe);
        parameterInteraction.setRecipe(recipe);
        em.persist(parameterInteraction);

        final EntryPointInteraction entryPointInteraction = new EntryPointInteraction();
        entryPointInteraction.setFrom(in1);
        entryPointInteraction.setTo(out);
        entryPointInteraction.setRecipe(recipe);
        em.persist(entryPointInteraction);

        tx.commit();

        final List<Recipe> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT r from Recipe r WHERE r.id = "
                                + recipe.getId(),
                        Recipe.class).getResultList();

        final String expectedTableContent = String.format(
                "RecipeId;CustomProductId;RecipeName;TradingEnterpriseId\n"
                        + "%d;%d;RecipeName;%d",
                recipe.getId(), product.getId(), enterprise.getId());

        Assert.assertNotNull(queriedInstances);
        Assert.assertFalse(queriedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queriedInstances)));
    }
}
