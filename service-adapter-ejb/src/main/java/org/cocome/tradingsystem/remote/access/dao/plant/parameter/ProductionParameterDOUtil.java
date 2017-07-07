package org.cocome.tradingsystem.remote.access.dao.plant.parameter;

import org.cocome.tradingsystem.inventory.data.plant.parameter.ProductionParameter;

/**
 * Utility functions for DAO classes in the parameter package
 */
class ProductionParameterDOUtil {

    @SuppressWarnings("unchecked")
    static ProductionParameter createInstance(final String className) {
        try {
            final Class<?> clazz = Class.forName(className);
            if (!ProductionParameter.class.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException("Unknown parameter type: " + className);
            }
            final Class<? extends ProductionParameter> paramClass;
            paramClass = (Class<? extends ProductionParameter>) clazz;
            return paramClass.newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new IllegalArgumentException("Cannot create instance of: " + className);
        }
    }
}
