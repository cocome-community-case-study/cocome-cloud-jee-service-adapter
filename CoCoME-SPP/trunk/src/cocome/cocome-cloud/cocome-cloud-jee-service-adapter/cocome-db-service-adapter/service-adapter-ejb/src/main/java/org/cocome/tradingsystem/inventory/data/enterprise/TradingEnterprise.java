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

package org.cocome.tradingsystem.inventory.data.enterprise;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.cocome.tradingsystem.inventory.data.store.Store;

/**
 * Represents a TradingEnterprise in the database.
 * 
 * @author Yannick Welsch
 */
@Entity
@XmlRootElement(name = "Enterprise")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Enterprise", propOrder = {"name","stores"})
public class TradingEnterprise implements Serializable{

	private static final long serialVersionUID = 1L;

	@XmlTransient
	private long id;

	@XmlElement(name="Name")
	private String name;

	@XmlTransient
	private Collection<ProductSupplier> suppliers;

	@XmlElementWrapper(name="Stores")
	@XmlElement(name="Store")
	private Collection<Store> stores;

	/**
	 * @return id a unique identifier of this TradingEnterprise
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            a unique identifier of this TradingEnterprise
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return Name of this TradingEnterprise
	 */
	@Basic
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            Name of this TradingEnterprise
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Collection of Stores related to the TradingEnterprise
	 */
	@OneToMany(mappedBy = "enterprise", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public Collection<Store> getStores() {
		return stores;
	}

	/**
	 * @param stores
	 *            Collection of Stores related to the TradingEnterprise
	 */
	public void setStores(Collection<Store> stores) {
		this.stores = stores;
	}

	/**
	 * @return Collection of Suppliers related to the TradingEnterprise
	 */
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public Collection<ProductSupplier> getSuppliers() {
		return suppliers;
	}

	/**
	 * @param suppliers
	 *            Collection of Suppliers related to the TradingEnterprise
	 */
	public void setSuppliers(Collection<ProductSupplier> suppliers) {
		this.suppliers = suppliers;
	}
	
	@Override
	public String toString() {
		return "[Class:" + getClass().getSimpleName() + ",Id" + getId()
				+ ",Name:" + getName() + "]";
	}

}
