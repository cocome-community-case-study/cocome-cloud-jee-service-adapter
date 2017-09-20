package org.cocome.tradingsystem.remote.access.dao.enterprise.parameter;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.NorminalCustomProductParameter;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class NorminalCustomProductParameterDAOTest {
    private NorminalCustomProductParameterDAO dao = TestUtils.injectFakeEJB(NorminalCustomProductParameterDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        final CustomProduct product = new CustomProduct();
        product.setBarcode(123);

        final NorminalCustomProductParameter param = new NorminalCustomProductParameter();
        param.setProduct(product);
        param.setCategory("Ingredients");
        param.setName("Fruits");
        param.setOptions(new HashSet<>(Arrays.asList("Apple", "Banana", "Coconut")));

        tx.begin();
        em.persist(product);
        em.persist(param);
        tx.commit();

        final List<NorminalCustomProductParameter> queriedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT param from NorminalCustomProductParameter param", NorminalCustomProductParameter.class).getResultList();

        final String expectedTableContent = String.format("CustomProductId;NorminalCustomProductParameterId;"
                + "NorminalCustomProductParameterName;NorminalCustomProductParameterCategory;"
                + "NorminalCustomProductParameterOptions\n"
                + "%d;%d;Fruits;Ingredients;Apple,Coconut,Banana",
        product.getId(),
                param.getId());

        Assert.assertNotNull(queriedInstances);
        Assert.assertFalse(queriedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(dao.toTable(queriedInstances)));
    }
}
