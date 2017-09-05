package org.cocome.tradingsystem.inventory.data.enterprise.parameter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.Set;

/**
 * Gives the user a finite number of options to customize a product
 * @author Rudolf Biczok
 */
@Entity
public class NorminalCustomProductParameter extends CustomProductParameter {
    private static final long serialVersionUID = -2577328715744776645L;

    private Set<String> options;

    /**
     * @return The possible values this options a user can set for this option
     */
    @ElementCollection
    public Set<String> getOptions() {
        return options;
    }

    /**
     * @param options The possible values this options a user can set for this option
     */
    public void setOptions(final Set<String> options) {
        this.options = options;
    }
}
