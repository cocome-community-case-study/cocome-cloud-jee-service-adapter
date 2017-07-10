package org.cocome.tradingsystem.remote.access;

/**
 * Utility functions for DAO classes in the parameter package
 */
public class ReflectionUtil {

    @SuppressWarnings("unchecked")
    public static <T> T createInstance(final Class<T> superClass, final String className) {
        try {
            final Class<?> clazz = Class.forName(className);
            if (!superClass.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException("Unknown parameter type: " + className);
            }
            final Class<T> paramClass = (Class<T>) clazz;
            return paramClass.newInstance();
        } catch (final InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new IllegalArgumentException("Cannot create instance of: " + className);
        }
    }
}
