package org.cocome.tradingsystem.remote.access;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;


import org.cocome.tradingsystem.inventory.data.DataFactory;
import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.enterprise.ProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistence;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContext;
import org.cocome.tradingsystem.inventory.data.store.OrderEntry;
import org.cocome.tradingsystem.inventory.data.store.ProductOrder;
import org.cocome.tradingsystem.inventory.data.store.StockItem;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.usermanager.Customer;
import org.cocome.tradingsystem.usermanager.LoginUser;

@Stateless(name = DatabaseAccess.BEAN_NAME, mappedName = DatabaseAccess.JDNI_NAMING,
		description = "Bean to provide access on the database")
public class DatabaseAccessBean implements DatabaseAccess {

	// **********************************************************************
	// * FIELDS *
	// **********************************************************************

	@PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
	private EntityManagerFactory emf;

	// **********************************************************************
	// * PUBLIC *
	// **********************************************************************

	/**
	 * Create the given {@link TradingEnterprise} in database
	 */
	@Override
	public Notification createEnterprises(final List<TradingEnterprise> enterprises)
			throws IllegalArgumentException {
		final Notification notification = new Notification();
		if (enterprises != null) {
			final EntityManager em = this.emf.createEntityManager();

			for(final TradingEnterprise enterprise : enterprises) {
				final TradingEnterprise tmpEnterprise = this._queryEnterprise(em, enterprise);
				if (tmpEnterprise != null) {
					notification.addNotification(
							"createEnterprise", Notification.FAILED,
							"Enterprise already available:" + enterprise);
					return notification;
				}

				final TradingEnterprise _enterprise = new TradingEnterprise();
				_enterprise.setName(enterprise.getName());
				this._persist(enterprise);
				notification.addNotification(
						"createEnterprise", Notification.SUCCESS,
						"Enterprise creation:" + _enterprise);
			}
            em.flush();
			em.close();
			return notification;
		}
		throw new IllegalAccessError("[createEnterprie]argument is null");
	}

	@Override
	public Notification createStore(final List<Store> stores)
			throws IllegalArgumentException {
		final Notification notification = new Notification();
		if (stores != null) {
			final EntityManager em = this.emf.createEntityManager();
			TradingEnterprise _enterprise;
			for (final Store nextStore : stores) {
				_enterprise = this._queryEnterprise(em, nextStore.getEnterprise());
				if (_enterprise == null) {
					notification.addNotification(
							"createStore", Notification.FAILED,
							"Creation Store failed, Enterprise not available:"
									+ nextStore.getEnterprise() + "," + nextStore);
					continue;
				}
				final Store store = new Store();
				store.setName(nextStore.getName());
				store.setLocation(nextStore.getLocation());
				store.setEnterprise(_enterprise);
				_enterprise.getStores().add(store);
				em.persist(store);
				em.merge(_enterprise);
				notification.addNotification(
						"createStore", Notification.SUCCESS,
						"Creation Store:" + store);
			}
			em.flush();
			em.close();
			return notification;
		}
		throw new IllegalArgumentException("[createStore]given arguments are null");
	}

	@Override
	public Notification createProducts(final List<Product> products)
			throws IllegalArgumentException {
		final EntityManager em = this.emf.createEntityManager();
		final Notification notification = new Notification();

		for (final Product nextProduct : products) {
			if (this._queryProduct(em, nextProduct) == null) {
				em.persist(nextProduct);
				notification.addNotification(
						"createProducts", Notification.SUCCESS,
						"Creation product:" + nextProduct);
			} else {
				notification.addNotification(
						"createProducts", Notification.FAILED,
						"Product already available:" + nextProduct);
			}
		}
		return notification;
	}

	@Override
	public Notification createStockItem(final List<StockItem> stockitems) {
		final EntityManager em = this.emf.createEntityManager();
		final Notification notification = new Notification();

		Store _store;
		Product _product;

		for (final StockItem nextStockItem : stockitems) {
			// query product
			_product = this._queryProduct(em, nextStockItem.getProduct());
			if (_product == null) {
				notification.addNotification(
						"createStockItem", Notification.FAILED,
						"Product not available:" + nextStockItem.getProduct());
				continue;
			}
			// query store
			_store = this._queryStoreById(em, nextStockItem.getStore());
			if (_store == null) {
				notification.addNotification(
						"createStockItem", Notification.FAILED,
						"Store not available:" + nextStockItem.getStore());
				continue;
			}
			// ensure store can save items
			if (_store.getStockItems() == null) {
				_store.setStockItems(new ArrayList<>());
			}
			// update object with actual database objects
			nextStockItem.setStore(_store);
			nextStockItem.setProduct(_product);
			// persist
			_store.getStockItems().add(nextStockItem);
			em.persist(nextStockItem);
			em.merge(_store.getEnterprise());
			em.merge(_store);
			em.merge(_product);
			notification.addNotification(
					"createStockItem", Notification.SUCCESS,
					"Creation StockItem:" + nextStockItem);
		}
		em.flush();
		em.close();
		return notification;
	}

	@Override
	public Notification createProductSupplier(final List<ProductSupplier> productSuppliers)
			throws IllegalArgumentException {
		final EntityManager em = this.emf.createEntityManager();
		final Notification notification = new Notification();

		for (final ProductSupplier nextProSupp : productSuppliers) {
			if (this._queryProductSupplier(em, nextProSupp) == null) {
				em.persist(nextProSupp);
				notification.addNotification(
						"createProductSupplier", Notification.SUCCESS,
						"Creation ProductSupplier:" + nextProSupp);
			} else {
				notification.addNotification(
						"createProductSupplier", Notification.FAILED,
						"ProductSupplier already available:" + nextProSupp);
			}
		}
		return notification;
	}

	@Override
	public Notification createProductOrder(final List<ProductOrder> orders) {
		final EntityManager em = this.emf.createEntityManager();
		final Notification notification = new Notification();
		Store _store;
		Product _product;
		ProductOrder _productOrder;
		for (final ProductOrder nextOrder : orders) {

			_store = this._queryStoreById(em, nextOrder.getStore());
			if (_store == null) {
				notification.addNotification(
						"createProductOrder", Notification.FAILED,
						"Store not available:" + nextOrder.getStore());
				continue;
			}

			_productOrder = new ProductOrder();
			_productOrder.setOrderEntries(new ArrayList<>());
			_productOrder.setStore(_store);
			_productOrder.setDeliveryDate(nextOrder.getDeliveryDate());
			_productOrder.setOrderingDate(nextOrder.getOrderingDate());
			// set the store of database

			// search for the right product
			for (final OrderEntry nextOrderEntry : nextOrder.getOrderEntries()) {
				// query product
				_product = this._queryProduct(em, nextOrderEntry.getProduct());
				if (_product == null) {
					System.out.println(
							"Product missing:" + nextOrderEntry.getProduct());
					notification.addNotification(
							"createProductOrder", Notification.FAILED,
							"Product not available:" + nextOrderEntry.getProduct());
					continue;
				}
				final OrderEntry _orderEntery = new OrderEntry();
				_orderEntery.setProduct(_product);
				_orderEntery.setAmount(nextOrderEntry.getAmount());
				_orderEntery.setOrder(_productOrder);
				_productOrder.getOrderEntries().add(_orderEntery);
				// set the product of database
			}
			// persist order
			em.persist(_productOrder);
			notification.addNotification(
					"createProductOrder", Notification.SUCCESS,
					"Creation ProductOrder:" + nextOrder);
		}
		em.flush();
		em.close();
		return notification;
	}

	@Override
	public Notification updateEnterprises(final List<TradingEnterprise> list)
			throws IllegalArgumentException {
		final EntityManager em = this.emf.createEntityManager();
		final Notification notification = new Notification();

		TradingEnterprise _enterprise;
		for (final TradingEnterprise nextEnterprise : list) {
			_enterprise = this._queryEnterpriseById(em, nextEnterprise);
			if (_enterprise == null) {
				notification.addNotification(
						"updateEnterprise", Notification.FAILED,
						"Enterprise not available:" + nextEnterprise);
				continue;
			}
			_enterprise.setName(nextEnterprise.getName());
			em.merge(_enterprise);
			notification.addNotification(
					"updateEnterprise", Notification.SUCCESS,
					"Update Enterprise:" + nextEnterprise);
		}
		em.flush();
		em.close();
		return notification;
	}

	@Override
	public Notification updateProductSupplier(final List<ProductSupplier> list)
			throws IllegalArgumentException {
		final EntityManager em = this.emf.createEntityManager();
		final Notification notification = new Notification();

		ProductSupplier _supplier;
		for (final ProductSupplier nextSupplier : list) {
			_supplier = this._queryProductSupplierById(em, nextSupplier);
			if (_supplier == null) {
				notification.addNotification(
						"updateProductSupplier", Notification.FAILED,
						"ProductSupplier not available:" + nextSupplier);
				continue;
			}
			_supplier.setName(nextSupplier.getName());
			em.merge(_supplier);
			notification.addNotification(
					"updateProductSupplier", Notification.SUCCESS,
					"Update ProductSupplier:" + nextSupplier);
		}
		em.flush();
		em.close();
		return notification;
	}

	@Override
	public Notification updateProductOrder(final List<ProductOrder> list)
			throws IllegalArgumentException {
		final EntityManager em = this.emf.createEntityManager();
		final Notification notification = new Notification();

		ProductOrder _order;
		for (final ProductOrder nextOrder : list) {
			_order = this._queryProductOrderById(em, nextOrder);
			if (_order == null) {
				notification.addNotification(
						"updateProductOrder", Notification.FAILED,
						"ProductOrder not available:" + nextOrder);
				continue;
			}

			// update
			if (nextOrder.getDeliveryDate() != null) {
				_order.setDeliveryDate(nextOrder.getDeliveryDate());
			}

			if (nextOrder.getOrderingDate() != null) {
				_order.setOrderingDate(nextOrder.getOrderingDate());
			}

			em.merge(_order);
			notification.addNotification(
					"updateProductOrder", Notification.SUCCESS,
					"Update ProductOrder:" + nextOrder);
		}
		em.flush();
		em.close();
		return notification;
	}

	@Override
	public Notification updateProducts(final List<Product> products)
			throws IllegalArgumentException {
		final EntityManager em = this.emf.createEntityManager();
		final Notification notification = new Notification();
		Product _pro;
		ProductSupplier _proSupp;
		double pprice;
		for (final Product nextProduct : products) {
			// query product
			_pro = this._queryProduct(em, nextProduct);
			if (_pro != null) {
				// update purchase price
				pprice = _pro.getPurchasePrice();
				_pro.setPurchasePrice(nextProduct.getPurchasePrice());
				notification.addNotification(
						"updateProduct", Notification.SUCCESS,
						"Update PurchasePrice:From" + pprice + "->TO:" + nextProduct.getPurchasePrice());
				// update product supplier
				if (nextProduct.getSupplier() != null) {
					_proSupp = this._queryProductSupplier(em, nextProduct.getSupplier());
					if (_proSupp != null) {
						_pro.setSupplier(_proSupp);
						notification.addNotification(
								"updateProduct", Notification.SUCCESS,
								"Update ProductSupplier:" + _proSupp);
					} else {
						notification.addNotification(
								"updateProduct", Notification.FAILED,
								"ProductSupplier: <null>");
					}
				} else {
					notification.addNotification(
							"updateProduct", Notification.WARNING,
							"ProductSupplier not provided for:" + nextProduct);
				}
				// refresh database object
				em.merge(_pro);
			} else {
				notification.addNotification(
						"updateProducts", Notification.FAILED,
						"Product not available:" + nextProduct);
			}
		}
		em.flush();
		em.close();
		return notification;
	}

	@Override
	public Notification updateStore(final List<Store> stores)
			throws IllegalArgumentException {
		final EntityManager em = this.emf.createEntityManager();
		final Notification notification = new Notification();
		Store _store;
		for (final Store nextStore : stores) {
			_store = this._queryStoreById(em,  nextStore);
			if (_store == null) {
				notification.addNotification(
						"updateStore", Notification.FAILED,
						"Store not available:" + nextStore);
				continue;
			}
			// update location
			_store.setLocation(nextStore.getLocation());
			// update name
			_store.setName(nextStore.getName());

			em.merge(_store);
			notification.addNotification(
					"updateStore", Notification.SUCCESS,
					"Update Store:" + _store);
		}
		em.flush();
		em.close();
		return notification;
	}

	@Override
	public Notification updateStockItems(final List<StockItem> stockitems) {
		final EntityManager em = this.emf.createEntityManager();
		final Notification notification = new Notification();
		StockItem _stockItem = null;
		Store _store;

		for (final StockItem nextStockItem : stockitems) {
			// query store
			_store = this._queryStoreById(em, nextStockItem.getStore());
			if (_store == null) {
				notification.addNotification(
						"updateStockItems", Notification.FAILED,
						"Store not available:" + nextStockItem.getStore());
				continue;
			}

			// query stock item
			for (final StockItem _storeNextStockItem : _store.getStockItems()) {
				if (_storeNextStockItem.getProduct().getBarcode()
				== nextStockItem.getProduct().getBarcode()) {
					_stockItem = _storeNextStockItem;
					break;
				}
			}

			// update stock item
			if (_stockItem == null) {
				notification.addNotification(
						"updateStockItems", Notification.FAILED,
						"StockItem not available:" + nextStockItem);
				continue;
			}

			_stockItem.setAmount(nextStockItem.getAmount());
			_stockItem.setIncomingAmount(nextStockItem.getIncomingAmount());
			_stockItem.setMaxStock(nextStockItem.getMaxStock());
			_stockItem.setMinStock(nextStockItem.getMinStock());
			_stockItem.setSalesPrice(nextStockItem.getSalesPrice());

			em.merge(_stockItem);
			notification.addNotification(
					"updateStockItems", Notification.SUCCESS,
					"Update StockItem:" + _stockItem);
		}
		em.flush();
		em.close();
		return notification;
	}

	/**
	 * Trigger a book sale.
	 */
	@Override
	public Notification bookSale(final Object o) throws IllegalArgumentException {
		// TODO Implement this.
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> query(final String query) throws IllegalArgumentException {
		if ((query != null) && !query.isEmpty()) {
			return this.emf.createEntityManager().createQuery(query).getResultList();
		} else {
			throw new IllegalArgumentException("[query]given arguments are null");
		}
	}

	// **********************************************************************
	// * PRIVATE *
	// **********************************************************************

	private TradingEnterprise _queryEnterprise(final EntityManager em, final TradingEnterprise enterprise) {
        return _querySingleInstance(em.createQuery(
				"SELECT te FROM TradingEnterprise te WHERE te.name LIKE :teName",
				TradingEnterprise.class).setParameter("teName", enterprise.getName()));
	}

	private TradingEnterprise _queryEnterpriseById(final EntityManager em, final TradingEnterprise enterprise) {
        return _querySingleInstance(em.createQuery(
				"SELECT te FROM TradingEnterprise te WHERE te.id = :teId",
				TradingEnterprise.class).setParameter("teId", enterprise.getId()));
	}

	private Store _queryStoreById(final EntityManager em, final Store store) {
        return _querySingleInstance(em.createQuery(
				"SELECT s FROM Store s WHERE s.id=:sId",
				Store.class).setParameter("sId", store.getId()));
	}

	private Product _queryProduct(final EntityManager em, final Product product) {
        return _querySingleInstance(em.createQuery(
				"SELECT p FROM Product p WHERE p.barcode = :pBarCode",
				Product.class).setParameter("pBarCode", product.getBarcode()));
	}

	private ProductSupplier _queryProductSupplier(final EntityManager em, final ProductSupplier ps) {
        return _querySingleInstance(em.createQuery(
				"SELECT p FROM ProductSupplier p WHERE p.name = :pName",
				ProductSupplier.class).setParameter("pName", ps.getName()));
	}

	private ProductSupplier _queryProductSupplierById(final EntityManager em, final ProductSupplier ps) {
        return _querySingleInstance(em.createQuery(
				"SELECT p FROM ProductSupplier p WHERE p.id = :pId",
				ProductSupplier.class).setParameter("pId", ps.getId()));
	}

	private ProductOrder _queryProductOrderById(final EntityManager em, final ProductOrder order) {
        return _querySingleInstance(em.createQuery(
				"SELECT p FROM ProductOrder p WHERE p.id = :pId",
				ProductOrder.class).setParameter("pId", order.getId()));
	}

	private LoginUser _queryUser(final EntityManager em, final LoginUser user) {
        return _querySingleInstance(em.createQuery(
				"SELECT u FROM LoginUser u WHERE u.username = :uName",
				LoginUser.class).setParameter("uName", user.getUsername()));
	}

	private Customer _queryCustomer(final EntityManager em, final Customer customer) {
	    return _querySingleInstance(em.createQuery(
                "SELECT c FROM Customer c WHERE c.id = :cId",
                Customer.class).setParameter("cId", customer.getId()));
	}

    private <T> T _querySingleInstance(final TypedQuery<T> query) {
        T result;
        try {
            result = query.getSingleResult();
        } catch (final NoResultException e) {
            return null;
        }
        return result;
    }

	/**
	 * Persist the given object in the order they are given.<br>
	 * No argument check is done.
	 *
	 * @param objects the objects to persist
	 */
	private void _persist(final Object... objects) {
		final IData data = DataFactory.getInstance(this.emf);
		final IPersistence pm = data.getPersistenceManager();
		final IPersistenceContext pctx = pm.getPersistenceContext();
		try {
			for (Object o : objects) {
				pctx.makePersistent(o);
			}
		} catch (final Exception e) {
			e.printStackTrace();
			if (LoggerConfig.ON) {
				System.out.println(e.getMessage());
			}
		}
	}

	@Override
	public Notification createCustomer(List<Customer> customers) throws IllegalArgumentException {
		final EntityManager em = this.emf.createEntityManager();
		final Notification notification = new Notification();

		LoginUser _user;
		Store _store = null;

		for (final Customer nextCustomer : customers) {
			// query product
			_user = this._queryUser(em, nextCustomer.getUser());
			if (_user == null) {
				notification.addNotification(
						"createCustomer", Notification.FAILED,
						"User not available:" + nextCustomer.getUser());
				continue;
			}
			// query store
			if (nextCustomer.getPreferredStore() != null) {
				_store = this._queryStoreById(em, nextCustomer.getPreferredStore());
				if (_store == null) {
					notification.addNotification(
							"createCustomer", Notification.FAILED,
							"Store not available:" + nextCustomer.getPreferredStore());
					continue;
				}
			}

			// update object with actual database objects
			nextCustomer.setPreferredStore(_store);
			nextCustomer.setUser(_user);

			// persist
			em.persist(nextCustomer);
			notification.addNotification(
					"createCustomer", Notification.SUCCESS,
					"Creation customer:" + nextCustomer);
		}
		em.flush();
		em.close();
		return notification;
	}

	@Override
	public Notification createUser(List<LoginUser> users) throws IllegalArgumentException {
		final EntityManager em = this.emf.createEntityManager();
		final Notification notification = new Notification();

		for (final LoginUser nextUser: users) {
			// persist
			em.persist(nextUser);
			notification.addNotification(
					"createUser", Notification.SUCCESS,
					"Creation User:" + nextUser);
		}
		em.flush();
		em.close();
		return notification;
	}

	@Override
	public Notification updateUser(List<LoginUser> users) throws IllegalArgumentException {
		final EntityManager em = this.emf.createEntityManager();
		final Notification notification = new Notification();

		for (final LoginUser nextUser: users) {
			LoginUser persistedUser = _queryUser(em, nextUser);

			if (persistedUser == null) {
				notification.addNotification("updateUser", Notification.FAILED,
						"Update user: No such user: " + nextUser.getUsername());
				continue;
			}

			persistedUser.setUsername(nextUser.getUsername());
			persistedUser.setRoles(nextUser.getRoles());
			persistedUser.setCredentials(nextUser.getCredentials());

			em.merge(nextUser);
			notification.addNotification(
					"updateUser", Notification.SUCCESS,
					"Update User:" + nextUser);
		}
		em.flush();
		em.close();
		return notification;
	}

	@Override
	public Notification updateCustomer(List<Customer> customers) throws IllegalArgumentException {
		final EntityManager em = this.emf.createEntityManager();
		final Notification notification = new Notification();

		for (final Customer nextCustomer : customers) {
			Customer persistedCustomer = _queryCustomer(em, nextCustomer);

			if (persistedCustomer == null) {
				notification.addNotification("updateCustomer", Notification.FAILED,
						"Update Customer: No such customer: " + nextCustomer.getId());
				continue;
			}

			persistedCustomer.setFirstName(nextCustomer.getFirstName());
			persistedCustomer.setLastName(nextCustomer.getLastName());
			persistedCustomer.setCreditCardInfo(nextCustomer.getCreditCardInfo());
			persistedCustomer.setPreferredStore(nextCustomer.getPreferredStore());
			// Do not change mail address and users because they are used for
			// the login and should not change

			em.merge(nextCustomer);
			notification.addNotification(
					"updateCustomer", Notification.SUCCESS,
					"Update Customer: " + nextCustomer);
		}
		em.flush();
		em.close();
		return notification;
	}

}
