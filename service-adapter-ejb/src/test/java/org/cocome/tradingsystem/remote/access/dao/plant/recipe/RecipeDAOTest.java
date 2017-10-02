package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.CustomProductParameter;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.parameter.PlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecipeDAOTest {
    private RecipeDAO dao = TestUtils.injectFakeEJB(RecipeDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        final CustomProduct product = new CustomProduct();
        product.setBarcode(123123);
        em.persist(product);

        final CustomProductParameter customProductParameter = new CustomProductParameter();
        customProductParameter.setProduct(product);
        em.persist(customProductParameter);

        final TradingEnterprise enterprise = new TradingEnterprise();
        em.persist(enterprise);

        final Plant plant = new Plant();
        plant.setEnterprise(enterprise);
        em.persist(plant);

        final EntryPoint in1 = new EntryPoint();
        in1.setName("Slot 1a");
        em.persist(in1);

        final EntryPoint in2 = new EntryPoint();
        in2.setName("Slot 1b");
        em.persist(in2);

        final EntryPoint out = new EntryPoint();
        out.setName("Slot 2");
        em.persist(out);

        final PlantOperation operation = new PlantOperation();
        operation.setPlant(plant);
        em.persist(operation);

        final PlantOperationParameter plantOperationParameter = new PlantOperationParameter();
        plantOperationParameter.setPlantOperation(operation);
        operation.setInputEntryPoint(Arrays.asList(in1, in2));
        operation.setOutputEntryPoint(Collections.singletonList(out));
        em.persist(plantOperationParameter);

        final ParameterInteraction parameterInteraction = new ParameterInteraction();
        parameterInteraction.setFrom(customProductParameter);
        parameterInteraction.setTo(plantOperationParameter);
        em.persist(parameterInteraction);

        final EntryPointInteraction entryPointInteraction = new EntryPointInteraction();
        entryPointInteraction.setFrom(in1);
        entryPointInteraction.setTo(out);
        em.persist(entryPointInteraction);

        final Recipe recipe = new Recipe();
        recipe.setCustomProduct(product);
        recipe.setOperations(Collections.singletonList(operation));
        recipe.setParameterInteractions(Collections.singletonList(parameterInteraction));
        recipe.setEntryPointInteractions(Collections.singletonList(entryPointInteraction));
        em.persist(recipe);

        tx.commit();

        final List<Recipe> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT r from Recipe r WHERE r.id = "
                                + recipe.getId(),
                        Recipe.class).getResultList();

        final String expectedTableContent = String.format("RecipeId;CustomProductId;PlantOperationId;"
                        + "EntryPointInteractionId;ParameterInteractionId\n" +
                        "%d;%d;%d;%d;%d",
                recipe.getId(),
                product.getId(),
                operation.getId(),
                entryPointInteraction.getId(),
                parameterInteraction.getId());

        Assert.assertNotNull(queriedInstances);
        Assert.assertFalse(queriedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queriedInstances)));
    }
}
