package org.cocome.tradingsystem.inventory.data.plant.expression;

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Represents an expression that can be used for plant-local recipes.
 *
 * @author Rudolf Biczok
 */
@Entity
public class Expression implements Serializable, QueryableById {
    private static final long serialVersionUID = 1L;

    private long id;
    private PlantOperation plantOperation;

    /**
     * @return A unique identifier of this Plant.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    /**
     * @param id a unique identifier of this Plant
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return the associated plant operation
     */
    @NotNull
    @ManyToOne
    public PlantOperation getPlantOperation() {
        return plantOperation;
    }

    /**
     * @param plantOperation the associated plant operation
     */
    public void setPlantOperation(PlantOperation plantOperation) {
        this.plantOperation = plantOperation;
    }

}
