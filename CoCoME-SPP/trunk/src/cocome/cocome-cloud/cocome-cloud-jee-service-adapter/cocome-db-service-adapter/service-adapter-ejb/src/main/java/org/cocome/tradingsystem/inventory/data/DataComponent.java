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

package org.cocome.tradingsystem.inventory.data;

import javax.persistence.EntityManagerFactory;

import org.cocome.tradingsystem.inventory.data.enterprise.EnterpriseQueryProvider;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.persistence.EntityPersistence;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistence;
import org.cocome.tradingsystem.inventory.data.store.IStoreQuery;
import org.cocome.tradingsystem.inventory.data.store.StoreQueryProvider;

/**
 * @author Yannick Welsch
 */
final class DataComponent implements IData {
	
	public DataComponent() {}
	public DataComponent(EntityManagerFactory emf){
		__emf = emf;
	}
	
	private EntityManagerFactory __emf = null;
	
	//not using since we get the emf per injection from the ejb-container
//	private EntityManagerFactory __emf =
//			javax.persistence.Persistence.createEntityManagerFactory(EJB_PERSISTENCE_UNIT_NAME);

	//

	public IPersistence getPersistenceManager() {
		return new EntityPersistence(__emf);
	}

	public IEnterpriseQuery getEnterpriseQuery() {
		return new EnterpriseQueryProvider();
	}

	public IStoreQuery getStoreQuery() {
		return new StoreQueryProvider();
	}

}
