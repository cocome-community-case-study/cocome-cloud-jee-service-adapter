package org.cocome.tradingsystem.inventory.data.enterprise.parameter;

import java.util.Set;

/**
 * Gives the user a finite number of options to customize a product
 * @author Rudolf Biczok
 */
public interface INorminalParameter extends IParameter {
    Set<String> getOptions();
    void setOptions(final Set<String> options);
}
