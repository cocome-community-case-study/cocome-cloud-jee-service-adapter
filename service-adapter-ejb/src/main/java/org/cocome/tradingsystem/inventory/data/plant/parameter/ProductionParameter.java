package org.cocome.tradingsystem.inventory.data.plant.parameter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a product customization parameter
 * @param <T> The class type this parameter is associated with
 * @author Rudolf Biczok
 */
//TODO: This should be @MappedSuperclass though, but would then prevent polymorphism
@Entity
public class ProductionParameter<T> implements Serializable {

    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String name;

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
}
