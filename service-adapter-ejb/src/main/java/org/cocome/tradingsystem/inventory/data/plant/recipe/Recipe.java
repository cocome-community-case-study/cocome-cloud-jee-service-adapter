/*
 *************************************************************************
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
 *************************************************************************
 */

package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * Represents the top-level recipe for producing a custom product.
 *
 * @author Rudolf Biczok
 */
@Entity
public class Recipe extends RecipeOperation {
    private static final long serialVersionUID = 1L;

    private CustomProduct customProduct;
    private TradingEnterprise enterprise;

    /**
     * @return the custom product for which this recipe is used for
     */
    @NotNull
    @OneToOne
    public CustomProduct getCustomProduct() {
        return customProduct;
    }

    /**
     * @param customProduct the custom product for which this recipe is used for
     */
    public void setCustomProduct(CustomProduct customProduct) {
        this.customProduct = customProduct;
    }

    @NotNull
    @ManyToOne
    public TradingEnterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(TradingEnterprise enterprise) {
        this.enterprise = enterprise;
    }
}
