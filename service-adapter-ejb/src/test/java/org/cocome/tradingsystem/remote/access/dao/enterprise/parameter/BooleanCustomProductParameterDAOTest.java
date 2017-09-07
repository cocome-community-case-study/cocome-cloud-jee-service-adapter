package org.cocome.tradingsystem.remote.access.dao.enterprise.parameter;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.BooleanCustomProductParameter;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class BooleanCustomProductParameterDAOTest {

    private BooleanCustomProductParameterDAO dao = TestUtils.injectFakeEJB(BooleanCustomProductParameterDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        final CustomProduct product = new CustomProduct();
        product.setBarcode(12345);

        final BooleanCustomProductParameter param = new BooleanCustomProductParameter();
        param.setProduct(product);
        param.setCategory("Ingredients");
        param.setName("With Chocolate");

        tx.begin();
        em.persist(param);
        tx.commit();

        final List<BooleanCustomProductParameter> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT param from BooleanCustomProductParameter param", BooleanCustomProductParameter.class).getResultList();

        final String expectedTableContent = String.format("CustomProductId;BooleanCustomProductParameterId;"
                        + "BooleanCustomProductParameterName;BooleanCustomProductParameterCategory\n"
                        + "%d;%d;With Chocolate;Ingredients",
                product.getId(),
                param.getId());

        Assert.assertNotNull(queriedInstances);
        Assert.assertFalse(queriedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queriedInstances)));
    }
}
