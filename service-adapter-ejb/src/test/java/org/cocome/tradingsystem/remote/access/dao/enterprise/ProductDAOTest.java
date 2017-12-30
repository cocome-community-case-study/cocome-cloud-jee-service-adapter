package org.cocome.tradingsystem.remote.access.dao.enterprise;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ProductDAOTest {

    private ProductDAO dao = TestUtils.injectFakeEJB(ProductDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        tx.begin();

        final CustomProduct product1 = new CustomProduct();
        product1.setBarcode(9876);
        product1.setName("Best Bear");
        product1.setPurchasePrice(10);
        em.persist(product1);

        final CustomProduct product2 = new CustomProduct();
        product2.setBarcode(9999999);
        product2.setName("Best Chocolate");
        product2.setPurchasePrice(100000);
        em.persist(product2);

        tx.commit();

        final List<Product> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT p from Product p WHERE p.id = " + product1.getId(),
                        Product.class).getResultList();

        final String expectedTableContent =
                "ProductBarcode;ProductName;ProductPurchasePrice;ProductType"
                        + System.lineSeparator()
                        + "9876;Best Bear;10.0;org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct";

        Assert.assertNotNull(queriedInstances);
        Assert.assertFalse(queriedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queriedInstances)));

        final List<Product> queriedInstances2 = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT p from CustomProduct p WHERE p.id = " + product2.getId(),
                        Product.class).getResultList();

        final String expectedTableContent2 = "ProductBarcode;ProductName;ProductPurchasePrice;ProductType" +
                System.lineSeparator()
                + "9999999;Best Chocolate;100000.0;org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct";

        Assert.assertNotNull(queriedInstances2);
        Assert.assertFalse(queriedInstances2.isEmpty());
        Assert.assertEquals(expectedTableContent2, TestUtils.toCSV(dao.toTable(queriedInstances2)));

    }
}
