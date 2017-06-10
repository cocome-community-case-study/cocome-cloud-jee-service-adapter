package org.cocome.tradingsystem.inventory.data.plant.parameter;

import javax.persistence.Entity;

/**
 * Represents a customization that can either be turned on or off.
 * (e.g. yogurt with extra chocolate sprinkles or not)
 * @param <T> The class type this parameter is associated with
 * @author Rudolf Biczok
 */
@Entity
public class BooleanParameter<T> extends ProductionParameter<T> {
    private static final long serialVersionUID = -2577328715744776645L;
}
