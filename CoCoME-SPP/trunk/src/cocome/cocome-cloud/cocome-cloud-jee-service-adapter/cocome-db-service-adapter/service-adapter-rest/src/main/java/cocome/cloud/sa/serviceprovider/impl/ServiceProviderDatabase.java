package cocome.cloud.sa.serviceprovider.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBElement;

import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.enterprise.ProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.store.OrderEntry;
import org.cocome.tradingsystem.inventory.data.store.ProductOrder;
import org.cocome.tradingsystem.inventory.data.store.StockItem;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.remote.access.DatabaseAccess;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.usermanager.Customer;
import org.cocome.tradingsystem.usermanager.LoginUser;
import org.cocome.tradingsystem.usermanager.credentials.AbstractCredential;
import org.cocome.tradingsystem.usermanager.credentials.ICredential;
import org.cocome.tradingsystem.usermanager.credentials.PlainPassword;
import org.cocome.tradingsystem.usermanager.datatypes.CredentialType;
import org.cocome.tradingsystem.usermanager.datatypes.Role;

import cocome.cloud.sa.entities.Message;
import cocome.cloud.sa.entities.MessageEntry;
import cocome.cloud.sa.query.QueryConst;
import cocome.cloud.sa.query.parsing.QueryParser;
import cocome.cloud.sa.serviceprovider.Service;
import cocome.cloud.sa.serviceprovider.ServiceProvider;
import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import de.kit.ipd.java.utils.framework.table.TableObjectFactory;
import de.kit.ipd.java.utils.parsing.csv.CSVParser;
import de.kit.ipd.java.utils.time.TimeUtils;
import de.kit.ipd.java.utils.xml.JAXBEngine;


/**
 * Servlet implementation for the ServiceProviderDatabase-Service. This Service
 * provides access to the database
 * @author Alessandro Giusa, alessandrogiusa@gmail.com
 *
 */
@WebServlet("/Database/ServiceProviderDatabase")
//@ServletSecurity(httpMethodConstraints={ 
//		@HttpMethodConstraint(value="GET",rolesAllowed={"ADMIN"},transportGuarantee=TransportGuarantee.CONFIDENTIAL),
//	    @HttpMethodConstraint(value="POST", rolesAllowed={"ADMIN"},transportGuarantee=TransportGuarantee.CONFIDENTIAL),
//	    @HttpMethodConstraint(value="PUT", rolesAllowed={"ADMIN"},transportGuarantee=TransportGuarantee.CONFIDENTIAL),
//	    @HttpMethodConstraint(value="TRACE", emptyRoleSemantic=ServletSecurity.EmptyRoleSemantic.DENY)}) 
public class ServiceProviderDatabase extends HttpServlet {
	
	// ********************************************************************
	// * LOCAL CLASSES
	// ********************************************************************
	
	/**
	 * Local class to encapsulate the GET-Request where the description of this
	 * {@link ServiceProvider} is requested.
	 * 
	 * @author Alessandro Giusa, alessandrogiusa@gmail.com
	 *
	 */
	private static class ServiceProviderDescriptor {
		
		/**
		 * Write the description to {@link HttpServletResponse}
		 * @param request
		 * @param response
		 * @throws IOException
		 */
		public static void getDescription(HttpServletRequest request,
				HttpServletResponse response) throws IOException{
			
			String urlBase = request.getScheme() 
					+ "://" + request.getServerName()
					+ ":" + request.getServerPort() 
					+ request.getContextPath();

			ServiceProvider spDatabase = new ServiceProvider();
			spDatabase.setName(NAME_SERVICE_PROVIDER_DATABASE);
			spDatabase.setUrl(urlBase + ServiceProviderInterface.URL_SERVICE_BASE
					+ URL_SERVICE_PROVIDER_DATABASE);

			Service getData = new Service();
			getData.setName(NAME_SERVICE_DATABASE_GETDATA);
			getData.setUrl(urlBase + ServiceProviderInterface.URL_SERVICE_BASE
					+ URL_SERVICE_DATABASE_GETDATA);
			
			Service setData = new Service();
			setData.setName(NAME_SERVICE_DATABASE_SETDATA);
			setData.setUrl(urlBase + ServiceProviderInterface.URL_SERVICE_BASE
					+ URL_SERVICE_DATABASE_SETDATA);
			
			Service schemas = new Service();
			schemas.setName(NAME_SERVICE_SCHEMAS);
			schemas.setUrl(urlBase + ServiceProviderInterface.URL_SERVICE_BASE
					+ URL_SERVICE_DATABASE_SCHEMAS);
			
			
			spDatabase.getServices().add(getData);
			spDatabase.getServices().add(setData);
			spDatabase.getServices().add(schemas);
			
			response.setContentType("text/xml");
			PrintWriter writer = response.getWriter();
			writer.append(JAXBEngine.getInstance().write(spDatabase).toString());
			writer.close();
		}
	}
	
	// ********************************************************************
	// * GLOBAL FIELDS
	// ********************************************************************
	
	private static final long serialVersionUID = 1L;
    
	private static final String pathTypesSchema = "/WEB-INF/schemas/types.xsd";
	
	private static final String CONTENT_TYPE_CSV = "application/csv";
	private static final String CONTENT_TYPE_XML = "application/xml";
	
	private static final String ENTERPRISE = "enterprise";
	private static final String STORE = "store";
	private static final String PRODUCT = "product";
	private static final String PRODUCTSUPPLIER = "productsupplier";
	private static final String STOCKITEM = "stockitem";
	private static final String PRODUCTORDER = "productorder";
	private static final String USER = "loginuser";
	private static final String CUSTOMER = "customer";
	
	public static final String URL_SERVICE_PROVIDER_DATABASE = "/Database/ServiceProviderDatabase";
	public static final String NAME_SERVICE_PROVIDER_DATABASE = "Database";
	
	public static final String URL_SERVICE_DATABASE_GETDATA = "/Database/GetData";
	public static final String NAME_SERVICE_DATABASE_GETDATA = "GetData";
	
	public static final String URL_SERVICE_DATABASE_SETDATA = "/Database/SetData";
	public static final String NAME_SERVICE_DATABASE_SETDATA = "SetData";
	
	public static final String MESSAGE_ENTRY_RESULT = "result";

	private static final String NAME_SERVICE_SCHEMAS = "Schemas";

	private static final String URL_SERVICE_DATABASE_SCHEMAS = "/Database/Schemas";
	
	// ********************************************************************
	// * LOCAL FIELDS
	// ********************************************************************
	
	/**EJB for database access*/
	@EJB
	private DatabaseAccess databaseAccess;
	
	/**Content-Type format*/
	private String contentTypeFormat = CONTENT_TYPE_XML;
	
	// ********************************************************************
	// * CONSTRUCTORS
	// ********************************************************************
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServiceProviderDatabase() {
        super();
    }
    
    // ********************************************************************
	// * AUTHENTICATION
	// ********************************************************************
    
    
    
    
    
    // ********************************************************************
	// * HTTP SERVLET METHODS
	// ********************************************************************
    
    //TODO
    /*
     * Problem: URLEncoding
     * If the request url is encoded, it has to be decoded first!Otherwise the query is not
     * working
     */
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String requestedUri = request.getRequestURI();
		if (requestedUri.endsWith(URL_SERVICE_DATABASE_SETDATA)) {
    		String next = null;
    		for(Enumeration<String> param = request.getParameterNames();
    				param.hasMoreElements();){
    			next = param.nextElement();
    			switch(next){
    			case QueryConst.QUERY_INSERT:
    				dispatchQueryWriteRequest(next, request, response);
    				break;
    			}
    		}
		}
	}
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
    	String requestedUri = request.getRequestURI();
    	if(requestedUri.endsWith(URL_SERVICE_DATABASE_SETDATA)){
    		String next = null;
    		for(Enumeration<String> param = request.getParameterNames();param.hasMoreElements();){
    			next = param.nextElement();
    			switch(next){
    			case QueryConst.QUERY_UPDATE:
    				dispatchQueryWriteRequest(next, request, response);
    				break;
    			}
    		}
		}
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
    	String requestedUri = request.getRequestURI();
    	if(requestedUri.endsWith(URL_SERVICE_DATABASE_SETDATA)){
    		String next = null;
    		for(Enumeration<String> param = request.getParameterNames();param.hasMoreElements();){
    			next = param.nextElement();
    			switch(next){
    			case QueryConst.QUERY_DELETE:
    				dispatchQueryWriteRequest(next, request, response);
    				break;
    			}
    		}
		}
    }
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response) 
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String requestedUri = request.getRequestURI();
		if(requestedUri.endsWith(URL_SERVICE_PROVIDER_DATABASE)){
			ServiceProviderDescriptor.getDescription(request, response);
		
		} else if(requestedUri.endsWith(URL_SERVICE_DATABASE_GETDATA)){
			dispatchQueryReadRequest(request, response);
		
		} else if(requestedUri.endsWith(URL_SERVICE_DATABASE_SCHEMAS)){
			ServiceProviderSchemaHelp.getSchemas(request, response, getServletContext());
		}
	}
	
	// ********************************************************************
	// * QUERY-DISPATCHER
	// ********************************************************************
	
	/**
	 * Provides following query functionality:<br><br>
	 * <ul>
	 * <li>{@link QueryConst#QUERY_INSERT}</li>
	 * <li>{@link QueryConst#QUERY_UPDATE}</li>
	 * <li>{@link QueryConst#QUERY_DELETE}</li>
	 * </ul>
	 * @param qm
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void dispatchQueryWriteRequest(String qm, 
			HttpServletRequest request, HttpServletResponse response)
					throws IOException{
		
		String requestedUri = request.getRequestURI();
		if(!requestedUri.endsWith(URL_SERVICE_DATABASE_SETDATA)){
			return;
		}
		this.contentTypeFormat = request.getHeader("Content-Type");
		this.contentTypeFormat = request.getContentType();
		if(this.contentTypeFormat.contains(";")){
			this.contentTypeFormat = this.contentTypeFormat.substring(0,
					this.contentTypeFormat.indexOf(";", 0)).trim().replaceAll(" ", "");
		}
		
		Message message = new Message();
		message.appendHeader("RequestedTime", TimeUtils.getTime());
		message.appendHeader("RequestedUrl",
				request.getRequestURL().toString());
		message.appendHeader("RequestedParam", request.getQueryString());
		message.appendHeader("RequestedFormat", this.contentTypeFormat);
		
		PrintWriter writer = response.getWriter();
		try {
			BufferedReader br = request.getReader();
			StringBuilder builder = new StringBuilder();
			String inputLine;
			while ((inputLine = br.readLine()) != null){
				builder.append(inputLine);
				builder.append(System.lineSeparator());
			}
			String data = builder.toString();
			
			switch (qm.trim().toLowerCase()) {
			case QueryConst.QUERY_SELECT:
				String queryselect = querySelect(request.getParameter(qm),
						message);
				System.out.println(queryselect);
				message.appendBody(MESSAGE_ENTRY_RESULT, queryselect);
				break;
			case QueryConst.QUERY_INSERT:
				queryInsert(request.getParameter(qm),data,message);
				break;
			case QueryConst.QUERY_UPDATE:
				queryUpdate(request.getParameter(qm), data,message);
				break;
			case QueryConst.QUERY_DELETE:
				// TODO delete impl
				break;
			default:
				message.appendBody("Error", "command "+qm+" not available!");
				break;
			}
			
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			Throwable cause = e.getCause();
			while(cause!=null){
				message.appendBody("Error", cause.getMessage());
				cause = cause.getCause();
			}
		}
		JAXBEngine engine = JAXBEngine.getInstance();
		StringWriter strWriter = new StringWriter();
		engine.write(message, strWriter, Message.class,MessageEntry.class);
		String strRes = strWriter.toString();
		writer.append(strRes);
		writer.close();
	}
	
	/**
	 * Provides following query functionality:<br><br>
	 * <ul>
	 * <li>{@link QueryConst#QUERY_SELECT}</li>
	 * </ul>
	 * @param request
	 * @param response
	 */
	private void dispatchQueryReadRequest(HttpServletRequest request,
			HttpServletResponse response) {
		
		this.contentTypeFormat = request.getContentType();
		Message message = new Message();
		message.appendHeader("RequestedTime", TimeUtils.getTime());
		message.appendHeader("RequestedUrl",
				request.getRequestURL().toString());
		message.appendHeader("RequestedParam", request.getQueryString());
		message.appendHeader("RequestedFormat", this.contentTypeFormat);
		
		
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			String next = null;
			//iteration through all parameter
			for (Enumeration<String> param = request.getParameterNames(); param
					.hasMoreElements();) {
				next = param.nextElement();
				switch (next) {
				case QueryConst.QUERY_SELECT:
					String queryselect = querySelect(request.getParameter(next),
							message);
					message.appendBody(MESSAGE_ENTRY_RESULT, queryselect);
					break;
				default:
					message.appendBody("Error", next+" not available!");
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Throwable cause = e.getCause();
			while(cause!=null){
				message.appendBody("Error", cause.getMessage());
				cause = cause.getCause();
			}
		}
		JAXBEngine engine = JAXBEngine.getInstance();
		StringWriter strWriter = new StringWriter();
		engine.write(message, strWriter, Message.class,MessageEntry.class);
		String strRes = strWriter.toString();
		writer.append(strRes);
		writer.close();
	}
	
	// ********************************************************************
	// * QUERY WRITE
	// ********************************************************************
	
	/**
	 * Query-Insert
	 * @param parameter
	 * @param content
	 * @param message
	 */
	private void queryInsert(String parameter, String content, Message message){
		
		switch (parameter.toLowerCase()) {
		case ENTERPRISE:
			createEnterprises(content,message);
			break;
		case STORE:
			createStores(content,message);
			break;
		case PRODUCT:
			createProducts(content,message);
			break;
		case PRODUCTSUPPLIER:
			createProductSupplier(content,message);
			break;
		case STOCKITEM:
			createStockItem(content,message);
			break;
		case PRODUCTORDER:
			createProductOrder(content,message);
			break;
		case USER:
			System.out.println("Received user insert message");
			createUser(content, message);
			break;
		case CUSTOMER:
			createCustomer(content, message);
			break;
		default:
			message.appendBody("Error", "parameter value " 
								+ parameter + " not available!");
			break;
		}
	}

	private void queryUpdate(String parameter, String content, Message message){
		switch (parameter.toLowerCase()) {
		case ENTERPRISE:
			updateEnterprises(content, message);
			break;
		case STORE:
			updateStores(content,message);
			break;
		case PRODUCT:
			updateProducts(content,message);
			break;
		case PRODUCTSUPPLIER:
			updateProductSupplier(content,message);
			break;
		case PRODUCTORDER:
			updateProductOrder(content,message);
			break;
		case STOCKITEM:
			updateStockItems(content,message);
			break;
		case USER:
			updateUser(content, message);
			break;
		case CUSTOMER:
			updateCustomer(content, message);
			break;
		default:
			message.appendBody("Error", "parameter value " 
								+ parameter + " not available!");
			break;
		}
	}
	
	private void queryDelete(String parameter, String content, Message message){
		//TODO impl missing
	}
	
	// ********************************************************************
	// * QUERY READ
	// ********************************************************************
	
	@SuppressWarnings("unchecked")
	private String querySelect(String parameter,Message msg){
		//TODO debug
		System.out.println("to parser->"+parameter);
		
		//create query
		QueryParser parser = new QueryParser();
		parser.parse("query.select="+parameter);
		String _query = parser.getModel();
		
		//TODO debug
		System.out.println("Query:"+_query);
		//perform query
		
		String response = "No Result!";
		
		List queryResult = this.databaseAccess.query(_query);
		msg.appendBody("Info Size Resultset",String.valueOf(queryResult.size()));
		
		//TODO debug
		System.out.println("Found Results:"+queryResult.size());
		
		//compute the query
		CSVParser csvparser = new CSVParser();
		Table<String> table = new Table<>();
		switch (parser.getEntityType().toLowerCase()) {
		case STORE:
			table = createStoreTable(queryResult);
			break;
		case "tradingenterprise":
			table = createEnterpriseTable(queryResult);
			break;
		case PRODUCT:
			table = createProductTable(queryResult);
			break;
		case PRODUCTSUPPLIER:
			table = createProductSupplierTable(queryResult);
			break;
		case STOCKITEM:
			table = createStockItemTable(queryResult);
			break;
		case PRODUCTORDER:
			table = createProductOrderTable(queryResult);
			break;
		case USER:
			table = createUserTable(queryResult);
			break;
		case CUSTOMER:
			table = createCustomerTable(queryResult);
			break;
		default:
			//TODO add notification
			break;
		}
		
		//chose the format
		switch (this.contentTypeFormat.toLowerCase()) {
		case CONTENT_TYPE_CSV:
			csvparser.setModel(table);
			response = csvparser.toString();
			break;
			
		default/*XML*/:
			JAXBEngine engine = JAXBEngine.getInstance();
			StringWriter writer = new StringWriter();
			engine.write(table, writer, table.getObjectFactory());
			response = writer.toString();
			break;
		}
		return response;
	}
	
	// ********************************************************************
	// * CREATE ENTITY
	// ********************************************************************
	
	private void createEnterprises(String content, Message message) {
		List<TradingEnterprise> list = createEnterpriseList(createTable(content));
		
		Notification notification = null;
		for(TradingEnterprise enterprise:list){
			//TODO why here only one enterprise?
			notification = this.databaseAccess.createEnterprise(enterprise);
			includeNotification(notification.getNotification(), message);
		}
	}
	
	private void createStores(String content, Message message){
		List<Store> list = createStoreList(createTable(content));
		Notification notification = this.databaseAccess.createStore(list);
		includeNotification(notification.getNotification(), message);
	}
	
	private void createProducts(String content, Message message) {
		List<Product> list = createProductList(createTable(content));
		Notification notification = this.databaseAccess.createProducts(list);
		includeNotification(notification.getNotification(), message);
	}
	
	private void createProductSupplier(String content, Message message) {
		List<ProductSupplier> list = createProductSupplierList(createTable(content));
		Notification notification = this.databaseAccess.createProductSupplier(list);
		includeNotification(notification.getNotification(), message);
	}
	
	private void createStockItem(String content, Message message){
		List<StockItem> list = createStockItemList(createTable(content));
		Notification notification = this.databaseAccess.createStockItem(list);
		includeNotification(notification.getNotification(), message);
	}
	
	private void createProductOrder(String content, Message message){
		List<ProductOrder> list = createProductOrderList(createTable(content));
		Notification notification = this.databaseAccess.createProductOrder(list);
		includeNotification(notification.getNotification(), message);
	}
	
	private void createUser(String content, Message message){
		System.out.println("Creating user list...");
		List<LoginUser> list = createUserList(createTable(content));
		Notification notification = this.databaseAccess.createUser(list);
		includeNotification(notification.getNotification(), message);
	}
	
	private void createCustomer(String content, Message message){
		List<Customer> list = createCustomerList(createTable(content));
		Notification notification = this.databaseAccess.createCustomer(list);
		includeNotification(notification.getNotification(), message);
	}
	
	// ********************************************************************
	// * UPDATE ENTITIES
	// ********************************************************************
		
	private void updateEnterprises(String content, Message message) {
		List<TradingEnterprise> list = createEnterpriseList(createTable(content));
		Notification notification = this.databaseAccess.updateEnterprises(list);
		includeNotification(notification.getNotification(), message);
	}
	
	private void updateStores(String content, Message message) {
		List<Store> list = createStoreList(createTable(content));
		Notification notification = this.databaseAccess.updateStore(list);
		includeNotification(notification.getNotification(), message);
	}
	
	private void updateProducts(String content, Message message) {
		List<Product> list = createProductList(createTable(content));
		Notification notification = this.databaseAccess.updateProducts(list);
		includeNotification(notification.getNotification(), message);
	}
	
	private void updateProductOrder(String content, Message message) {
		List<ProductOrder> list = createProductOrderList(createTable(content));
		Notification notification = this.databaseAccess.updateProductOrder(list);
		includeNotification(notification.getNotification(), message);
	}
	
	private void updateProductSupplier(String content, Message message) {
		List<ProductSupplier> list = createProductSupplierList(createTable(content));
		Notification notification = this.databaseAccess.updateProductSupplier(list);
		includeNotification(notification.getNotification(), message);
	}
	
	private void updateStockItems(String content, Message message){
		List<StockItem> list = createStockItemList(createTable(content));
		Notification notification = this.databaseAccess.updateStockItems(list);
		includeNotification(notification.getNotification(), message);
	}
	
	private void updateUser(String content, Message message){
		List<LoginUser> list = createUserList(createTable(content));
		Notification notification = this.databaseAccess.updateUser(list);
		includeNotification(notification.getNotification(), message);
	}
	
	private void updateCustomer(String content, Message message){
		List<Customer> list = createCustomerList(createTable(content));
		Notification notification = this.databaseAccess.updateCustomer(list);
		includeNotification(notification.getNotification(), message);
	}
	
	// ********************************************************************
	// * CREATE ENTITY-LIST FROM TABLE
	// ********************************************************************

	private List<TradingEnterprise> createEnterpriseList(Table<String> table){
		ArrayList<TradingEnterprise> list = new ArrayList<>();
		int len = table.size();
		Column<String> colName;
		Column<String> colId;
		for (int i = 0; i < len; i++) {
			colName = table.getColumnByName(i, "EnterpriseName");
			colId = table.getColumnByName(i, "EnterpriseId");
			TradingEnterprise t = new TradingEnterprise();
			t.setName(colName.getValue());
//			t.setId(-1l); -> this probably causes the bug that only one enterprise can be created
			if(colId!=null){
				t.setId(Long.parseLong(colId.getValue()));
			}
			list.add(t);
		}
		return list;
	}
	
	private List<Store> createStoreList(Table<String> table){
		ArrayList<Store> list = new ArrayList<>();
		int len = table.size();
		Column<String> colEnterprise;
		Column<String> colName;
		Column<String> colId;
		Column<String> colLocation;
		for (int i = 0; i < len; i++) {
			colEnterprise = table.getColumnByName(i, "EnterpriseName");
			TradingEnterprise t = new TradingEnterprise();
			t.setName(colEnterprise.getValue());
			colId = table.getColumnByName(i, "StoreId");
			colName = table.getColumnByName(i, "StoreName");
			colLocation = table.getColumnByName(i, "StoreLocation");
			Store s = new Store();
			if(colId!=null){
				s.setId(Long.parseLong(colId.getValue()));
			} else {
				s.setId(-1l);
			}
			s.setEnterprise(t);
			s.setLocation(colLocation.getValue());
			s.setName(colName.getValue());
			list.add(s);
		}
		return list;
	}
	
	private List<Product> createProductList(Table<String> table){
		ArrayList<Product> list = new ArrayList<>();
		int len = table.size();
		Column<String> colBarcode;
		Column<String> colName;
		Column<String> colPurchasePrice;
		for (int i = 0; i < len; i++) {
			colBarcode = table.getColumnByName(i, "ProductBarcode");
			colName = table.getColumnByName(i, "ProductName");
			colPurchasePrice = table.getColumnByName(i, "ProductPurchasePrice");
			
			if(colBarcode==null){
				System.out.println("Barcode column was null for row="+i);
				continue;
			}
			Product p = new Product();
			p.setBarcode(Long.parseLong(colBarcode.getValue()));
			if(colName!=null){
				p.setName(colName.getValue());
			}
			if(colPurchasePrice!=null){
				p.setPurchasePrice(Double.parseDouble(colPurchasePrice.getValue()));
			}
			list.add(p);
		}
		return list;
	}
	
	
	private List<ProductSupplier> createProductSupplierList(
			Table<String> table) {
		ArrayList<ProductSupplier> list = new ArrayList<>();
		int len = table.size();
		Column<String> colName;
		Column<String> colId;
		for (int i = 0; i < len; i++) {
			colName = table.getColumnByName(i, "ProductSupplierName");
			colId = table.getColumnByName(i, "ProductSupplierId");
			if(colName==null){
				//TODO message error!
				System.out.println("Name column was null for row="+i);
				continue;
			}
			
			ProductSupplier p = new ProductSupplier();
			if(colId!=null){
				p.setId(Long.parseLong(colId.getValue()));
			}
			p.setName(colName.getValue());
			list.add(p);
		}
		return list;
	}
	
	private List<ProductOrder> createProductOrderList(Table<String> table){
		
		HashMap<String, ProductOrder> map = new HashMap<>();
		
		int len = table.size();
		Column<String> colProductOrderId;
		Column<String> colStoreId;
		Column<String> colProductBarcode;
		Column<String> colOrderDeliveryDate;
		Column<String> colOrderOrderingDate;
		Column<String> colOrderAmount;
		
		ProductOrder productOrder = null;
		
		for (int i = 0; i < len; i++) {
			colProductOrderId = table.getColumnByName(i, "ProductOrderId");
			colStoreId = table.getColumnByName(i, "StoreId");
			colProductBarcode = table.getColumnByName(i, "ProductBarcode");
			colOrderDeliveryDate = table.getColumnByName(i, "OrderDeliveryDate");
			colOrderOrderingDate = table.getColumnByName(i, "OrderOrderingDate");
			colOrderAmount = table.getColumnByName(i, "OrderAmount");
			
			productOrder = map.get(colProductOrderId.getValue());
			if(productOrder==null){
				productOrder = new ProductOrder();
				productOrder.setId(Long.parseLong(colProductOrderId.getValue()));
				productOrder.setOrderEntries(new ArrayList<OrderEntry>());
				
				Store store = new Store();
				store.setId(Long.parseLong(colStoreId.getValue()));
				productOrder.setStore(store);
				
				productOrder.setDeliveryDate(TimeUtils.convertToDateObject(
						colOrderDeliveryDate.getValue()));
				productOrder.setOrderingDate(TimeUtils.convertToDateObject(
						colOrderOrderingDate.getValue()));
				
				map.put(colProductOrderId.getValue(), productOrder);
			}
			
			Product product = new Product();
			product.setBarcode(Long.parseLong(colProductBarcode.getValue()));
			
			OrderEntry orderentry = new OrderEntry();
			orderentry.setOrder(productOrder);
			
			orderentry.setAmount(Long.parseLong(colOrderAmount.getValue()));
			orderentry.setProduct(product);
			productOrder.getOrderEntries().add(orderentry);
		}
		
		ArrayList<ProductOrder> list = new ArrayList<>(map.values());
		System.out.println("Product Order List:"+list.size());
		return list;
	}
	
	private List<StockItem> createStockItemList(Table<String> table){
		ArrayList<StockItem> list = new ArrayList<>();
		int len = table.size();
		Column<String> colEnterpriseName;
		Column<String> colStoreName;
		Column<String> colStoreId;
		Column<String> colStoreLocation;
		Column<String> colProductBarcode;
		Column<String> colStockItemMinStock;
		Column<String> colStockItemMaxStock;
		Column<String> colStockItemIncomingAmount;
		Column<String> colStockItemAmount;
		Column<String> colStockItemSalesPrice;
		for (int i = 0; i < len; i++) {
			colStoreId = table.getColumnByName(i, "StoreId");
			colProductBarcode = table.getColumnByName(i, "ProductBarcode");
			colStockItemMinStock = table.getColumnByName(i, "StockItemMinStock");
			colStockItemMaxStock = table.getColumnByName(i, "StockItemMaxStock");
			colStockItemIncomingAmount = table.getColumnByName(i, "StockItemIncomingAmount");
			colStockItemAmount = table.getColumnByName(i, "StockItemAmount");
			colStockItemSalesPrice = table.getColumnByName(i, "StockItemSalesPrice");
			
			
			Store store = new Store();
			
			if(colStoreId!=null){
				store.setId(Long.parseLong(colStoreId.getValue()));
			}else {
				store.setId(-1l);
			}

			Product product = new Product();
			product.setBarcode(Long.parseLong(colProductBarcode.getValue()));
			
			StockItem stockItem = new StockItem();
			stockItem.setStore(store);
			stockItem.setProduct(product);
			stockItem.setIncomingAmount(Long.parseLong(colStockItemIncomingAmount.getValue()));
			stockItem.setMaxStock(Long.parseLong(colStockItemMaxStock.getValue()));
			stockItem.setMinStock(Long.parseLong(colStockItemMinStock.getValue()));
			stockItem.setAmount(Long.parseLong(colStockItemAmount.getValue()));
			stockItem.setSalesPrice(Double.parseDouble(colStockItemSalesPrice.getValue()));
			
			list.add(stockItem);
		}
		return list;
	}
	
	private List<LoginUser> createUserList(Table<String> table){
		int len = table.size();
		System.out.println("User table size: " + table.size());
		HashMap<String, LoginUser> list = new HashMap<>((int) (len / 0.75));
		
		Column<String> colUserId;
		Column<String> colUsername;
		Column<String> colCredentialType;
		Column<String> colCredentialString;
		Column<String> colRole;
		
		for (int i = 0; i < len; i++) {
			colUserId = table.getColumnByName(i, "UserId");
			colUsername = table.getColumnByName(i, "UserName");
			colCredentialType = table.getColumnByName(i, "CredentialType");
			colCredentialString = table.getColumnByName(i, "CredentialString");
			colRole = table.getColumnByName(i, "Role");
			
			LoginUser user;
			
			if (colUsername != null) {
				String username = colUsername.getValue();
				user = list.get(username);
				
				if (user == null) {
					user = new LoginUser();
					user.setUsername(username);
				}				
			} else {
				//user.setUsername("default");
				continue;
			}
			
			if(colUserId != null) {
				user.setId(Long.parseLong(colUserId.getValue()));
			}

			Map<CredentialType, AbstractCredential> credentials = user.getCredentials();
			
			if (credentials == null) {
				credentials = new HashMap<>();
			}
			
			if (colCredentialType != null 
					&& colCredentialType.getValue().equals(CredentialType.PASSWORD.label())) {
				PlainPassword password = new PlainPassword();
				password.setCredentialString(colCredentialString.getValue());
				credentials.put(CredentialType.PASSWORD, password);
			}
			
			Set<Role> roles = user.getRoles();
			
			if (roles == null) {
				roles = new HashSet<>();
			}
			
			if (colRole != null) {
				roles.add(Role.valueOf(colRole.getValue()));
			}
			
			user.setCredentials(credentials);
			user.setRoles(roles);
			
			System.out.println("Adding User :" + user.toString());
			
			list.put(user.getUsername(), user);
		}
		return new ArrayList<>(list.values());
	}
	
	private List<Customer> createCustomerList(Table<String> table){
		ArrayList<Customer> list = new ArrayList<>();
		int len = table.size();
		
		Column<String> colCustomerId;
		Column<String> colFirstName;
		Column<String> colLastName;
		Column<String> colMailAddress;
		Column<String> colCreditCardString;
		Column<String> colPreferredStoreId;
		Column<String> colPreferredStoreEnterpriseName;
		Column<String> colPreferredStoreName;
		Column<String> colPreferredStoreLocation;
		Column<String> colUserId;
		Column<String> colUsername;
		
		for (int i = 0; i < len; i++) {
			colUserId = table.getColumnByName(i, "UserId");
			colUsername = table.getColumnByName(i, "UserName");
			colCustomerId = table.getColumnByName(i, "CustomerId");
			colFirstName = table.getColumnByName(i, "FirstName");
			colLastName = table.getColumnByName(i, "LastName");
			colMailAddress = table.getColumnByName(i, "MailAddress");
			colCreditCardString = table.getColumnByName(i, "CreditCardInfo");
			colPreferredStoreId = table.getColumnByName(i, "PreferredStoreId");
			colPreferredStoreEnterpriseName = table.getColumnByName(i, "PreferredStoreEnterpriseName");
			colPreferredStoreName = table.getColumnByName(i, "PreferredStoreName");
			colPreferredStoreLocation = table.getColumnByName(i, "PreferredStoreLocation");
			
			Customer customer= new Customer();
			
			if (colCustomerId != null) {
				customer.setId(Long.parseLong(colCustomerId.getValue()));
			}
			
			if (colPreferredStoreId != null) {
				Store preferredStore = new Store();
				preferredStore.setId(Long.parseLong(colPreferredStoreId.getValue()));
				preferredStore.setName(colPreferredStoreName.getValue());
				preferredStore.setLocation(colPreferredStoreLocation.getValue());
				
				TradingEnterprise enterprise = new TradingEnterprise();
				enterprise.setName(colPreferredStoreEnterpriseName.getValue());
				preferredStore.setEnterprise(enterprise);
				
				customer.setPreferredStore(preferredStore);
			}
			
			LoginUser user = new LoginUser();
			if (colUserId != null) {
				user.setId(Long.parseLong(colUserId.getValue()));
			}
			
			if (colUsername != null) {
				user.setUsername(colUsername.getValue());
			}
			customer.setUser(user);
			
			Set<String> creditCardInfo = new HashSet<>();
			if (colCreditCardString != null) {
				creditCardInfo.add(colCreditCardString.getValue());
			}
			
			customer.setCreditCardInfo(creditCardInfo);
			customer.setFirstName(colFirstName.getValue());
			customer.setLastName(colLastName.getValue());
			customer.setMailAddress(colMailAddress.getValue());
			
			list.add(customer);
		}
		return list;
	}
	
	// ********************************************************************
	// * CREATE TABLE FROM ENTITY-LIST
	// ********************************************************************

	private Table<String> createEnterpriseTable(List<TradingEnterprise> list){
		Table<String> table = new Table<>();
		table.addHeader("EnterpriseId","EnterpriseName","SupplierId","SupplierName");
		int row = 0;
		for (TradingEnterprise nextEnterprise : list) {
			Collection<ProductSupplier> suppliers = nextEnterprise.getSuppliers();
			if (suppliers.isEmpty()) {
				table.addColumn(row, 0, String.valueOf(nextEnterprise.getId()), true);
				table.addColumn(row, 1, nextEnterprise.getName(), true);
				table.addColumn(row, 2, "N/A",true);
				table.addColumn(row, 3, "N/A",true);
				row++;
			}
			
			for (ProductSupplier supplier : nextEnterprise.getSuppliers()) {
				table.addColumn(row, 0, String.valueOf(nextEnterprise.getId()), true);
				table.addColumn(row, 1, nextEnterprise.getName(), true);
				table.addColumn(row, 2, String.valueOf(supplier.getId()),true);
				table.addColumn(row, 3, supplier.getName(),true);
				row++;
			}
		}
		return table;
	}
	
	private Table<String> createStoreTable(List<Store> list){
		Table<String> table = new Table<>();
		table.addHeader("EnterpriseName","StoreId","StoreName","StoreLocation");
		int len = list.size();
		for (int i = 0; i < len; i++) {
			table.addColumn(i, 0, list.get(i).getEnterprise().getName(), true);
			table.addColumn(i, 1, String.valueOf(list.get(i).getId()), true);
			table.addColumn(i, 2, list.get(i).getName(), true);
			table.addColumn(i, 3, list.get(i).getLocation(), true);
		}
		return table;
	}
	
	private Table<String> createProductTable(List<Product> list) {
		Table<String> table = new Table<>();
		table.addHeader("ProductBarcode","ProductName","ProductPurchasePrice");
		int len = list.size();
		for (int i = 0; i < len; i++) {
			table.addColumn(i, 0, String.valueOf(list.get(i).getBarcode()), true);
			table.addColumn(i, 1, list.get(i).getName(), true);
			table.addColumn(i, 2, String.valueOf(list.get(i).getPurchasePrice()), true);
			//TODO supplier should also be added!
		}
		return table;
	}
	
	private Table<String> createProductSupplierTable(List<ProductSupplier> list) {
		Table<String> table = new Table<>();
//		table.addHeader("ProductSupplierId","ProductSupplierName","ProductBarcode","ProductName");
		table.addHeader("ProductSupplierId","ProductSupplierName");
		int row = 0;
		for (ProductSupplier nextProductSupplier : list) {
			table.addColumn(row, 0, String.valueOf(nextProductSupplier.getId()), true);
			table.addColumn(row, 1, nextProductSupplier.getName(), true);
			row++;
//			if (nextProductSupplier.getProducts().size() > 0) {
//				int len = nextProductSupplier.getProducts().size();
//				
//				for (Product nextProduct : nextProductSupplier.getProducts()) {
//					table.addColumn(row, 0, nextProductSupplier.getName(), true);
//					table.addColumn(row, 1, String.valueOf(nextProduct.getBarcode()), true);
//					table.addColumn(row, 2, nextProduct.getName(), true);
//					row++;
//				}
//			} else{
//				table.addColumn(row, 0, nextProductSupplier.getName(), true);
//				table.addColumn(row, 1, "", true);
//				table.addColumn(row, 2, "", true);
//				row++;
//			}
		}
		return table;
	}
	
	private Table<String> createStockItemTable(List<StockItem> list){
		Table<String> table = new Table<>();
		table.addHeader("EnterpriseName",
				"StoreName","StoreLocation","ProductBarcode",
				"StockItemId","StockItemMinStock","StockItemMaxStock",
				"StockItemIncomingAmount","StockItemAmount",
				"StockItemSalesPrice");
		int row = 0;
		for(StockItem nextStockItem:list){
			table.addColumn(row, 0, nextStockItem.getStore().getEnterprise().getName(), true);
			table.addColumn(row, 1, nextStockItem.getStore().getName(), true);
			table.addColumn(row, 2, nextStockItem.getStore().getLocation(), true);
			table.addColumn(row, 3, String.valueOf(nextStockItem.getProduct().getBarcode()), true);
			table.addColumn(row, 4, String.valueOf(nextStockItem.getId()), true);
			table.addColumn(row, 5, String.valueOf(nextStockItem.getMinStock()), true);
			table.addColumn(row, 6, String.valueOf(nextStockItem.getMaxStock()), true);
			table.addColumn(row, 7, String.valueOf(nextStockItem.getIncomingAmount()), true);
			table.addColumn(row, 8, String.valueOf(nextStockItem.getAmount()), true);
			table.addColumn(row, 9, String.valueOf(nextStockItem.getSalesPrice()), true);
			row++;
		}
		return table;
	}
	
	
	private Table<String> createProductOrderTable(List<ProductOrder> list){
		Table<String> table = new Table<>();
		table.addHeader("ProductOrderId","EnterpriseName",
				"StoreName","StoreLocation","ProductBarcode",
				"OrderDeliveryDate","OrderOrderingDate",
				"OrderAmount");
		int row = 0;
		for(ProductOrder nextOrder:list){
			for(OrderEntry nextOrderEntry:nextOrder.getOrderEntries()){
				table.addColumn(row, 0, String.valueOf(nextOrder.getId()), true);
				table.addColumn(row, 1, nextOrder.getStore().getEnterprise().getName(), true);
				table.addColumn(row, 2, nextOrder.getStore().getName(), true);
				table.addColumn(row, 3, nextOrder.getStore().getLocation(), true);
				table.addColumn(row, 4, String.valueOf(nextOrderEntry.getProduct().getBarcode()), true);
				table.addColumn(row, 5, TimeUtils.convertToStringDate(nextOrder.getDeliveryDate()), true);
				table.addColumn(row, 6, TimeUtils.convertToStringDate(nextOrder.getOrderingDate()), true);
				table.addColumn(row, 7, String.valueOf(nextOrderEntry.getAmount()), true);
				row++;
			}
		}
		return table;
	}
	
	private Table<String> createUserTable(List<LoginUser> list){
		Table<String> table = new Table<>();
		table.addHeader("UserId","UserName",
				"CredentialType","CredentialString","Role");
		
		int row = 0;
		
		for(LoginUser nextUser : list){
			Collection<AbstractCredential> credentials  = nextUser.getCredentials().values();
			Set<Role> roles = nextUser.getRoles();
			
			Iterator<AbstractCredential> credIterator = credentials.iterator();
			Iterator<Role> roleIterator = roles.iterator();
			
			ICredential currCred = null;
			Role currRole = null;
			
			while(credIterator.hasNext() || roleIterator.hasNext()){
				if (credIterator.hasNext()) currCred = credIterator.next();
				if (roleIterator.hasNext()) currRole = roleIterator.next();
				
				table.addColumn(row, 0, String.valueOf(nextUser.getId()), true);
				table.addColumn(row, 1, nextUser.getUsername(), true);
				table.addColumn(row, 2, (currCred == null ? "" : currCred.getType().label()), true);
				table.addColumn(row, 3, (currCred == null ? "" : currCred.getCredentialString()), true);
				table.addColumn(row, 4, (currRole == null ? "" : currRole.label()), true);
				row++;
			}
		}
		return table;
	}
	
	private Table<String> createCustomerTable(List<Customer> list){
		Table<String> table = new Table<>();
		table.addHeader("CustomerId","FirstName",
				"LastName","MailAddress","CreditCardInfo",
				"PreferredStoreEnterpriseName", "PreferredStoreId", 
				"PreferredStoreName", "PreferredStoreLocation", 
				"UserId", "UserName");
		
		int row = 0;
		
		for (Customer nextCustomer : list) {
			if (nextCustomer.getCreditCardInfo().size() > 0) {
				for (String creditCardInfo : nextCustomer.getCreditCardInfo()) {
					createCustomerEntry(table, row, nextCustomer, creditCardInfo);
					row++;
				}
			} else {
				createCustomerEntry(table, row, nextCustomer, "null");
				row++;
			}
		}
		return table;
	}

	private void createCustomerEntry(Table<String> table, int row, Customer nextCustomer, String creditCardInfo) {
		table.addColumn(row, 0, String.valueOf(nextCustomer.getId()), true);
		table.addColumn(row, 1, nextCustomer.getFirstName(), true);
		table.addColumn(row, 2, nextCustomer.getLastName(), true);
		table.addColumn(row, 3, nextCustomer.getMailAddress(), true);
		table.addColumn(row, 4, creditCardInfo, true);
		table.addColumn(row, 5,
				nextCustomer.getPreferredStore() == null ? "null" : String.valueOf(nextCustomer.getPreferredStore().getEnterprise().getName()), true);
		table.addColumn(row, 5, nextCustomer.getPreferredStore() == null ? "null" : String.valueOf(nextCustomer.getPreferredStore().getId()), true);
		table.addColumn(row, 5, nextCustomer.getPreferredStore() == null ? "null" : String.valueOf(nextCustomer.getPreferredStore().getName()), true);
		table.addColumn(row, 5, nextCustomer.getPreferredStore() == null ? "null" : String.valueOf(nextCustomer.getPreferredStore().getLocation()),
				true);
		table.addColumn(row, 6, String.valueOf(nextCustomer.getUser().getId()), true);
		table.addColumn(row, 7, nextCustomer.getUser().getUsername(), true);
	}
	
	// ********************************************************************
	// * GENERIC UTILITY
	// ********************************************************************
	
	/**
	 * Create a {@link Table} object based on the provided string content. The
	 * content can have following format:<br>
	 * <ul>
	 * <li>XML (based on {@link Table} schema)</li>
	 * <li>CSV</li>
	 * </ul>
	 * 
	 * @param content
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Table<String> createTable(String content){
		//TODO debug
		System.out.println("Starting creating Table");
		CSVParser parser = new CSVParser();
		Table<String> table = null;
		
		switch (this.contentTypeFormat.toLowerCase()) {
		case CONTENT_TYPE_CSV:
			parser.parse(content);
			table = parser.getModel();
			break;

		default:
			JAXBEngine engine = JAXBEngine.getInstance();
			JAXBElement<Table<String>> obj = (JAXBElement<Table<String>>) 
					engine.read(content, TableObjectFactory.class);
			table = obj.getValue();
			break;
		}
		//TODO debug
		System.out.println("Table created\n");
		parser.setModel(table);
		System.out.println(parser.toString());
		return table;
	}
	
	/**
	 * Include the notification into the message object
	 * @param table
	 * @param message
	 */
	private void includeNotification(Table<String> table, Message message){
		CSVParser parser = new CSVParser();
		parser.setModel(table);
		message.appendBody("Notification", parser.toString());
	}
	
	private String getBaseUrl(HttpServletRequest request){
		return request.getScheme() 
				+ "://" + request.getServerName()
				+ ":" + request.getServerPort() 
				+ request.getContextPath();
	}

}
