package org.cocome.tradingsystem.inventory.data.enterprise;


import org.cocome.tradingsystem.inventory.data.plant.parameter.ParameterCategory;
import org.cocome.tradingsystem.inventory.data.plant.recipe.Recipe;

import javax.persistence.*;
import java.util.Collection;

/**
 * This class represents a customizable product in the database
 *
 * @author Rudolf Biczok
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"barcode"}))
public class CustomProduct extends AbstractProduct {

    private static final long serialVersionUID = -2577328715744776645L;

    private Collection<ParameterCategory<CustomProduct>> parameterCategories;
    private Recipe productionRecipe;

    /**
     * @return all available parameters (organized in categories)
     */
    @OneToMany
    public Collection<ParameterCategory<CustomProduct>> getParameterCategory() {
        return parameterCategories;
    }

    /**
     * @param parameterCategories all available parameters (organized in categories)
     */
    public void setParameterCategory(Collection<ParameterCategory<CustomProduct>> parameterCategories) {
        this.parameterCategories = parameterCategories;
    }

    /**
     * @return The recipe used to delegate instructions th the plants
     */
    @OneToOne
    public Recipe getProductionRecipe() {
        return productionRecipe;
    }

    /**
     * @param productionRecipe
     *            The recipe used to delegate instructions th the plants
     */
    public void setProductionRecipe(Recipe productionRecipe) {
        this.productionRecipe = productionRecipe;
    }
}
