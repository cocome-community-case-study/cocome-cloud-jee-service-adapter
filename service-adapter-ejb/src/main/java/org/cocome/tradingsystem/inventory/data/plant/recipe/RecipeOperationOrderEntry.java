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

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a single {@link PlantOperationOrder} entry in the database.
 *
 * @author Rudolf Biczok
 */
@Entity
public class RecipeOperationOrderEntry implements Serializable, QueryableById {

    private static final long serialVersionUID = -7683436740437770058L;

    private long id;
    private long amount;
    private boolean finished;

    /**
     * Gets identifier value
     *
     * @return The id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    /**
     * Sets identifier.
     *
     * @param id Identifier value.
     */
    @Override
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return The amount of ordered products
     */
    @Basic
    public long getAmount() {
        return this.amount;
    }

    /**
     * @param amount The amount of ordered products
     */
    public void setAmount(final long amount) {
        this.amount = amount;
    }


    /**
     * @return {@code true} if order has been finished
     */
    @Basic
    public boolean isFinished() {
        return finished;
    }

    /**
     * @param finished a flag indicating of the order has finished or not
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
