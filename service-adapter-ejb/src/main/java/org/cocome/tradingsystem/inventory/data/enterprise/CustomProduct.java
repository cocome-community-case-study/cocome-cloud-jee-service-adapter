package org.cocome.tradingsystem.inventory.data.enterprise;

import org.cocome.tradingsystem.inventory.data.plant.recipe.Recipe;

import javax.persistence.*;

/**
 * This class represents a customizable product in the database
 *
 * @author Rudolf Biczok
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"barcode"}))
public class CustomProduct extends AbstractProduct {

    private static final long serialVersionUID = -2577328715744776645L;

    private Recipe productionRecipe;

    /**
     * @return The recipe used to delegate instructions th the plants
     */
    @OneToOne
    public Recipe getProductionRecipe() {
        return productionRecipe;
    }

    /**
     * @param productionRecipe The recipe used to delegate instructions th the plants
     */
    public void setProductionRecipe(Recipe productionRecipe) {
        this.productionRecipe = productionRecipe;
    }
}
