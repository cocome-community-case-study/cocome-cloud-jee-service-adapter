package org.cocome.tradingsystem.inventory.data.plant.productionunit.expression;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Represents an expression that can be used for plant-local recipes.
 * @author Rudolf Biczok
 */
@Entity
public class Expression implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;

    /**
     * @return A unique identifier of this Plant.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    /**
     * @param id
     *            a unique identifier of this Plant
     */
    public void setId(final long id) {
        this.id = id;
    }
}
