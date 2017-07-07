package org.cocome.tradingsystem.remote.access;

import de.kit.ipd.java.utils.framework.table.Table;
import de.kit.ipd.java.utils.parsing.csv.CSVParser;
import org.cocome.tradingsystem.inventory.data.IData;

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

/**
 * Utilities for unit tests that use JPA together with an embedded database
 * @author Rudolf Biczok
 */
public class TestUtils {

    public static final EntityManagerFactory TEST_EMF = Persistence.createEntityManagerFactory(
            "InventoryManagerTest");

    public static <T> T injectFakeEJB(Class<T> ejbClass) {
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
                    setProperty(instance, f, TEST_EMF);
                }
                if(a.annotationType().equals(EJB.class)) {
                    setProperty(instance, f, injectFakeEJB(f.getType()));
                }
            }
        }
        return instance;
    }

    public static String toCSV(final Table<String> table) {
        final CSVParser csvparser = new CSVParser();
        csvparser.setModel(table);
        return csvparser.toString();
    }

    public static Table<String> fromCSV(final String content) {
        final CSVParser parser = new CSVParser();
        Table<String> table;
        parser.parse(content);
        table = parser.getModel();
        return table;
    }

    private static boolean hasAnnotation(Class<?> ejbClass,
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
