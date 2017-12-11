/*
 **************************************************************************
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

import org.cocome.tradingsystem.inventory.data.store.Store;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * The class represents an order of a {@link PlantOperation} in the database.
 *
 * @author Rudolf Biczok
 */
@Entity
public class ProductionOrder extends RecipeOperationOrder {

    private static final long serialVersionUID = -8340585715760459030L;

    private Store store;

    @NotNull
    @ManyToOne
    public Store getStore() {
        return this.store;
    }

    public void setStore(final Store store) {
        this.store = store;
    }
}
