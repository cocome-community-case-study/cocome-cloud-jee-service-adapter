/*
 ***************************************************************************
 * Copyright 2013 DFG SPP 1593 (http://dfg-spp1593.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **************************************************************************
 */

package org.cocome.tradingsystem.inventory.data.plant.recipe;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Represents a single {@link PlantOperationOrder} entry in the database.
 *
 * @author Rudolf Biczok
 */
@Entity
public class ProductionOrderEntry extends RecipeOperationOrderEntry {

    private static final long serialVersionUID = -7683436740437770058L;

    private Recipe recipe;
    private ProductionOrder order;

    /**
     * @return The order object
     */
    @ManyToOne
    public ProductionOrder getOrder() {
        return this.order;
    }

    /**
     * @param order the order object
     */
    public void setOrder(final ProductionOrder order) {
        this.order = order;
    }

    /**
     * @return The recipe to process
     */
    @ManyToOne
    public Recipe getRecipe() {
        return this.recipe;
    }

    /**
     * @param recipe The the recipe to process
     */
    public void setRecipe(final Recipe recipe) {
        this.recipe = recipe;
    }
}
