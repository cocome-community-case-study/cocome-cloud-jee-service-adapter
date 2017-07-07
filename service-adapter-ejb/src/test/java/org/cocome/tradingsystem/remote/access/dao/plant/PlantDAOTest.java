package org.cocome.tradingsystem.remote.access.dao.plant;


import de.kit.ipd.java.utils.framework.table.Table;
import de.kit.ipd.java.utils.parsing.csv.CSVParser;
import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.junit.Assert;
import org.junit.Test;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class PlantDAOTest {

    final EntityManagerFactory factory = Persistence.createEntityManagerFactory(
            "InventoryManagerTest");

    private PlantDAO plantDAO = injectFakeEJB(PlantDAO.class);

    @Test
    public void testToTable() throws Exception {
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
            setLocation("Munique");
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
                "456;2;TUM;Munique\n" +
                "456;3;ETH Zürich;Zurich";

        Assert.assertEquals(expected, toCSV(plantDAO.toTable(list)));
    }

    private String toCSV(final Table<String> table) {
        final CSVParser csvparser = new CSVParser();
        csvparser.setModel(table);
        return csvparser.toString();
    }

    private <T> T injectFakeEJB(Class<T> ejbClass) {
        System.out.println("Inject:" + ejbClass.getName());

        if (!hasAnnotation(ejbClass, LocalBean.class) || !hasAnnotation(ejbClass, Stateless.class)) {
            throw new IllegalArgumentException("@LocalBean and @Stateless annotations must be present on target class: "
                    + ejbClass.getName());
        }
        final Constructor<T> c;
        try {
            c = ejbClass.getConstructor();
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException("Target class has no non-arg constructor: " + ejbClass.getName());
        }
        final T instance;
        try {
            instance = c.newInstance();
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to create instance", e);
        }
        for (final Field f : instance.getClass().getDeclaredFields()) {
            for (final Annotation a : f.getAnnotations()) {
                if (a.annotationType().equals(PersistenceUnit.class)) {
                    final String unitName = f.getAnnotation(PersistenceUnit.class).unitName();
                    if (!unitName.equals(IData.EJB_PERSISTENCE_UNIT_NAME)) {
                        throw new IllegalArgumentException("Unknown unit name:" + unitName);
                    }
                    setProperty(instance, f, factory);
                }
                if(a.annotationType().equals(EJB.class)) {
                    setProperty(instance, f, injectFakeEJB(f.getType()));
                }
            }
        }
        return instance;
    }

    private boolean hasAnnotation(Class<?> ejbClass,
                                  Class<? extends Annotation> annotationClass) {
        return Arrays.stream(ejbClass.getDeclaredAnnotations())
                .filter(a -> a.annotationType().equals(annotationClass)).count() != 0;
    }

    private static void setProperty(final Object object, final Field field, final Object fieldValue) {
        try {
            field.setAccessible(true);
            field.set(object, fieldValue);
            field.setAccessible(false);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
