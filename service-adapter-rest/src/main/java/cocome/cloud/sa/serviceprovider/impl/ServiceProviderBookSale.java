package cocome.cloud.sa.serviceprovider.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import de.kit.ipd.java.utils.xml.JAXBEngine;
import de.kit.ipd.java.utils.xml.XML;

import cocome.cloud.sa.entities.Message;
import cocome.cloud.sa.serviceprovider.Service;
import cocome.cloud.sa.serviceprovider.ServiceProvider;

/**
 * Provides all service provider
 */
@WebServlet("/BookSale/ServiceProviderBookSale")
public class ServiceProviderBookSale extends HttpServlet {

	public static final String URL_SERVICE_PROVIDER_BOOK_SALE = "/BookSale/ServiceProviderBookSale";
	public static final String NAME_SERVICE_PROVIDER_BOOK_SALE = "Database";

	public static final String URL_SERVICE_BOOK_SALE_GETDATA = "/BookSale/Get";
	public static final String NAME_SERVICE_BOOK_SALE_GETDATA = "GetAllProducts";

	public static final String URL_SERVICE_BOOK_SALE_SETDATA = "/BookSale/Set";
	public static final String NAME_SERVICE_BOOK_SALE_SETDATA = "OrderProducts";

	public static final String URL_SERVICE_BASE = "/Services";

	private static final String CONTENT_TYPE_CSV = "application/csv";
	private static final String CONTENT_TYPE_XML = "application/xml";

	private static final long serialVersionUID = 1L;

	private static final String USER_AGENT = "Mozilla/5.0";

	/** Content-Type format */
	private String contentTypeFormat = CONTENT_TYPE_XML;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServiceProviderBookSale() {
		super();
	}

	@POST
	@Override
	public void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		final String requestedUri = request.getRequestURI();

		this.contentTypeFormat = request.getHeader("Content-Type");
		if (this.contentTypeFormat != null) {
			this.contentTypeFormat = request.getContentType();
			if (this.contentTypeFormat.contains(";")) {
				this.contentTypeFormat = this.contentTypeFormat.substring(0,
						this.contentTypeFormat.indexOf(";", 0)).trim().replaceAll(" ", "");
			}
		} else {
			this.contentTypeFormat = CONTENT_TYPE_XML;
		}

		if (requestedUri.endsWith(URL_SERVICE_PROVIDER_BOOK_SALE)) {
			ServiceProviderDescriptor.getDescription(request, response);

		} else if (requestedUri.endsWith(URL_SERVICE_BOOK_SALE_SETDATA)) {
			this.dispatchDoPostRequest(request, response);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@GET
	@Override
	public void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException, IOException {

		final String requestedUri = request.getRequestURI();

		this.contentTypeFormat = request.getHeader("Content-Type");
		if (this.contentTypeFormat != null) {
			this.contentTypeFormat = request.getContentType();
			if (this.contentTypeFormat.contains(";")) {
				this.contentTypeFormat = this.contentTypeFormat.substring(0,
						this.contentTypeFormat.indexOf(";", 0)).trim().replaceAll(" ", "");
			}
		} else {
			this.contentTypeFormat = CONTENT_TYPE_XML;
		}

		if (requestedUri.endsWith(URL_SERVICE_PROVIDER_BOOK_SALE)) {
			ServiceProviderDescriptor.getDescription(request, response);

		} else if (requestedUri.endsWith(URL_SERVICE_BOOK_SALE_GETDATA)) {
			this.dispatchDoGetRequest(request, response);
		}
	}

	private void dispatchDoPostRequest(final HttpServletRequest request,
			final HttpServletResponse response) {
		try {

			final String content = this.getContent(request);
			if (content == null || content.isEmpty())
				throw new NullPointerException("sent content is null or empty!");

			final Message msg = this.orderProducts(content, request);
			// TODO check if all entries are SUCCESS
			response.setStatus(HttpURLConnection.HTTP_OK);
		} catch (final Exception e) { // NOCS
			e.printStackTrace();
			// TODO this has to be encapsulated into a Message Object. For 1.Iteration it is okay like
			// this.
			response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
			try (PrintWriter writer = response.getWriter()) {
				e.printStackTrace(writer);
			} catch (final IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void dispatchDoGetRequest(final HttpServletRequest request,
			final HttpServletResponse response) {

		try {
			final String allProducts = this.getAllProducts(request);
			if (allProducts != null) {
				response.setStatus(HttpURLConnection.HTTP_OK);

				try (PrintWriter writer = response.getWriter()) {
					writer.append(allProducts);
					writer.flush();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		} catch (final Exception e) { // NOCS
			e.printStackTrace();
			// TODO this has to be encapsulated into a Message Object. For 1.Iteration it is okay like
			// this.
			response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
			try (PrintWriter writer = response.getWriter()) {
				e.printStackTrace(writer);
			} catch (final IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private String getAllProducts(final HttpServletRequest request) {
		final String queryAllProducts = "?query.select=entity.type=Product;product.name=%20Like%20'*'";
		final String baseUrl = this.getBaseUrl(request);
		final String url = baseUrl
				+ URL_SERVICE_BASE
				+ ServiceProviderDatabase.URL_SERVICE_DATABASE_GETDATA
				+ queryAllProducts;
		final Message resultMessage = this.doGetRequest(url);
		final String resultContent = resultMessage.getResultBodyContent();
		return resultContent;
	}

	private Message orderProducts(final String content, final HttpServletRequest request) {
		if (!this.contentTypeFormat.equalsIgnoreCase(CONTENT_TYPE_CSV)
				&& !this.contentTypeFormat.equalsIgnoreCase(CONTENT_TYPE_XML))
			throw new IllegalArgumentException("'Content-Type' has to be either"
					+ " application/csv or appclication/xml");

		final String queryAllProducts = "?query.insert=productorder";
		final String baseUrl = this.getBaseUrl(request);
		final String url = baseUrl
				+ URL_SERVICE_BASE
				+ ServiceProviderDatabase.URL_SERVICE_DATABASE_SETDATA
				+ queryAllProducts;

		final Message resultMessage = this.doPostRequest(url, content);
		final XML xmlMsg = JAXBEngine.getInstance().write(resultMessage, Message.class);
		System.out.println(xmlMsg.toString());
		return resultMessage;
	}

	// ********************************************************************
	// * HTTP-CONNECTIONS
	// ********************************************************************

	private String getContent(final HttpServletRequest request) {
		try (BufferedReader br = request.getReader()) {
			final StringBuilder builder = new StringBuilder();
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				builder.append(inputLine);
				builder.append(System.lineSeparator());
			}
			final String data = builder.toString();
			return data;
		} catch (final Exception e) { // NOCS
			e.printStackTrace();
			return null;
		}
	}

	private Message doGetRequest(final String url) {
		final String result = ServiceProviderBookSale.doHttpGet(url);
		final Message msg = (Message) JAXBEngine.getInstance().read(result, Message.class);
		return msg;
	}

	private Message doPostRequest(final String url, final String content) {
		final String result = ServiceProviderBookSale.doHttpPost(url, content);
		final Message msg = (Message) JAXBEngine.getInstance().read(result, Message.class);
		return msg;
	}

	private String getBaseUrl(final HttpServletRequest request) {
		return request.getScheme()
				+ "://" + request.getServerName()
				+ ":" + request.getServerPort()
				+ request.getContextPath();
	}

	// ********************************************************************
	// * HTTP-UTILITY-CONNECTIONS
	// ********************************************************************

	private static HttpURLConnection getConnection(final URL url) throws IOException {
		if (url.getProtocol().equals("https"))
			return (HttpsURLConnection) url.openConnection();
		return (HttpURLConnection) url.openConnection();
	}

	public static String doHttpGet(final String strUrl) {
		try {
			final URL url = new URL(strUrl);

			final HttpURLConnection con = ServiceProviderBookSale.getConnection(url);
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", CONTENT_TYPE_CSV);
			con.setRequestProperty("User-Agent", USER_AGENT);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));

			final String lineSeparator = System.getProperty("line.separator");

			final StringBuilder builder = new StringBuilder();
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				builder.append(inputLine);
				builder.append(lineSeparator);
			}

			reader.close();

			return builder.toString();

		} catch (final MalformedURLException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String doHttpPost(final String strUrl, final String params) {
		try {

			final URL url = new URL(strUrl);
			final HttpURLConnection con = ServiceProviderBookSale.getConnection(url);
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Content-Type", CONTENT_TYPE_CSV);
			con.setRequestProperty("CONTENT_LENGTH", String.valueOf(params.getBytes().length));

			// post the xml
			final DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(params);
			wr.flush();
			wr.close();

			// read answer
			final BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

			final String lineSeparator = System.getProperty("line.separator");
			final StringBuilder builder = new StringBuilder();
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				builder.append(inputLine);
				builder.append(lineSeparator);
			}
			br.close();
			return builder.toString();
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		} catch (final ProtocolException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 * @author unknown
	 *
	 */
	private static final class ServiceProviderDescriptor {

		private ServiceProviderDescriptor() {}

		public static void getDescription(final HttpServletRequest request,
				final HttpServletResponse response) throws IOException {

			final String urlBase = request.getScheme()
					+ "://" + request.getServerName()
					+ ":" + request.getServerPort()
					+ request.getContextPath();

			final ServiceProvider spBookSale = new ServiceProvider();
			spBookSale.setName(NAME_SERVICE_PROVIDER_BOOK_SALE);
			spBookSale.setUrl(urlBase + ServiceProviderInterface.URL_SERVICE_BASE
					+ URL_SERVICE_PROVIDER_BOOK_SALE);

			final Service getData = new Service();
			getData.setName(NAME_SERVICE_BOOK_SALE_GETDATA);
			getData.setUrl(urlBase + ServiceProviderInterface.URL_SERVICE_BASE
					+ URL_SERVICE_BOOK_SALE_GETDATA);

			final Service setData = new Service();
			setData.setName(NAME_SERVICE_BOOK_SALE_SETDATA);
			setData.setUrl(urlBase + ServiceProviderInterface.URL_SERVICE_BASE
					+ URL_SERVICE_BOOK_SALE_SETDATA);

			spBookSale.getServices().add(getData);
			spBookSale.getServices().add(setData);

			response.setContentType("text/xml");
			final PrintWriter writer = response.getWriter();
			writer.append(JAXBEngine.getInstance().write(spBookSale).toString());
			writer.close();
		}
	}
}
