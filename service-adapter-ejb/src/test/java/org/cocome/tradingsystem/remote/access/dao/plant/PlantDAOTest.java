package org.cocome.tradingsystem.remote.access.dao.plant;

import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PlantDAOTest {

    private PlantDAO plantDAO = TestUtils.injectFakeEJB(PlantDAO.class);

    @Test
    public void tableConversionTest() throws Exception {
        final TradingEnterprise enterprise = new TradingEnterprise() {{
            this.setId(456);
        }};
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
                "EnterpriseId;PlantId;PlantName;PlantLocation\n" +
                "456;1;SDQ KIT;Karlsruhe\n" +
                "456;2;TUM;Munich\n" +
                "456;3;ETH Zürich;Zurich";

        Assert.assertEquals(expected, TestUtils.toCSV(plantDAO.toTable(list)));
        Assert.assertEquals(expected, TestUtils.toCSV(plantDAO.toTable(
                plantDAO.fromTable(TestUtils.fromCSV(expected)))));
    }

}
