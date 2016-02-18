package webservice;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.cocome.tradingsystem.inventory.application.reporting.IReporting;
import org.cocome.tradingsystem.inventory.application.reporting.ReportTO;
import org.cocome.tradingsystem.inventory.application.reporting.ReportingServer;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderEntryTO;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;
import org.cocome.tradingsystem.inventory.application.store.IStoreInventoryManager;
import org.cocome.tradingsystem.inventory.application.store.StockItemTO;
import org.cocome.tradingsystem.inventory.application.store.StoreServer;
import org.cocome.tradingsystem.inventory.data.DataFactory;
import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistence;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContext;
import org.cocome.tradingsystem.inventory.data.store.StockItem;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.remote.access.LoggerConfig;
import org.cocome.tradingsystem.remote.access.Notification;

//@Stateless(name = CoCoMeWebserviceInterface.BEAN_NAME, mappedName = CoCoMeWebserviceInterface.JDNI_NAMING)
//@WebService(serviceName = CoCoMeWebserviceInterface.WEBSERVICE_NAME)
public class CoCoMeWebserviceInterfaceImpl implements CoCoMeWebserviceInterface {

	/**************************************************************************
	 * FIELDS
	 *************************************************************************/

	/** &#64; */
	private static final String ID_DELIMITER = "@";

	@PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
	EntityManagerFactory emf;

	// @PersistenceContext(unitName=IData.EJB_PERSISTENCE_UNIT_NAME)
	// EntityManager em;

	/**************************************************************************
	 * API - PUBLIC - WEBMETHOD
	 *************************************************************************/
	
	@Override
	@WebMethod(operationName="getEnterpriseStockReports")
	public String getEnterpriseStockReports(String enterpriseName) {
		TradingEnterprise enterprise = getTradingEnterprise(enterpriseName);
		if(enterprise!=null){
			ReportTO report = getEnterpriseReports(enterprise);
			if(report!=null){
				return report.getReportText();
			}
			return "NO REPORT";
		}
		return "ENTERPRISE NOT AVAILABLE";
	}
	
	
	
	@Override
	@WebMethod(operationName="getDeliveryReports")
	public String getDeliveryReports(String enterpriseName) {
		TradingEnterprise enterprise = getTradingEnterprise(enterpriseName);
		if(enterprise!=null){
			ReportTO report = getDeliveryReports(enterprise);
			if(report!=null){
				return report.getReportText();
			}
			return "NO REPORT";
		}
		return "ENTERPRISE NOT AVAILABLE";
	}
	
	
	@Override
	@WebMethod(operationName = "getStockReport")
	public String getStockReports(String storeName, String location) {
		Store store = getStore(storeName, location);
		if(store!=null){
			ReportTO report = getStockReport(store);
			if(report!=null){
				return report.getReportText();
			}
			return "NO REPORT";
		}
		return "STORE NOT AVAILABLE";
	}
	
	
	@Override
	@WebMethod(operationName = "changePrice")
	public int changePrice(String storeName, String location, String productName, double newPrice) {
		Store store = getStore(storeName, location);
		StockItem stockItem = getStockItemByName(store, productName);
		if(stockItem!=null){
			StockItemTO stockItemTo = new StockItemTO();
			stockItemTo.setSalesPrice(newPrice);
			stockItemTo.setAmount(stockItem.getAmount());
			stockItemTo.setId(stockItem.getId());
			stockItemTo.setMaxStock(stockItem.getMaxStock());
			stockItemTo.setMinStock(stockItem.getMinStock());
			
			try {
				IData data = DataFactory.getInstance(emf);
				IStoreInventoryManager sim = StoreServer.newInstance(store);
				sim.changePrice(data, stockItemTo);
				persist(store,stockItem); //TODO is this necessary?
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
//			stockItem.setSalesPrice(newPrice);
//			persist(store,stockItem);
			return 0;
		}
		return -1;
	}
	
	@Override
	@WebMethod(operationName = "getPrice")
	public double getPrice(String storeName, String location, String productName) {
		Store store = getStore(storeName, location);
		StockItem stockItem = getStockItemByName(store, productName);
		if(stockItem!=null){
			return (float) stockItem.getSalesPrice();
		}
		return -1;
	}
	
	
	@Override
	@WebMethod(operationName = "createStore")
	public long createStore(String name, String location) {
		if (Boolean.valueOf(checkStore(name, location))) {
			if (LoggerConfig.ON) {
				System.out.println("Id creation failed for Store:" + name
						+ ",Location:" + location
						+ ",Store was already available!");
			}
			return -1l;
		}

		long id = createID(name, location);
		if (id != -1) {
			Store store = new Store();
			store.setId(id);
			store.setName(name);
			store.setLocation(location);
			if (persist(store) != -1) {
				if (LoggerConfig.ON) {
					System.out.println("Id created for Store:" + name
							+ ",Location:" + location + ",id=" + id);
				}

				return id;
			}
		}
		if (LoggerConfig.ON) {
			System.out.println("Id creation failed for Store:" + name
					+ ",Location:" + location + ",id=" + id);
		}
		return -1;
	}
	
	@Override
	public String createStore(String name, String location, String nameEnterprise) {
		if(name!=null && location!=null && nameEnterprise!=null){
			TradingEnterprise enterprise = getTradingEnterprise(nameEnterprise);
			if(enterprise!=null){
				long id = createID(name, location, nameEnterprise);
				if (id != -1) {
					Store store = new Store();
//					store.setId(id);
					store.setName(name);
					store.setLocation(location);
					store.setEnterprise(enterprise);
					if (persist(store,enterprise) != -1) {
						return Notification.SUCCESS;
					}
				}
			}else {
				return "enterprise:"+nameEnterprise+" not available";
			}
		}
		return "Input arguments null";
	}

	@Override
	@WebMethod(operationName = "checkStore")
	public String checkStore(String name, String location) {
		if (name != null && location != null) {
			try {
				IData data = DataFactory.getInstance(emf);
				IPersistence pm = data.getPersistenceManager();
				IPersistenceContext pctx = pm.getPersistenceContext();
				Store store = data.getStoreQuery().queryStore(name, location, pctx);
				return Boolean.toString(store != null);
			} catch (Exception e) {
				e.printStackTrace();
				if (LoggerConfig.ON) {
					System.out.println(e.getMessage());
				}
			}
		}
		return "No store found";
	}
	
	@Override
	@WebMethod(operationName="createTradingEnterprise")
	public long createTradingEnterprise(String name) {
		final long id =createID(name);
		System.out.println("ID for Enterprise:"+id);
		if(checkTradingEnterprise(id)){
			if (LoggerConfig.ON) {
				System.out.println("Id creation failed for TradingEnterprise:" + name
						+ ",TradingEnterprise was already available!");
			}
			return -1l;
		}
		TradingEnterprise enterprise = new TradingEnterprise();
		enterprise.setId(id);
		enterprise.setName(name);
		persist(enterprise);
		return id;
	}
	
	@Override
	@WebMethod(operationName="checkTradingEnterprise")
	public boolean checkTradingEnterprise(long enterpriseId) {
		if (enterpriseId != -1) {
			try {
				IData data = DataFactory.getInstance(emf);
				IPersistence pm = data.getPersistenceManager();
				IPersistenceContext pctx = pm.getPersistenceContext();
				
				TradingEnterprise te = data.getEnterpriseQuery().queryEnterpriseById(enterpriseId, pctx);
				return te!=null && te.getName()!=null;
			} catch (Exception e) {
				e.printStackTrace();
				if (LoggerConfig.ON) {
					System.out.println(e.getMessage());
				}
			}
		}
		return false;
	}

	@Override
	@WebMethod(operationName = "test")
	public String test(@WebParam(name = "name") String name) {
		return "Hello " + name + ", here is your Webservice";
	}
	
	
	/**************************************************************************
	 * API - PUBLIC - EJB METHODS
	 *************************************************************************/
	
	@Override
	public List<ComplexOrderTO> orderProducts(ComplexOrderTO order, Store store){
		if(store!=null){
			try {
				IData data = DataFactory.getInstance(emf);
				IStoreInventoryManager sim = StoreServer.newInstance(store);
				return sim.orderProducts(order);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return null; //TODO chose other return value
	}
	
	@Override
	public List<ComplexOrderEntryTO> getOrderedProducts(Store store, long orderId){
		if(store!=null){
			try {
				IData data = DataFactory.getInstance(emf);
				IStoreInventoryManager sim = StoreServer.newInstance(store);
				ComplexOrderTO coto = sim.getOrder(orderId);
				if(coto!=null){
					return coto.getOrderEntryTOs();
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return null; //TODO chose other return value
	}
	
	@Override
	public List<TradingEnterprise> getAllTradingEnterprises() {
		final IData data = DataFactory.getInstance(emf);
		final IPersistence pm = data.getPersistenceManager();
		final IPersistenceContext pctx = pm.getPersistenceContext();
		
		return data.getEnterpriseQuery().queryAllEnterprises(pctx);
	}
	
	@Override
	public TradingEnterprise getTradingEnterprise(String name) {
		if(name!=null && !name.isEmpty()){
			List<TradingEnterprise> list = getAllTradingEnterprises();
			for(TradingEnterprise next:list){
				if(next.getName().equalsIgnoreCase(name)){
					return next;
				}
			}
		}
		return null;
	}
	
	/**
	 * Get the store with the given name and location.
	 * 
	 * @param name
	 * @param location
	 * @return
	 */
	@Override
	public Store getStore(String name, String location){
		if (name != null && location != null) {
			try {
				IData data = DataFactory.getInstance(emf);
				IPersistence pm = data.getPersistenceManager();
				IPersistenceContext pctx = pm.getPersistenceContext();
				Store store = data.getStoreQuery().queryStore(name, location, pctx);
				return store;
			} catch (Exception e) {
				e.printStackTrace();
				if (LoggerConfig.ON) {
					System.out.println(e.getMessage());
				}
			}
		}
		return null;
	}
	
//	@Override
//	public CashDeskModel getCashDeskModel(String cashDeskName,
//			String storeName, String bankName, Connection connection) {
//		return CashDeskModel.newInstance(cashDeskName,
//				storeName,
//				bankName,
//				connection);
//	}
	
	/**************************************************************************
	 * PRIVATE
	 *************************************************************************/
	
	/**
	 * Get the stock report for the given store.
	 * @param store
	 * @return
	 */
	private ReportTO getStockReport(Store store){
		if(store!=null){
			try {
				IData data = DataFactory.getInstance(emf);
				IPersistence pm = data.getPersistenceManager();
				IPersistenceContext pctx = pm.getPersistenceContext();
				IReporting reporting = new ReportingServer();
				return reporting.getStoreStockReport(store, pctx);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Get the delivery reports
	 * @param enterprise
	 * @return
	 */
	private ReportTO getDeliveryReports(TradingEnterprise enterprise){
		if(enterprise!=null){
			try {
				IData data = DataFactory.getInstance(emf);
				IPersistence pm = data.getPersistenceManager();
				IPersistenceContext pctx = pm.getPersistenceContext();
				IReporting reporting = new ReportingServer();
				return reporting.getEnterpriseDeliveryReport(enterprise, pctx);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private ReportTO getEnterpriseReports(TradingEnterprise enterprise){
		if(enterprise!=null){
			try {
				IData data = DataFactory.getInstance(emf);
				IPersistence pm = data.getPersistenceManager();
				IPersistenceContext pctx = pm.getPersistenceContext();
				IReporting reporting = new ReportingServer();
				return reporting.getEnterpriseStockReport(enterprise, pctx);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	/**
	 * Get StockItem by its underlying {@link Product} name
	 * 
	 * @param store use {@link #getStore(String, String)}
	 * @param name
	 * @return
	 */
	private StockItem getStockItemByName(Store store, String name){
		if(store!=null){
			for(StockItem nextStockItem: store.getStockItems()){
				if(nextStockItem.getProduct().getName().equalsIgnoreCase(name)){
					return nextStockItem;
				}
			}
		}
		return null;
	}
	
	/**
	 * Create a unique id by concatenating the given tokens with the
	 * {@link #ID_DELIMITER}
	 * 
	 * @param token
	 * @return
	 */
	private long createID(String... token) {
		if (token != null && token.length > 0) {
			StringBuilder builder = new StringBuilder();
			String[] tempToken = token.clone();
			int len = tempToken.length;
			for (int i = 0; i < len; i++) {
				if (tempToken[i] == null || tempToken[i].isEmpty()) {
					return -1;
				}
				builder.append(tempToken[i].trim().replaceAll(" ", ""));
				if ((i + 1) < len) {
					builder.append(ID_DELIMITER);
				}
			}
			return builder.toString().hashCode();
		}
		return -1;
	}

	/**
	 * Persists the provided objects. Object should not be null
	 * 
	 * @param objects
	 * @return
	 */
	private int persist(Object... objects) {
		if (objects != null && objects.length > 0) {
			Object[] tempToken = objects.clone();
			int len = tempToken.length;
			IData data = DataFactory.getInstance(emf);
			IPersistence pm = data.getPersistenceManager();
			IPersistenceContext pctx = pm.getPersistenceContext();

			try {
				// TODO how to handle transaction?
				// pctx.getTransactionContext().beginTransaction();

				for (int i = 0; i < len; i++) {
					pctx.makePersistent(objects[i]);
				}

				// pctx.getTransactionContext().commit();
				return 1;

			} catch (Exception e) {
				// if(pctx!=null && pctx.getTransactionContext().isActive())
				// pctx.getTransactionContext().rollback();
				e.printStackTrace();
				if (LoggerConfig.ON) {
					System.out.println(e.getMessage());
				}

			}
		}
		return -1;
	}

}
