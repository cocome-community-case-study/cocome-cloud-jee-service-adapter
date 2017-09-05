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

        final BooleanCustomProductParameter param = new BooleanCustomProductParameter();
        param.setProduct(product);
        param.setCategory("Ingredients");
        param.setName("With Chocolate");

        tx.begin();
        em.persist(param);
        tx.commit();

        final List<BooleanCustomProductParameter> queryedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT param from BooleanCustomProductParameter param", BooleanCustomProductParameter.class).getResultList();

        final String expectedTableContent = "CustomProductId;BooleanCustomProductParameterId;"
        + "BooleanCustomProductParameterName;BooleanCustomProductParameterCategory\n"
        + "2;1;With Chocolate;Ingredients";

        Assert.assertNotNull(queryedInstances);
        Assert.assertFalse(queryedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queryedInstances)));
    }
}
