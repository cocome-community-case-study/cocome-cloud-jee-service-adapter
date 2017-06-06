package cocome.cloud.sa.serviceprovider.impl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;


import com.sun.xml.messaging.saaj.util.ByteOutputStream;

import de.kit.ipd.java.utils.xml.JAXBEngine;

import cocome.cloud.sa.serviceprovider.ServiceProvider;
import cocome.cloud.sa.serviceprovider.ServiceProviderCatalog;
import cocome.cloud.sa.serviceprovider.ServiceProviderObjectFactory;

/**
 * Provides all service provider
 */
@WebServlet("/ServiceProviderInterface")
public class ServiceProviderInterface extends HttpServlet {

	static final String URL_SERVICE_BASE = "/Services";

	private static final long serialVersionUID = 1L;

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
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		final PrintWriter writer = response.getWriter();

		// TODO later..for testing purposes disabled
		// if(!checkUser(request,response)){
		// return;
		// }

		final String urlBase = this.getBaseUrl(request);

		final ServiceProviderCatalog catalog = new ServiceProviderCatalog();

		final ServiceProvider spDatabase = new ServiceProvider();
		spDatabase.setName(ServiceProviderDatabase.NAME_SERVICE_PROVIDER_DATABASE);
		spDatabase.setUrl(urlBase
				+ URL_SERVICE_BASE
				+ ServiceProviderDatabase.URL_SERVICE_PROVIDER_DATABASE);

		final ServiceProvider spCashier = new ServiceProvider();
		spCashier.setName("BookSale");
		spCashier.setUrl(urlBase
				+ URL_SERVICE_BASE
				+ "/BookSale/ServiceProviderBookSale");

		catalog.getListServiceProvider().add(spCashier);
		catalog.getListServiceProvider().add(spDatabase);

		response.setContentType("text/xml");

		writer.append(this.getRespond(catalog));
		writer.close();
	}

	/*************************************************************************
	 * PRIVATE
	 ************************************************************************/

	private String getRespond(final ServiceProviderCatalog catalog) {
		final JAXBEngine engine = JAXBEngine.getInstance();
		final ByteOutputStream out = new ByteOutputStream();
		engine.write(catalog, out, ServiceProviderObjectFactory.class);
		final String strRespond = out.toString();
		out.close();
		return strRespond;
	}

	private String getBaseUrl(final HttpServletRequest request) {
		return request.getScheme()
				+ "://" + request.getServerName()
				+ ":" + request.getServerPort()
				+ request.getContextPath();
	}

}
