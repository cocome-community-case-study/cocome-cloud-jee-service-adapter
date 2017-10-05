package org.cocome.tradingsystem.remote.access;

import de.kit.ipd.java.utils.framework.table.Table;
import de.kit.ipd.java.utils.parsing.csv.CSVParser;
import org.cocome.tradingsystem.inventory.data.IData;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utilities for unit tests that use JPA together with an embedded database
 *
 * @author Rudolf Biczok
 */
public class TestUtils {

    public static final EntityManagerFactory TEST_EMF = Persistence.createEntityManagerFactory(
            "InventoryManagerTest");

    private static class EJBAnswer implements Answer<Object> {
        private EntityManagerFactory proxyEMF = Mockito.mock(EntityManagerFactory.class);

        private List<EntityTransaction> txList = new ArrayList<>();

        EntityManagerFactory getProxyEMF() {
            return proxyEMF;
        }

        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            final EntityManager em = TEST_EMF.createEntityManager();
            Mockito.when(proxyEMF.createEntityManager()).then(inv -> em);
            if(!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            Object result = invocation.callRealMethod();
            for(int i = 0; i < txList.size(); i ++) {
                txList.get(i).commit();
                txList.remove(i);
            }
            em.getTransaction().commit();
            return result;
        }
    }

    public static <T> T injectFakeEJB(Class<T> ejbClass) {
        if (!hasAnnotation(ejbClass, LocalBean.class) || !hasAnnotation(ejbClass, Stateless.class)) {
            throw new IllegalArgumentException("@LocalBean and @Stateless annotations must be present on target class: "
                    + ejbClass.getName());
        }

        EJBAnswer ejbAnswer = new EJBAnswer();
        final T proxyInstance = Mockito.mock(ejbClass, ejbAnswer);

        for (final Field f : listAllFields(proxyInstance)) {
            for (final Annotation a : f.getAnnotations()) {
                if (a.annotationType().equals(PersistenceUnit.class)) {
                    final String unitName = f.getAnnotation(PersistenceUnit.class).unitName();
                    if (!unitName.equals(IData.EJB_PERSISTENCE_UNIT_NAME)) {
                        throw new IllegalArgumentException("Unknown unit name:" + unitName);
                    }
                    setProperty(proxyInstance, f, ejbAnswer.getProxyEMF());
                }
                if (a.annotationType().equals(EJB.class) || a.annotationType().equals(Inject.class)) {
                    setProperty(proxyInstance, f, injectFakeEJB(f.getType()));
                }
            }
        }
        return proxyInstance;
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

    private static List<Field> listAllFields(final Object obj) {
        final List<Field> current = new ArrayList<>();
        Class tmpClass = obj.getClass();
        while (tmpClass.getSuperclass() != null) {
            current.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
            tmpClass = tmpClass.getSuperclass();
        }
        return current;
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
