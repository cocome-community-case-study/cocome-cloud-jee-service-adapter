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

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.store.Store;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * The class represents an order of a {@link PlantOperation} in the database.
 *
 * @author Rudolf Biczok
 */
@Entity
public class RecipeOperationOrder implements Serializable, QueryableById {

    private static final long serialVersionUID = -8340585715760459030L;

    private long id;
    private Date deliveryDate;
    private Date orderingDate;
    private TradingEnterprise enterprise;
    private boolean finished;

    /**
     * @return A unique identifier for ProductOrder objects
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    /**
     * @param id A unique identifier for ProductOrder objects
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return The date of ordering.
     */
    @NotNull
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getOrderingDate() {
        return this.orderingDate;
    }

    /**
     * @param orderingDate the date of ordering
     */
    public void setOrderingDate(final Date orderingDate) {
        this.orderingDate = orderingDate;
    }

    /**
     * The delivery date is used for computing the mean time to delivery
     *
     * @return The date of order fulfillment.
     */
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDeliveryDate() {
        return this.deliveryDate;
    }

    /**
     * The delivery date is used for computing the mean time to delivery
     *
     * @param deliveryDate the date of order fulfillment
     */
    public void setDeliveryDate(final Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @NotNull
    @ManyToOne
    public TradingEnterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(TradingEnterprise enterprise) {
        this.enterprise = enterprise;
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