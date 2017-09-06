package org.cocome.tradingsystem.inventory.data.enterprise.parameter;

import org.cocome.tradingsystem.inventory.data.enterprise.NameableEntity;

import java.io.Serializable;

/**
 * Represents a product customization parameter
 *
 * @author Rudolf Biczok
 */
public interface IParameter extends Serializable, NameableEntity {

    /**
     * @return The the parameter category
     */
    String getCategory();

    /**
     * @param category The parameter category
     */
    void setCategory(String category);
}
