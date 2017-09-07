package org.cocome.tradingsystem.remote.access.dao.plant.parameter;

import org.cocome.tradingsystem.inventory.data.plant.parameter.NorminalPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class NorminalPlantOperationParameterDAOTest {

    private NorminalPlantOperationParameterDAO dao = TestUtils.injectFakeEJB(NorminalPlantOperationParameterDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        final PlantOperation operation = new PlantOperation();

        final NorminalPlantOperationParameter param = new NorminalPlantOperationParameter();
        param.setPlantOperation(operation);
        param.setCategory("Ingredients");
        param.setName("Fruits");
        param.setOptions(new HashSet<>(Arrays.asList("Apple", "Banana", "Coconut")));

        tx.begin();
        em.persist(param);
        tx.commit();

        final List<NorminalPlantOperationParameter> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT param from NorminalPlantOperationParameter param", NorminalPlantOperationParameter.class).getResultList();

        final String expectedTableContent = String.format("PlantOperationId;NorminalPlantOperationParameterId;"
                        + "NorminalPlantOperationParameterName;NorminalPlantOperationParameterCategory;"
                        + "NorminalPlantOperationParameterOptions\n"
                        + "%d;%d;Fruits;Ingredients;Apple,Coconut,Banana",
                operation.getId(),
                param.getId());

        Assert.assertNotNull(queriedInstances);
        Assert.assertFalse(queriedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queriedInstances)));
    }
}
