/*
 ****************************************************************************
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

package org.cocome.tradingsystem.inventory.data.enterprise;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * This class represents a Product in the database
 * 
 * @author Yannick Welsch
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "barcode" }))
public class Product extends AbstractProduct {

	private static final long serialVersionUID = -2577328715744776645L;

	private ProductSupplier supplier;

	/**
	 * @return The ProductSupplier of this product
	 */
	@ManyToOne
	public ProductSupplier getSupplier() {
		return supplier;
	}

	/**
	 * @param supplier
	 *            The ProductSupplier of this product
	 */
	public void setSupplier(ProductSupplier supplier) {
		this.supplier = supplier;
	}

	@Override
	public String toString() {
		return "[Class:" + getClass().getSimpleName() + ",Barcode:"
				+ this.getBarcode() + ",Name:" + this.getName() + ",Supplier:"
				+ this.getSupplier() + "]";
	}

}
