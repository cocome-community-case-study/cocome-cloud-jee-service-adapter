package org.cocome.tradingsystem.inventory.data.enterprise.parameter;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;

import javax.persistence.Entity;

/**
 * Represents a boolean parameter for {@link CustomProduct}
 *
 * @author Rudolf Biczok
 */
@Entity
public class BooleanCustomProductParameter extends CustomProductParameter
        implements IBooleanParameter {
    private static final long serialVersionUID = -2577328715744776645L;
}
