package org.cocome.tradingsystem.inventory.data.plant.parameter;

import org.cocome.tradingsystem.inventory.data.enterprise.NameableEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a product customization parameter
 * @param <T> The class type this parameter is associated with
 * @author Rudolf Biczok
 */
@Entity
public class ProductionParameter<T> implements Serializable, NameableEntity {

    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String name;
    private String category;

    /**
     * @return The id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    /**
     * @param id
     *            Identifier value.
     */
    @Override
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The parameter name
     */
    @Override
    @Basic
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The parameter name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The the parameter category
     */
    @Basic
    public String getCategory() {
        return category;
    }

    /**
     * @param category
     *            The parameter category
     */
    public void setCategory(String category) {
        this.category = category;
    }
}
