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
 ***************************************************************************
 */

package org.cocome.tradingsystem.inventory.data.store;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * Represents a concrete product in the, store including sales price,
 * amount, ...
 *
 * @author Yannick Welsch
 */
@Entity
public class StockItem extends Item {

    private static final long serialVersionUID = -293179135307588628L;

    private long amount;
    private long minStock;
    private long maxStock;
    private long incomingAmount;

    /**
     * @return The currently available amount of items of a product
     */
    @Basic
    public long getAmount() {
        return this.amount;
    }

    /**
     * @param amount the currently available amount of items of a product
     */
    public void setAmount(final long amount) {
        this.amount = amount;
    }

    /**
     * This method will be used while computing the low stock item list.
     *
     * @return The maximum capacity of a product in a store.
     */
    @Basic
    public long getMaxStock() {
        return this.maxStock;
    }

    /**
     * This method enables the definition of the maximum capacity of a product
     * in a store.
     *
     * @param maxStock the maximum capacity of a product in a store
     */
    public void setMaxStock(final long maxStock) {
        this.maxStock = maxStock;
    }

    /**
     * @return The minimum amount of products which has to be available in a
     * store.
     */
    @Basic
    public long getMinStock() {
        return this.minStock;
    }

    /**
     * @param minStock the minimum amount of products which has to be available in a
     *                 store
     */
    public void setMinStock(final long minStock) {
        this.minStock = minStock;
    }

    /**
     * Required for UC 8
     *
     * @return incomingAmount
     * the amount of products that will be delivered in the near future
     */
    @Basic
    public long getIncomingAmount() {
        return this.incomingAmount;
    }

    /**
     * Set the amount of products that will be delivered in the near future.
     * <p>
     * Required for UC 8
     *
     * @param incomingAmount the absolute amount (no delta) of incoming products
     */
    public void setIncomingAmount(final long incomingAmount) {
        this.incomingAmount = incomingAmount;
    }
}
