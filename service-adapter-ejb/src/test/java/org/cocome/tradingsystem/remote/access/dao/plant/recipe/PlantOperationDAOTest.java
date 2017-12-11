package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.parameter.PlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;
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

        final ProductionUnitClass puc = new ProductionUnitClass();
        puc.setPlant(plant);
        em.persist(puc);

        final ProductionUnitOperation c1 = new ProductionUnitOperation();
        c1.setOperationId("__OP1");
        c1.setName("Name_of_op1");
        c1.setExecutionDurationInMillis(10);
        c1.setProductionUnitClass(puc);
        em.persist(c1);

        final ProductionUnitOperation c2 = new ProductionUnitOperation();
        c2.setOperationId("__OP2");
        c2.setName("Name_of_op2");
        c2.setExecutionDurationInMillis(10);
        c2.setProductionUnitClass(puc);
        em.persist(c2);

        final ProductionUnitOperation c3 = new ProductionUnitOperation();
        c3.setOperationId("__OP3");
        c3.setName("Name_of_op3");
        c3.setExecutionDurationInMillis(10);
        c3.setProductionUnitClass(puc);
        em.persist(c3);

        final PlantOperationParameter p = new PlantOperationParameter();
        p.setPlantOperation(operation);
        em.persist(p);

        //TODO No validation here so far, i.e., markup could be bullshit
        operation.setMarkup("MARKUP");

        tx.commit();

        final List<PlantOperation> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT opr from PlantOperation opr WHERE opr.id = "
                                + operation.getId(),
                        PlantOperation.class).getResultList();

        final String expectedTableContent = String.format("PlantOperationId;PlantId;PlantOperationMarkup;PlantOperationName;"
                + "EntryPointInputId;EntryPointOutputId\n"
                + "%d;%d;MARKUP;Build Stuff;%d,%d;%d",
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
