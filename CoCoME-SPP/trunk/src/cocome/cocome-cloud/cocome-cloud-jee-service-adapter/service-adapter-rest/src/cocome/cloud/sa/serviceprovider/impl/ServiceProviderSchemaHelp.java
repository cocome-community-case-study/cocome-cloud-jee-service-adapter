package cocome.cloud.sa.serviceprovider.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cocome.cloud.sa.entities.Message;
import de.kit.ipd.java.utils.framework.table.Table;
import de.kit.ipd.java.utils.io.Utilities;
import de.kit.ipd.java.utils.parsing.html.HtmlParser;

/**
 * This class provides all schemas, syntax and other important resources which
 * are related to the service-provider-interface. With this information, user
 * can interactive learn the functionality of the service provider
 * 
 * @author Alessandro Giusa, alessandrogiusa@gmail.com
 *
 */
public class ServiceProviderSchemaHelp {
	
	/**
	 * Get the schemas of the service provider
	 * @param request
	 * @param response
	 * @param message
	 * @param ctx
	 */
	public static void getSchemas(HttpServletRequest request,
			HttpServletResponse response, ServletContext ctx){
		
		PrintWriter writer;
		try {
			writer = response.getWriter();
			HtmlParser parser = new HtmlParser();

			// Enterprise
			writer.append("<p>For Enterprise creation</p>");
			writer.append("<p>EntityName=TradingEnterprise</p>");
			Table<String> t1 = new Table<>();
			t1.addHeader("EnterpriseName");
			parser.setTable(t1);
			writer.append(parser.toString());
			writer.append("<div id=\"EnterpriseCreation\">" + "EnterpriseName"
					+ "</div>");

			writer.append("<p>For Enterprise update</p>");
			Table<String> t2 = new Table<>();
			t2.addHeader("EnterpriseId", "EnterpriseName");
			parser.setTable(t2);
			writer.append(parser.toString());
			writer.append("<div id=\"EnterpriseUpdate\">"
					+ "EnterpriseId;EnterpriseName" + "</div>");

			// Store
			writer.append("<p>For Store creation</p>");
			writer.append("<p>EntityName=Store</p>");
			Table<String> t3 = new Table<>();
			t3.addHeader("EnterpriseName", "StoreName", "StoreLocation");
			parser.setTable(t3);
			writer.append(parser.toString());
			writer.append("<div id=\"StoreCreation\">"
					+ "EnterpriseName;StoreName;StoreLocation" + "</div>");

			writer.append("<p>For Store update</p>");
			Table<String> t4 = new Table<>();
			t4.addHeader("StoreId", "StoreName", "StoreLocation");
			parser.setTable(t4);
			writer.append(parser.toString());
			writer.append("<div id=\"StoreUpdate\">"
					+ "StoreId;StoreName;StoreLocation" + "</div>");

			// Product
			writer.append("<p>For Product creation</p>");
			writer.append("<p>EntityName=Product</p>");
			Table<String> t5 = new Table<>();
			t5.addHeader("ProductBarcode", "ProductName",
					"ProductPurchasePrice");
			parser.setTable(t5);
			writer.append(parser.toString());
			writer.append("<div id=\"ProductCreation\">"
					+ "ProductBarcode;ProductName;ProductPurchasePrice"
					+ "</div>");

			writer.append("<p>For Product update</p>");
			Table<String> t6 = new Table<>();
			t6.addHeader("ProductBarcode", "ProductName",
					"ProductPurchasePrice");
			parser.setTable(t6);
			writer.append(parser.toString());
			writer.append("<div id=\"ProductUpdate\">"
					+ "ProductBarcode;ProductName;ProductPurchasePrice"
					+ "</div>");

			// StockItem
			writer.append("<p>For StockItem creation</p>");
			writer.append("<p>EntityName=StockItem</p>");
			Table<String> t7 = new Table<>();
			t7.addHeader("StoreId", "ProductBarcode", "StockItemMinStock",
					"StockItemMaxStock", "StockItemIncomingAmount",
					"StockItemAmount", "StockItemSalesPrice");
			parser.setTable(t7);
			writer.append(parser.toString());
			writer.append("<div id=\"StockItemCreation\">"
					+ "StoreId;ProductBarcode;StockItemMinStock;StockItemMaxStock;"
					+ "StockItemIncomingAmount;StockItemAmount;StockItemSalesPrice"
					+ "</div>");

			writer.append("<p>For StockItem update</p>");
			Table<String> t8 = new Table<>();
			t8.addHeader("StoreId", "ProductBarcode", "StockItemMinStock",
					"StockItemMaxStock", "StockItemIncomingAmount",
					"StockItemAmount", "StockItemSalesPrice");
			parser.setTable(t8);
			writer.append(parser.toString());
			writer.append("<div id=\"StockItemCreation\">"
					+ "StoreId;ProductBarcode;StockItemMinStock;StockItemMaxStock;"
					+ "StockItemIncomingAmount;StockItemAmount;StockItemSalesPrice"
					+ "</div>");

			// ProductSupplier
			writer.append("<p>For ProductSupplier creation</p>");
			writer.append("<p>EntityName=ProductSupplier</p>");
			Table<String> t9 = new Table<>();
			t9.addHeader("ProductSupplierName");
			parser.setTable(t9);
			writer.append(parser.toString());
			writer.append("<div id=\"ProductSupplierCreation\">"
					+ "ProductSupplierName" + "</div>");

			writer.append("<p>For ProductSupplier update</p>");
			Table<String> t10 = new Table<>();
			t10.addHeader("ProductSupplierId", "ProductSupplierName");
			parser.setTable(t10);
			writer.append(parser.toString());
			writer.append("<div id=\"ProductSupplierUpdate\">"
					+ "ProductSupplierId;ProductSupplierName" + "</div>");

			// ProductOrder
			writer.append("<p>For ProductOrder creation</p>");
			writer.append("<p>EntityName=ProductOrder</p>");
			Table<String> t11 = new Table<>();
			t11.addHeader("ProductOrderId", "StoreId", "ProductBarcode",
					"OrderDeliveryDate", "OrderOrderingDate", "OrderAmount");
			parser.setTable(t11);
			writer.append(parser.toString());
			writer.append("<div id=\"ProductOrderCreation\">"
					+ "ProductOrderId;StoreId;ProductBarcode;OrderDeliveryDate;OrderOrderingDate;OrderAmount"
					+ "</div>");

			writer.append("<p>For ProductOrder update</p>");
			Table<String> t12 = new Table<>();
			t12.addHeader("ProductOrderId", "StoreId", "ProductBarcode",
					"OrderDeliveryDate", "OrderOrderingDate", "OrderAmount");
			parser.setTable(t12);
			writer.append(parser.toString());
			writer.append("<div id=\"ProductOrderCreation\">"
					+ "ProductOrderId;StoreId;ProductBarcode;OrderDeliveryDate;OrderOrderingDate;OrderAmount"
					+ "</div>");

			//Table schema
			writer.append("---------------------------------------------------");
			writer.append("<p>Table schema for xml response of server</p>");
			InputStream resourceContent = ctx
					.getResourceAsStream("/WEB-INF/res/schemas/Table.xsd");
			String tableschema = Utilities.getString(resourceContent);
			System.out.println(tableschema);
			tableschema = tableschema.replaceAll("<", "&lt;");
			tableschema = tableschema.replaceAll(">", "&gt;");
			writer.append("<div id=\"TableSchema\"><pre>"+tableschema+"</pre></div>");
			resourceContent.close();
			
			//Queries
			writer.append("---------------------------------------------------");
			//Insert
			writer.append("<p><b>Syntax for queries</b></p>");
			writer.append("<p><b>Insert-Query</b></p>");
			writer.append("<p>query.insert=[EntityName]</p>");
			writer.append("<p>content is expected over the input stream");
			writer.append("<p>Example: query.insert=Store");
			
			writer.append("<p><b>Update-Query</b></p>");
			writer.append("<p>query.update=[EntityName]</p>");
			writer.append("<p>content is expected over the input stream");
			writer.append("<p>Example: query.update=Store");
			
			writer.append("<p><b>Select-Query</b></p>");
			writer.append("<p>query.select=entity.type=[EntityName];[Condition];[Condition]...</p>");
			writer.append("<p>content is expected over the input stream");
			writer.append("<p>Example1: <i>query.select=entity.type=Store;store.name=like'*'</i>");
			writer.append("<p>the example 1 will query all stores.");
			
			writer.append("<p>Example2: <i>query.select=entity.type=ProductOrder;productorder.deliveryDate=< '2014-01-20'</i>");
			writer.append("<p>the example 2 will query all product order where the delivery data is less then the given one");
			
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
