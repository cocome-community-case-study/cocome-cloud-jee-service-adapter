package org.cocome.tradingsystem.remote.access.dao.enterprise;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.plant.parameter.BooleanPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class CustomProductDAOTest {

    private CustomProductDAO dao = TestUtils.injectFakeEJB(CustomProductDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        final CustomProduct product = new CustomProduct();
        product.setBarcode(9876);
        product.setName("Best Bear");
        product.setPurchasePrice(10);

        tx.begin();
        em.persist(product);
        tx.commit();

        final List<CustomProduct> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT p from CustomProduct p",
                        CustomProduct.class).getResultList();

        final String expectedTableContent = String.format("CustomProductId;CustomProductBarcode;CustomProductLocation;"
                        +"CustomProductPurchasePrice\n" +
                        "%d;9876;Best Bear;10.0",
                product.getId());

        Assert.assertNotNull(queriedInstances);
        Assert.assertFalse(queriedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queriedInstances)));
    }
}
