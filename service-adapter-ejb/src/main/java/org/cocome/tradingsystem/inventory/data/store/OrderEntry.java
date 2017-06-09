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

package org.cocome.tradingsystem.inventory.data.store;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.cocome.tradingsystem.inventory.data.enterprise.Product;

/**
 * Represents a single product order entry in the database.
 *
 * @author Yannick Welsch
 */
@Entity
public class OrderEntry implements Serializable {

	private static final long serialVersionUID = -7683436740437770058L;

	private long id;

	private long amount;

	private Product product;

	private ProductOrder order;

	/** Empty constructor. */
	public OrderEntry() {}

	/**
	 * Gets identifier value
	 *
	 * @return The id.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return this.id;
	}

	/**
	 * Sets identifier.
	 *
	 * @param id
	 *            Identifier value.
	 */
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
	 * @param amount
	 *            The amount of ordered products
	 */
	public void setAmount(final long amount) {
		this.amount = amount;
	}

	/**
	 * @return The ProductOrder where the OrderEntry belongs to
	 */
	@ManyToOne
	public ProductOrder getOrder() {
		return this.order;
	}

	/**
	 * @param productOrder
	 *            The ProductOrder where the OrderEntry belongs to
	 */
	public void setOrder(final ProductOrder productOrder) {
		this.order = productOrder;
	}

	/**
	 * @return The product which is ordered
	 */
	@ManyToOne
	public Product getProduct() {
		return this.product;
	}

	/**
	 * @param product
	 *            The product which is ordered
	 */
	public void setProduct(final Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "[Class:" + this.getClass().getSimpleName() + ",Id" + this.getId()
				+ ",Product:" + this.product + ",ProductOrder:" + this.order + "]";
	}

}
