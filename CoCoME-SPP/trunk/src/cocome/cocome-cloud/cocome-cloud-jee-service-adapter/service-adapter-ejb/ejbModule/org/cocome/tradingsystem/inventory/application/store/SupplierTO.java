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
 * A transfer object class for exchanging basic supplier information between
 * client and the service-oriented application layer. It contains either copies
 * of persisted data which is transferred to the client, or data which is
 * transferred from the client to the application layer to be processed and
 * persisted.
 * 
 * @author Sebastian Herold
 * @author Lubomir Bulej
 * @author AlessandroGiusa@gmail.com
 * @version 2.0
 */
@XmlRootElement(name = "Supplier")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Supplier", propOrder = { "__id", "__name"})
public class SupplierTO implements Serializable {

	private static final long serialVersionUID = -8178666207666411060L;

	@XmlElement(name="Id")
	private long __id;

	@XmlElement(name="Name")
	private String __name;

	/**
	 * Gets the supplier identifier.
	 * 
	 * @return
	 *         supplier identifier
	 */
	public long getId() {
		return __id;
	}

	/**
	 * Sets the supplier identifier.
	 * 
	 * @param id
	 *            supplier identifier
	 */
	public void setId(final long id) {
		__id = id;
	}

	/**
	 * Get the supplier name.
	 * 
	 * @return supplier name
	 */
	public String getName() {
		return __name;
	}

	/**
	 * Sets the supplier name.
	 * 
	 * @param name
	 *            supplier name
	 */
	public void setName(final String name) {
		__name = name;
	}

}
