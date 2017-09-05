package org.cocome.tradingsystem.inventory.data.enterprise.parameter;

import javax.persistence.Entity;

/**
 * Represents a customization that can either be turned on or off.
 * (e.g. yogurt with extra chocolate sprinkles or not)
 * @author Rudolf Biczok
 */
@Entity
public class BooleanCustomProductParameter extends CustomProductParameter {
    private static final long serialVersionUID = -2577328715744776645L;
}
