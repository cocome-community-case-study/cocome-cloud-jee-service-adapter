package webservice;

import java.util.List;

import javax.ejb.Remote;

import org.cocome.tradingsystem.inventory.application.store.ComplexOrderEntryTO;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.store.Store;

@Remote
public interface CoCoMeWebserviceInterface {
	
	String BEAN_NAME = "CoCoMeWebservice";
	String JDNI_NAMING = "ejb/CoCoMeWebservice";
	String WEBSERVICE_NAME= "service/CoCoMeWebservice";
	
	long createStore(String name, String location);
	
	String createStore(String name, String location, String enterprise);
	
	long createTradingEnterprise(String name);
	
	boolean checkTradingEnterprise(long enterpriseID);
	
	String checkStore(String name, String location);
	
	String test(String name);
	
	List<TradingEnterprise> getAllTradingEnterprises();
	
	TradingEnterprise getTradingEnterprise(String name);
	
	
	double getPrice(String storeName, String location, String productName);
	
//	/**
//	 * Get the model of CashDesk, to process all activities where a cash desk is
//	 * needed.
//	 * 
//	 * @param cashDeskName
//	 * @param storeName
//	 * @param bankName
//	 * @param connection
//	 * @return
//	 */
//	CashDeskModel getCashDeskModel(String cashDeskName,
//			String storeName, String bankName, Connection connection);
	
	/**
	 * Get the store with the given name and location
	 * 
	 * @param name
	 * @param location
	 * @return
	 */
	Store getStore(String name, String location);
	
	/**
	 * UC 3
	 * @param order
	 * @param store
	 * @return
	 */
	List<ComplexOrderTO> orderProducts(ComplexOrderTO order, Store store);
	
	/**
	 * UC 4
	 * @param store
	 * @param orderId
	 * @return
	 */
	List<ComplexOrderEntryTO> getOrderedProducts(Store store, long orderId);
	
	/**
	 * UC 5
	 * @param storeName
	 * @param location
	 * @return
	 */
	String getStockReports(String storeName, String location);
	
	/**
	 * UC 6
	 * @param enterpriseName
	 * @return
	 */
	String getDeliveryReports(String enterpriseName);
	
	/**
	 * UC 7
	 * @param storeName
	 * @param location
	 * @param productName
	 * @param newPrice
	 * @return
	 */
	int changePrice(String storeName, String location, String productName, double newPrice);
	
	/**
	 * Get all reports of enterprise
	 * @param enterpriseName
	 * @return
	 */
	String getEnterpriseStockReports(String enterpriseName);
	

}
