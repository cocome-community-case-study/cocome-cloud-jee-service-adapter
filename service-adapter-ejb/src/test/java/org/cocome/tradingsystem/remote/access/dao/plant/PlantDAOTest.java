package org.cocome.tradingsystem.remote.access.dao.plant;

import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Arrays;
import java.util.List;

public class PlantDAOTest {

    private PlantDAO plantDAO = TestUtils.injectFakeEJB(PlantDAO.class);

    @Test
    public void tableConversionTest() throws Exception {
        final TradingEnterprise enterprise = new TradingEnterprise();
        enterprise.setName("CoCoME SE");
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(enterprise);
        tx.commit();

        final List<Plant> list = Arrays.asList(new Plant() {{
            setId(1);
            setName("SDQ KIT");
            setLocation("Karlsruhe");
            setEnterprise(enterprise);
        }}, new Plant() {{
            setId(2);
            setName("TUM");
            setLocation("Munich");
            setEnterprise(enterprise);
        }}, new Plant() {{
            setId(3);
            setName("ETH Zürich");
            setLocation("Zurich");
            setEnterprise(enterprise);
        }});
        final String expected =
                "TradingEnterpriseId;PlantId;PlantName;PlantLocation\n" +
                        "1;1;SDQ KIT;Karlsruhe\n" +
                        "1;2;TUM;Munich\n" +
                        "1;3;ETH Zürich;Zurich";

        final Table<String> table = TestUtils.fromCSV(expected);
        plantDAO.createEntities(table);

        Assert.assertEquals(expected, TestUtils.toCSV(plantDAO.toTable(list)));
    }

}
