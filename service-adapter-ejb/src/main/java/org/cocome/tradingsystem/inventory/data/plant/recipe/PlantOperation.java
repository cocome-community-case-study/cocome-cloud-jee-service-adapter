package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.plant.Plant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Represents an operation provided by an plant
 *
 * @author Rudolf Biczok
 */
@Entity
public class PlantOperation extends RecipeOperation {
    private static final long serialVersionUID = 1L;

    private Plant plant;
    private String markup;

    /**
     * @return the plant that owns this production unit
     */
    @NotNull
    @ManyToOne
    public Plant getPlant() {
        return plant;
    }

    /**
     * @param plant the plant that owns this production unit
     */
    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    /**
     * @return the markup used to describe this operation
     */
    @Lob
    @Column(name = "EXECUTION_MARKUP", length = 512)
    public String getMarkup() {
        return markup;
    }

    /**
     * @param executionMarkup the markup used to describe this operation
     */
    public void setMarkup(final String executionMarkup) {
        this.markup = executionMarkup;
    }
}
