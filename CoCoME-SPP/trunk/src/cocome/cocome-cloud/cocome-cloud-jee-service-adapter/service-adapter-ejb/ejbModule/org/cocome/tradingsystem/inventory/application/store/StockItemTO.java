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

import org.cocome.tradingsystem.inventory.data.store.StockItem;

/**
 * A transfer object class for exchanging basic stock item information between
 * client and the service-oriented application layer. It contains either copies
 * of persisted data which is transferred to the client, or data which is
 * transferred from the client to the application layer to be processed and
 * persisted.
 * 
 * @see StockItem
 * 
 * @author Sebastian Herold
 * @author Lubomir Bulej
 * @author AlessandroGiusa@gmail.com
 * @version 2.0
 */
@XmlRootElement(name = "StockItem")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StockItem", propOrder = { "__id", "__salesPrice", "__amount",
	"__minStock", "__maxStock" })
public class StockItemTO implements Serializable {

	private static final long serialVersionUID = 5874806761123366899L;

	@XmlElement(name = "Id")
	private long id;

	@XmlElement(name = "SalesPrice")
	private double salesPrice;

	@XmlElement(name = "Amount")
	private long amount;

	@XmlElement(name = "MinStock")
	private long minStock;

	@XmlElement(name = "MaxStock")
	private long maxStock;

	/**
	 * Returns the unique identifier of the {@link StockItem} entity.
	 * 
	 * @return
	 *         {@link StockItem} entity identifier.
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Sets the unique identifier of the {@link StockItem} entity.
	 * 
	 * @param id
	 *            new {@link StockItem} entity identifier
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * Returns the stock amount.
	 * 
	 * @return
	 *         Stock amount.
	 */
	public long getAmount() {
		return this.amount;
	}

	/**
	 * Sets the stock amount.
	 * 
	 * @param amount
	 *            new stock amount
	 */
	public void setAmount(final long amount) {
		this.amount = amount;
	}

	/**
	 * Returns the minimal stock amount for this stock item.
	 * 
	 * @return
	 *         Minimum stock amount.
	 */
	public long getMinStock() {
		return this.minStock;
	}

	/**
	 * Sets the minimal stock amount for this stock item.
	 * 
	 * @param minStock
	 *            new minimal stock amount
	 */
	public void setMinStock(final long minStock) {
		this.minStock = minStock;
	}

	/**
	 * Returns the maximal stock amount for this stock item.
	 * 
	 * @return
	 *         Maximum stock amount.
	 */
	public long getMaxStock() {
		return this.maxStock;
	}

	/**
	 * Sets the maximal stock amount for this stock item.
	 * 
	 * @param maxStock
	 *            new maximal stock amount
	 */
	public void setMaxStock(final long maxStock) {
		this.maxStock = maxStock;
	}

	/**
	 * Returns the sales price of a stock item product.
	 * 
	 * @return
	 *         Stock item product sales price.
	 */
	public double getSalesPrice() {
		return this.salesPrice;
	}

	/**
	 * Sets the sales price of a stock item product.
	 * 
	 * @param salesPrice
	 *            new stock item product sales price
	 */
	public void setSalesPrice(final double salesPrice) {
		this.salesPrice = salesPrice;
	}

}
