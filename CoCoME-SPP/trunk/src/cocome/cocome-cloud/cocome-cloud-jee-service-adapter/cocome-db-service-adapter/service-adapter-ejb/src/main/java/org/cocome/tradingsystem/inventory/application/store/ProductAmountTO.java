/***************************************************************************
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
 ***************************************************************************/

package org.cocome.tradingsystem.inventory.application.store;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A transfer object class holding product information along with amount.
 * <p>
 * Required for UC 8.
 * 
 * @author kelsaka
 * @author Lubomir Bulej
 * @author AlessandroGiusa@gmail.com
 * @version 2.0
 */
@XmlRootElement(name = "ProductAmount")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductAmount", propOrder = { "__product", "__amount"})
public class ProductAmountTO implements Serializable {

	private static final long serialVersionUID = -6862103034045903860L;
	
	@XmlElement(name="Product")
	private ProductTO __product;
	
	@XmlElement(name="Amount")
	private long __amount;

	/**
	 * Initializes the product amount to zero.
	 */
	public ProductAmountTO() {
		__amount = 0;
	}

	/**
	 * Product
	 * 
	 * @return
	 *         Product transfer object.
	 */
	public ProductTO getProduct() {
		return __product;
	}

	/**
	 * Product
	 * 
	 * @param product
	 *            a product to set
	 */
	public void setProduct(final ProductTO product) {
		__product = product;
	}

	/**
	 * Returns the amount of product.
	 * 
	 * @return
	 *         Product amount.
	 */
	public long getAmount() {
		return __amount;
	}

	/**
	 * Sets the amount of product.
	 * 
	 * @param amount
	 *            new amount
	 */
	public void setAmount(final long amount) {
		__amount = amount;
	}

}
