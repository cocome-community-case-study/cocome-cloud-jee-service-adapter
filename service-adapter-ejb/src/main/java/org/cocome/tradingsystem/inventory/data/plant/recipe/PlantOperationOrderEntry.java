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
public class PlantOperationOrderEntry extends RecipeOperationOrderEntry {

    private static final long serialVersionUID = -7683436740437770058L;

    private PlantOperation operation;
    private PlantOperationOrder order;

    /**
     * @return The ProductOrder where the OrderEntry belongs to
     */
    @ManyToOne
    public PlantOperationOrder getOrder() {
        return this.order;
    }

    /**
     * @param productOrder The ProductOrder where the OrderEntry belongs to
     */
    public void setOrder(final PlantOperationOrder productOrder) {
        this.order = productOrder;
    }

    /**
     * @return The product which is ordered
     */
    @ManyToOne
    public PlantOperation getOperation() {
        return this.operation;
    }

    /**
     * @param operation The product which is ordered
     */
    public void setOperation(final PlantOperation operation) {
        this.operation = operation;
    }
}