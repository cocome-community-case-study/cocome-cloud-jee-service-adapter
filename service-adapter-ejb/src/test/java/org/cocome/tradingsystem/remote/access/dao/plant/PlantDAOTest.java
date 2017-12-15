package org.cocome.tradingsystem.remote.access.dao.plant;

import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.cocome.tradingsystem.remote.access.dao.enterprise.TradingEnterpriseDAO;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlantDAOTest {

    private TradingEnterpriseDAO enterpriseDAO = TestUtils.injectFakeEJB(TradingEnterpriseDAO.class);

    private PlantDAO plantDAO = TestUtils.injectFakeEJB(PlantDAO.class);

    @Test
    public void tableConversionTest() throws Exception {
        final TradingEnterprise enterprise = new TradingEnterprise();
        enterprise.setName("CoCoME SE");
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        System.out.println(em.createQuery("SELECT DISTINCT e FROM CustomProduct e WHERE e.barcode =1513290338897", CustomProduct.class).getResultList());

        tx.begin();
        em.persist(enterprise);
        tx.commit();

        final List<Plant> list = Arrays.asList(new Plant() {{
            setName("SDQ KIT");
            setLocation("Karlsruhe");
            setEnterprise(enterprise);
        }}, new Plant() {{
            setName("TUM");
            setLocation("Munich");
            setEnterprise(enterprise);
        }}, new Plant() {{
            setName("ETH Zürich");
            setLocation("Zurich");
            setEnterprise(enterprise);
        }});
        final String expected = String.format("TradingEnterpriseId;PlantId;PlantName;PlantLocation\n"
                + "%1$d;0;SDQ KIT;Karlsruhe\n"
                + "%1$d;0;TUM;Munich\n"
                + "%1$d;0;ETH Zürich;Zurich", enterprise.getId());

        final Table<String> table = TestUtils.fromCSV(expected);
        plantDAO.createEntities(table);

        Assert.assertEquals("Check for correct table representation",
                expected, TestUtils.toCSV(plantDAO.toTable(list)));

        final Table<String> dbTable = plantDAO.toTable(TestUtils.TEST_EMF
                .createEntityManager()
                .createQuery("SELECT p FROM Plant p WHERE p.enterprise.id = " + enterprise.getId(), Plant.class).getResultList());

        plantDAO.deleteEntities(dbTable);
        Assert.assertTrue(TestUtils.TEST_EMF
                .createEntityManager()
                .createQuery("SELECT p FROM Plant p WHERE p.enterprise.id = " + enterprise.getId())
                .getResultList().isEmpty());
        Assert.assertFalse(TestUtils.TEST_EMF
                .createEntityManager()
                .createQuery("SELECT e FROM TradingEnterprise e WHERE e.id = " + enterprise.getId())
                .getResultList().isEmpty());
        enterpriseDAO.deleteEntities(Collections.singletonList(enterprise));
        Assert.assertTrue(TestUtils.TEST_EMF
                .createEntityManager()
                .createQuery("SELECT e FROM TradingEnterprise e WHERE e.id = " + enterprise.getId())
                .getResultList().isEmpty());
    }
}
