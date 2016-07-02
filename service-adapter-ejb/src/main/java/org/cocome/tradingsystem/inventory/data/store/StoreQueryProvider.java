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

package org.cocome.tradingsystem.inventory.data.store;

import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.persistence.EntityPersistenceContext;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContext;

/**
 * @author Yannick Welsch
 * @author Lubomir Bulej
 */
public final class StoreQueryProvider implements IStoreQuery {

	private static final Logger LOGGER = Logger.getLogger(StoreQueryProvider.class.getName());

	//
	// Global queries
	//

	@Override
	public Store queryStore(final String name, final String location, final IPersistenceContext pctx) {
		final EntityManager em = this.getEntityManager(pctx);
		final Query query = em.createQuery("SELECT store FROM Store AS store "
				+ "WHERE store.name = ?1 AND store.location LIKE ?2");

		query.setParameter(1, name);
		query.setParameter(2, location);

		//
		// Return null if the store cannot be found
		//
		try {
			return (Store) query.getSingleResult();

		} catch (final NoResultException nre) {
			LOGGER.info(String.format(
					"There is no store matching name '%s' and location '%s'!",
					name, location));
			return null;
		}
	}

	@Override
	public Store queryStoreById(
			final long storeId, final IPersistenceContext pctx) {
		return this.getEntityById(Store.class, storeId, pctx);
	}

	@Override
	public ProductOrder queryOrderById(
			final long orderId, final IPersistenceContext pctx) {
		return this.getEntityById(ProductOrder.class, orderId, pctx);
	}

	@Override
	public StockItem queryStockItemById(
			final long stockId, final IPersistenceContext pctx) {
		return this.getEntityById(StockItem.class, stockId, pctx);
	}

	@Override
	public Product queryProduct(final edu.kit.ipd.sdq.evaluation.Barcode barcode, final IPersistenceContext pctx) {
		final EntityManager em = this.getEntityManager(pctx);
		return this.internalQueryProduct(barcode, em);
	}

	@Override
	public Product queryProductById(
			final long productId, final IPersistenceContext pctx) {
		return this.getEntityById(Product.class, productId, pctx);
	}

	//
	// Queries limited to a given store.
	//

	@Override
	public Collection<Product> queryProducts(
			final long storeId, final IPersistenceContext pctx) {
		//
		// Only select products that are in stock.
		//
		final EntityManager em = this.getEntityManager(pctx);
		final Query query = em.createQuery(
				"SELECT product FROM Product AS product " +
						"WHERE EXISTS (" +
						"SELECT stockitem FROM StockItem AS stockitem " +
						"WHERE stockitem.store = ?1 AND stockitem.product = product" +
						")");

		query.setParameter(1, this.queryStoreById(storeId, pctx));

		//
		// The following cast is safe: query result is a list of products
		//
		@SuppressWarnings("unchecked")
		final Collection<Product> result = query.getResultList();
		return result;
	}

	@Override
	public Collection<ProductOrder> queryOutstandingOrders(
			final long storeId, final IPersistenceContext pctx) {
		//
		// Only select outstanding product orders.
		//
		final EntityManager em = this.getEntityManager(pctx);
		final Query query = em.createQuery(
				"SELECT productOrder FROM ProductOrder AS productOrder " +
						"WHERE productOrder.store = ?1 AND productOrder.deliveryDate IS NULL");

		query.setParameter(1, this.queryStoreById(storeId, pctx));

		//
		// The following cast is safe: query result is a list of product orders
		//
		@SuppressWarnings("unchecked")
		final Collection<ProductOrder> result = query.getResultList();
		this.debug("%d outstanding product orders found in store %d", result.size(), storeId);
		return result;
	}

	@Override
	public Collection<StockItem> queryAllStockItems(
			final long storeId, final IPersistenceContext pctx) {
		this.debug("querying all stock items in store %d", storeId);

		final EntityManager em = this.getEntityManager(pctx);
		final Query query = em.createQuery(
				"SELECT stockItem FROM StockItem AS stockItem WHERE stockItem.store = ?1");

		query.setParameter(1, this.queryStoreById(storeId, pctx));

		//
		// The following cast is safe: query result is a list of stock items
		//
		@SuppressWarnings("unchecked")
		final Collection<StockItem> result = query.getResultList();
		this.debug("%d stock items found in store %d", result.size(), storeId);
		return result;
	}

	@Override
	public Collection<StockItem> queryLowStockItems(
			final long storeId, final IPersistenceContext pctx) {
		final EntityManager em = this.getEntityManager(pctx);
		final Query query = em.createQuery(
				"SELECT stockitem FROM StockItem AS stockitem " +
						"WHERE stockitem.store = ?1 AND stockitem.amount < stockitem.minStock");

		query.setParameter(1, this.queryStoreById(storeId, pctx));

		//
		// The following cast is safe: query result is a list of stock items
		//
		@SuppressWarnings("unchecked")
		final Collection<StockItem> result = query.getResultList();
		this.debug("%d low-stock items in store %d", result.size(), storeId);
		return result;
	}

	/**
	 * Query for a stock item identified by a barcode.
	 *
	 * @param storeId
	 *            id of the store where the item should be present
	 * @param barcode
	 *            the id identifying the item
	 * @param pctx
	 *            the persistence context
	 *
	 * @return returns one stock item when such item exists, else null
	 */
	@Override
	public StockItem queryStockItem(final long storeId, final edu.kit.ipd.sdq.evaluation.Barcode barcode, final IPersistenceContext pctx) {
		final EntityManager em = this.getEntityManager(pctx);

		final Product product = this.internalQueryProduct(barcode, em);
		if (product != null) {
			final Store store = this.queryStoreById(storeId, pctx);
			return this.internalQueryStockItem(store, product, em);

		} else
			return null;
	}

	private Product internalQueryProduct(final edu.kit.ipd.sdq.evaluation.Barcode barcode, final EntityManager em) {
		this.debug("looking for product barcode %d", barcode);
		final Query query = em.createQuery("SELECT product FROM Product AS product WHERE product.barcode = ?1");

		query.setParameter(1, barcode);

		//
		// Return null if the product cannot be found
		//
		try {
			return (Product) query.getSingleResult();

		} catch (final NoResultException nre) {
			this.debug("there is no product with barcode %d", barcode);
			return null;
		}
	}

	private StockItem internalQueryStockItem(final Store store, final Product product, final EntityManager em) {
		this.debug(
				"looking for stock item with product barcode %d in %s, id %d",
				product.getBarcode(), store.getName(), store.getId());

		final Query query = em.createQuery("SELECT stockItem FROM StockItem AS stockItem "
				+ "WHERE stockItem.store = ?1 AND stockItem.product = ?2");

		query.setParameter(1, store);
		query.setParameter(2, product);

		//
		// Return null if the stock item cannot be found
		//
		try {
			return (StockItem) query.getSingleResult();

		} catch (final NoResultException nre) {
			this.debug(
					"no stock item with product barcode %d in %s, id %d",
					product.getBarcode(), store.getName(), store.getId());
			return null;
		}
	}

	@Override
	public Collection<StockItem> queryStockItemsByProductId(
			final long storeId, final long[] productIds, final IPersistenceContext pctx) {
		//
		// Build query for the given product identifiers.
		// This requires non-empty productIds array.
		//
		if (productIds.length < 1)
			return Collections.emptyList();

		final EntityManager em = this.getEntityManager(pctx);

		final Query query = em.createQuery(String.format(
				"SELECT stockitem FROM StockItem AS stockitem WHERE stockitem.store = ?1 AND (%s)",
				this.buildProductsExpression(productIds)));

		query.setParameter(1, this.queryStoreById(storeId, pctx));

		//
		// The following cast is safe: query result is a list of stock items
		//
		@SuppressWarnings("unchecked")
		final Collection<StockItem> result = query.getResultList();

		this.debug("%d stock items found in store %d", result.size(), storeId);
		for (final StockItem stockItem : result) {
			this.debug("\tproduct %d, amount %d, min stock %d",
					stockItem.getProduct().getBarcode(),
					stockItem.getAmount(), stockItem.getMinStock());
		}
		return result;
	}

	private String buildProductsExpression(final long[] productIds) {
		final StringBuilder result = new StringBuilder();

		String logicalOr = "";
		for (final long productId : productIds) {
			result.append(logicalOr);
			result.append("stockitem.product.id = ");
			result.append(productId);

			logicalOr = " OR ";
		}

		return result.toString();
	}

	//

	private <E> E getEntityById(final Class<E> entityClass, final long entityId, final IPersistenceContext pctx) {
		// throws EntityNotFound exception if the entity could not be found
		final EntityManager em = this.getEntityManager(pctx);
		return em.getReference(entityClass, entityId);
	}

	// TODO This is a broken design. Either the store requires an entity manager
	// than it should get one by parameter or it should not use this method at all.
	// The remaining code suggests that indeed the entitymanager is required.

	private EntityManager getEntityManager(final IPersistenceContext pctx) {
		// XXX There should be no need to escape the PersistenceContext interface
		return ((EntityPersistenceContext) pctx).getEntityManager();
	}

	//

	private void debug(final String format, final Object... args) {
		final Level level = Level.INFO;
		if (LOGGER.getLevel() == Level.INFO) {
			LOGGER.log(level, String.format(format, args));
		}
	}

}
