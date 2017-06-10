package org.cocome.tradingsystem.inventory.data.plant.parameter;

import javax.persistence.*;
import java.util.Collection;

/**
 * Gives the user a finite number of options to customize a product
 * @author Rudolf Biczok
 */
@Entity
public class NorminalParameter<T> extends ProductionParameter<T> {
    private static final long serialVersionUID = -2577328715744776645L;

    private Collection<ParameterOption<T>> options;

    /**
     * @return all available options
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Collection<ParameterOption<T>> getOptions() {
        return options;
    }

    /**
     * @param options all available options
     */
    public void setOptions(Collection<ParameterOption<T>> options) {
        this.options = options;
    }
}
