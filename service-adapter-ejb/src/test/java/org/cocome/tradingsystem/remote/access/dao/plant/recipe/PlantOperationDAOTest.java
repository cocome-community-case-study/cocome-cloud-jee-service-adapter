package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.recipe.EntryPoint;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlantOperationDAOTest {
    private PlantOperationDAO dao = TestUtils.injectFakeEJB(PlantOperationDAO.class);

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
        operation.setName("Build Stuff");
        operation.setInputEntryPoint(Arrays.asList(in1, in2));
        operation.setOutputEntryPoint(Collections.singletonList(out));
        em.persist(operation);

        tx.commit();

        final List<PlantOperation> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT opr from PlantOperation opr WHERE opr.id = "
                                + operation.getId(),
                        PlantOperation.class).getResultList();

        final String expectedTableContent = String.format("PlantOperationId;PlantId;PlantOperationName;"
                + "EntryPointInputId;EntryPointOutputId\n"
                + "%d;%d;Build Stuff;%d,%d;%d",
                operation.getId(),
                plant.getId(),
                in1.getId(),
                in2.getId(),
                out.getId());

        Assert.assertNotNull(queriedInstances);
        Assert.assertFalse(queriedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queriedInstances)));
    }
}
