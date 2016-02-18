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

package org.cocome.tradingsystem.inventory.application.reporting;

import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.persistence.EntityNotFoundException;

import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContext;
import org.cocome.tradingsystem.inventory.data.persistence.ITransactionContext;
import org.cocome.tradingsystem.inventory.data.store.Store;

/**
 * Provides methods for generating enterprise reports.
 * 
 * @author Sebastian Herold
 * @author Lubomir Bulej
 */
public interface IReporting extends Remote {

	/**
	 * Generates report of available stock for all products in the specified
	 * store.
	 * 
	 * @param storeId
	 *            the store entity identifier for which to generate report
	 * @return
	 *         Report transfer object containing store stock information.
	 * @throws EntityNotFoundException
	 *             if the given store does not exist
	 */
	public ReportTO getStoreStockReport(long storeId) throws RemoteException;

	
	/**
	 * Generates report of available stock for all products in the specified
	 * store without using the transaction mechanism implemented by {@link ITransactionContext}.
	 * @param store
	 * @param pctx
	 * @return
	 * @throws RemoteException
	 */
	public ReportTO getStoreStockReport(Store store, IPersistenceContext pctx) throws RemoteException;
	
	
	/**
	 * Generates report of available stock for all products in all stores
	 * of the specified enterprise.
	 * 
	 * @param enterpriseId
	 *            the enterprise entity identifier for which to generate report
	 * @return
	 *         Report transfer object containing enterprise stock information.
	 * @throws EntityNotFoundException
	 *             if the given enterprise does not exist
	 */
	public ReportTO getEnterpriseStockReport(long enterpriseId)
			throws RemoteException;
	
	/**
	 * Generates report of available stock for all products in all stores
	 * of the specified enterprise without using the transaction mechanism specified by {@link ITransactionContext}
	 * 
	 * @param enterpriseId
	 *            the enterprise entity identifier for which to generate report
	 * @return
	 *         Report transfer object containing enterprise stock information.
	 * @throws EntityNotFoundException
	 *             if the given enterprise does not exist
	 */
	ReportTO getEnterpriseStockReport(TradingEnterprise enterprise, IPersistenceContext pctx)
			throws RemoteException;

	/**
	 * Generates report of mean time to delivery for each supplier of the
	 * specified enterprise.
	 * 
	 * @param enterpriseId
	 *            the enterprise entity identifier for which to generate report
	 * @return
	 *         Report transfer object containing mean time to delivery
	 *         information.
	 * @throws EntityNotFoundException
	 *             if the given enterprise does not exist
	 */
	public ReportTO getEnterpriseDeliveryReport(long enterpriseId)
			throws RemoteException;
	
	
	/**
	 * Used for implementation of UC 6:ShowDeliveryReports, without using
	 * transaction mechanism specified by {@link ITransactionContext}. Since
	 * JavaEE-Server is using JTA.
	 * 
	 * @param enterprise
	 * @param pctx
	 * @return
	 */
	ReportTO getEnterpriseDeliveryReport(TradingEnterprise enterprise, IPersistenceContext pctx) throws RemoteException;

}
