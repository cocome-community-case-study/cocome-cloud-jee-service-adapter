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

import org.cocome.tradingsystem.inventory.data.enterprise.Product;

/**
 * A transfer object class for exchanging basic product information between
 * client and the service-oriented application layer. It contains either copies
 * of persisted data which are transferred to the client, or data which is
 * transferred from the client to the application layer to be processed and
 * persisted.
 * 
 * @author Sebastian Herold
 * @author Lubomir Bulej
 * @author AlessandroGiusa@gmail.com
 * @version 2.0
 */
@XmlRootElement(name = "Product")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Product", propOrder = { "__id", "__barcode","__purchasePrice","__name"})
public class ProductTO implements Serializable {

	private static final long serialVersionUID = -4566375052296560384L;

	@XmlElement(name="Id")
	private long __id;
	@XmlElement(name="Barcode")
	private Barcode __barcode;
	@XmlElement(name="PurchasePrice")
	private double __purchasePrice;
	@XmlElement(name="Name")
	private String __name;

	/**
	 * Gets id.
	 * 
	 * @return
	 *         The identifier.
	 */
	public long getId() {
		return __id;
	}

	/**
	 * sets the id.
	 */
	public void setId(final long id) {
		__id = id;
	}

	/**
	 * Gets barcode value.
	 * 
	 * @return
	 *         Saved barcode value.
	 */
	public Barcode getBarcode() {
		return __barcode;
	}

	/**
	 * Sets barcode value.
	 * 
	 * @param barcode
	 */
	public void setBarcode(final Barcode barcode) {
		__barcode = barcode;
	}

	/**
	 * Gets name of the product.
	 * 
	 * @return
	 *         Name of product.
	 */
	public String getName() {
		return __name;
	}

	/**
	 * Sets name of product.
	 * 
	 * @param name
	 *            New name.
	 */
	public void setName(final String name) {
		__name = name;
	}

	/**
	 * Gets purchase price of product.
	 * 
	 * @return
	 *         Saved purchase price.
	 */
	public double getPurchasePrice() {
		return __purchasePrice;
	}

	/**
	 * Sets purchase price of product.
	 * 
	 * @param purchasePrice
	 *            Purchase price to e set.
	 */
	public void setPurchasePrice(final double purchasePrice) {
		__purchasePrice = purchasePrice;
	}

	/**
	 * Checks equality to a product entity.
	 * <p>
	 * Required for UC 8 (see {@link AmplCplexSolver}).
	 */
	public boolean equalsProduct(final Product that) {
		if (that == null) {
			return false;
		}

		return this.getBarcode() == that.getBarcode()
				&& this.getId() == that.getId()
				&& this.getName().equals(that.getName())
				&& this.getPurchasePrice() == that.getPurchasePrice();
	}

}
