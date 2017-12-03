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

import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents an article provided by a specific store
 *
 * @author Rudolf Biczok
 */
@Entity
public class Item implements Serializable, QueryableById {

    private static final long serialVersionUID = -293179135307588628L;
    private long id;
    private double salesPrice;
    private Store store;
    private Product product;

    /**
     * @return A unique identifier of this StockItem.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    /**
     * @param id a unique identifier of this StockItem
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return The Product of a StockItem.
     */
    @ManyToOne
    public Product getProduct() {
        return this.product;
    }

    /**
     * @param product the Product of a StockItem
     */
    public void setProduct(final Product product) {
        this.product = product;
    }

    /**
     * @return The sales price of the StockItem
     */
    @Basic
    public double getSalesPrice() {
        return this.salesPrice;
    }

    /**
     * @param salesPrice the sales price of the StockItem
     */
    public void setSalesPrice(final double salesPrice) {
        this.salesPrice = salesPrice;
    }

    /**
     * @return The store where the StockItem belongs to
     */
    @ManyToOne
    public Store getStore() {
        return this.store;
    }

    /**
     * @param store The store where the StockItem belongs to
     */
    public void setStore(final Store store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return "[Class:" + this.getClass().getSimpleName() + ",Id:" + this.getId() + ",Store:" + this.getStore() + ",Product:" + this.getProduct() + "]";
    }

}
