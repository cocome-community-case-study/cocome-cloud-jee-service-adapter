package org.cocome.tradingsystem.inventory.data.plant.parameter;

import org.cocome.tradingsystem.inventory.data.enterprise.parameter.INorminalParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.Set;

/**
 * Implementation of {@link INorminalParameter} for {@link PlantOperation}
 *
 * @author Rudolf Biczok
 */
@Entity
public class NorminalPlantOperationParameter extends PlantOperationParameter implements INorminalParameter {
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
