package org.cocome.tradingsystem.remote.access;

import java.util.List;

import javax.ejb.Remote;

import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.enterprise.ProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.store.ProductOrder;
import org.cocome.tradingsystem.inventory.data.store.StockItem;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.usermanager.Customer;
import org.cocome.tradingsystem.usermanager.LoginUser;

/**
 * With this interface, clients can work by RMI on the database.
 * 
 * @author AlessandroGiusa@gmail.com
 * @version 0.1
 */
@Remote
public interface DatabaseAccess {

	String BEAN_NAME = "DatabaseAccess";
	String JDNI_NAMING = "ejb/remote/DatabaseAccess";

	Notification createProducts(final List<Product> products)
			throws IllegalArgumentException;

	Notification updateProducts(final List<Product> products)
			throws IllegalArgumentException;

	Notification createStore(final List<Store> stores)
			throws IllegalArgumentException;

	Notification updateStore(List<Store> stores)
			throws IllegalArgumentException;

	Notification createEnterprises(List<TradingEnterprise> enterprises)
			throws IllegalArgumentException;

	Notification updateEnterprises(List<TradingEnterprise> list)
			throws IllegalArgumentException;

	Notification createProductSupplier(List<ProductSupplier> productSupplier)
			throws IllegalArgumentException;

	Notification updateProductSupplier(List<ProductSupplier> list)
			throws IllegalArgumentException;

	Notification createStockItem(List<StockItem> stockitems)
			throws IllegalArgumentException;

	Notification updateStockItems(List<StockItem> stockitems)
			throws IllegalArgumentException;

	Notification createProductOrder(List<ProductOrder> orders)
			throws IllegalArgumentException;

	Notification updateProductOrder(List<ProductOrder> orders)
			throws IllegalArgumentException;
	
	Notification createCustomer(List<Customer> list)
			throws IllegalArgumentException;

	Notification createUser(List<LoginUser> list)
			throws IllegalArgumentException;

	Notification updateUser(List<LoginUser> list)
			throws IllegalArgumentException;

	Notification updateCustomer(List<Customer> list)
			throws IllegalArgumentException;

	// TODO make a better method signature. Object is insufficient.
	Notification bookSale(Object o) throws IllegalArgumentException;

	/**
	 * Performs the given query based on Java Persistence Query API
	 * 
	 * @param query
	 * @return
	 * @throws IllegalArgumentException
	 */
	<T> List<T> query(String query) throws IllegalArgumentException;

}
