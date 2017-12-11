package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.recipe.EntryPoint;
import org.cocome.tradingsystem.inventory.data.plant.recipe.EntryPointInteraction;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.Recipe;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class EntryPointInteractionDAOTest {
    private EntryPointInteractionDAO dao = TestUtils.injectFakeEJB(EntryPointInteractionDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        final CustomProduct product = new CustomProduct();
        product.setBarcode(12312333);
        em.persist(product);

        final TradingEnterprise enterprise = new TradingEnterprise();
        em.persist(enterprise);

        final Plant plant = new Plant();
        plant.setEnterprise(enterprise);
        em.persist(plant);

        final Recipe recipe = new Recipe();
        recipe.setName("RecipeName");
        recipe.setCustomProduct(product);
        recipe.setEnterprise(enterprise);
        em.persist(recipe);

        final PlantOperation operation = new PlantOperation();
        operation.setPlant(plant);
        operation.setName("Build Stuff");
        em.persist(operation);

        final EntryPoint e1 = new EntryPoint();
        e1.setName("Unit 1 Solt 1");
        e1.setOperation(operation);
        e1.setDirection(EntryPoint.Direction.INPUT);
        em.persist(e1);

        final EntryPoint e2 = new EntryPoint();
        e2.setName("Unit 2 Solt 2b");
        e2.setOperation(operation);
        e2.setDirection(EntryPoint.Direction.OUTPUT);
        em.persist(e2);

        final EntryPointInteraction entryPointInteraction = new EntryPointInteraction();
        entryPointInteraction.setFrom(e1);
        entryPointInteraction.setTo(e2);
        entryPointInteraction.setRecipe(recipe);
        em.persist(entryPointInteraction);

        tx.commit();

        final List<EntryPointInteraction> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT ei from EntryPointInteraction ei WHERE ei.id = "
                                + entryPointInteraction.getId(),
                        EntryPointInteraction.class).getResultList();

        final String expectedTableContent = String.format("EntryPointInteractionId;EntryPointInteractionToId;"
                        + "EntryPointInteractionFromId;EntryPointInteractionRecipe\n" +
                        "%d;%d;%d;%d",
                entryPointInteraction.getId(), e2.getId(), e1.getId(), recipe.getId());

        Assert.assertNotNull(queriedInstances);
        Assert.assertFalse(queriedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queriedInstances)));
    }
}
