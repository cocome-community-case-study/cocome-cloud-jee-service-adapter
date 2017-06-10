package org.cocome.tradingsystem.inventory.data.plant.parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Represents a category container.
 * @param <T> The class type this parameter category is associated with
 * @author Rudolf Biczok
 */
@Entity
public class ParameterCategory<T> implements Serializable {

    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String name;
    private Collection<ProductionParameter<T>> parameters;

    /**
     * @return The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    /**
     * @param id
     *            Identifier value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The name of the product
     */
    @Basic
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name of the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return all available operations of this production unit type
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Collection<ProductionParameter<T>> getProductionParameters() {
        return parameters;
    }

    /**
     * @param parameters all available operations of this production unit type
     */
    public void setProductionParameters(Collection<ProductionParameter<T>> parameters) {
        this.parameters = parameters;
    }

}
