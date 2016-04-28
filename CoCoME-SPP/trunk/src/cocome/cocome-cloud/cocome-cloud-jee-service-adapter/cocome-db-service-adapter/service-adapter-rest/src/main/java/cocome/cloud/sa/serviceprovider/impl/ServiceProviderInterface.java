package cocome.cloud.sa.serviceprovider.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;

import sun.misc.BASE64Decoder;

import cocome.cloud.sa.serviceprovider.ServiceProvider;
import cocome.cloud.sa.serviceprovider.ServiceProviderCatalog;
import cocome.cloud.sa.serviceprovider.ServiceProviderObjectFactory;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import de.kit.ipd.java.utils.io.Utilities;
import de.kit.ipd.java.utils.xml.JAXBEngine;

/**
 * Provides all service provider
 */
@WebServlet("/ServiceProviderInterface")
public class ServiceProviderInterface extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String pathTypesSchema = "/WEB-INF/schemas/types.xsd";
	
	public static final String URL_SERVICE_BASE = "/Services";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServiceProviderInterface() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	@GET
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	PrintWriter writer = response.getWriter();
    	
    	//TODO later..for testing purposes disabled
//    	if(!checkUser(request,response)){
//    		return;
//    	}
    	
		String urlBase = getBaseUrl(request);
		
		ServiceProviderCatalog catalog = new ServiceProviderCatalog();
		
		ServiceProvider spDatabase = new ServiceProvider();
		spDatabase.setName(ServiceProviderDatabase.NAME_SERVICE_PROVIDER_DATABASE);
		spDatabase.setUrl(urlBase 
				+ URL_SERVICE_BASE 
				+ ServiceProviderDatabase.URL_SERVICE_PROVIDER_DATABASE);
		
		ServiceProvider spCashier = new ServiceProvider();
		spCashier.setName("BookSale");
		spCashier.setUrl(urlBase 
				+ URL_SERVICE_BASE 
				+ "/BookSale/ServiceProviderBookSale");
		
		
		catalog.getListServiceProvider().add(spCashier);
		catalog.getListServiceProvider().add(spDatabase);
		
		response.setContentType("text/xml");
		
		writer.append(getRespond(catalog));
		writer.close();
	}
	/*************************************************************************
	 * PRIVATE
	 ************************************************************************/

    private boolean checkUser(HttpServletRequest request, HttpServletResponse responses){
    	String authentification = request.getHeader("Authorization");
    	
    	if(authentification==null || authentification.isEmpty()
    			|| !authentification.toUpperCase().startsWith("BASIC")
    			){
    		responses.setHeader("WWW-Authenticate", "Not Authorized");
    		try {
				responses.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			} catch (IOException e) {
				e.printStackTrace();
			}
    		return false;
    	}
    	String userpassEncoded = authentification.substring(6);
    	BASE64Decoder decoder = new BASE64Decoder();
    	try {
			String userpassDecoded = new String(decoder.decodeBuffer(userpassEncoded));
			
			//Check user and password are allowed
			//TODO check the password here
			System.out.println(userpassDecoded);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    }
    
	private String getSchema(){
		ServletContext ctx = getServletContext();
		InputStream inputSchema = ctx.getResourceAsStream(pathTypesSchema);
		String schema = Utilities.getString(inputSchema); //TODO do later
		return schema;
	}
	
	
	private String getRespond(ServiceProviderCatalog catalog){
		JAXBEngine engine = JAXBEngine.getInstance();
		ByteOutputStream out = new ByteOutputStream();
		engine.write(catalog, out, ServiceProviderObjectFactory.class);
		String strRespond = out.toString();
		out.close();
		return strRespond;
	}
	
	
	private String getBaseUrl(HttpServletRequest request){
		return request.getScheme() 
				+ "://" + request.getServerName()
				+ ":" + request.getServerPort() 
				+ request.getContextPath();
	}
	
}
