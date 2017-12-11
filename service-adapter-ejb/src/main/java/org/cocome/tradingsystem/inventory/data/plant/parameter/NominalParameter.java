package org.cocome.tradingsystem.inventory.data.plant.parameter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.Set;

/**
 * Represents a parameter with multiple (non-multi-select) options
 *
 * @author Rudolf Biczok
 */
@Entity
public class NominalParameter extends Parameter {
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
