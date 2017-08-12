package cocome.cloud.sa.serviceprovider.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBElement;

import org.cocome.tradingsystem.remote.access.dao.DataAccessObject;
import org.cocome.tradingsystem.remote.access.dao.enterprise.ProductDAO;
import org.cocome.tradingsystem.remote.access.dao.enterprise.ProductSupplierDAO;
import org.cocome.tradingsystem.remote.access.dao.enterprise.TradingEnterpriseDAO;
import org.cocome.tradingsystem.remote.access.dao.plant.PlantDAO;
import org.cocome.tradingsystem.remote.access.dao.store.ProductOrderDAO;
import org.cocome.tradingsystem.remote.access.dao.store.StockItemDAO;
import org.cocome.tradingsystem.remote.access.dao.store.StoreDAO;
import org.cocome.tradingsystem.remote.access.dao.usermanager.CustomerDAO;
import org.cocome.tradingsystem.remote.access.dao.usermanager.LoginUserDAO;
import de.kit.ipd.java.utils.framework.table.Table;
import de.kit.ipd.java.utils.framework.table.TableObjectFactory;
import de.kit.ipd.java.utils.parsing.csv.CSVParser;
import de.kit.ipd.java.utils.time.TimeUtils;
import de.kit.ipd.java.utils.xml.JAXBEngine;

import de.kit.ipd.java.utils.xml.XML;
import org.cocome.tradingsystem.remote.access.DatabaseAccess;
import org.cocome.tradingsystem.remote.access.Notification;

import cocome.cloud.sa.entities.Message;
import cocome.cloud.sa.entities.MessageEntry;
import cocome.cloud.sa.query.IQueryConst;
import cocome.cloud.sa.query.parsing.QueryParser;
import cocome.cloud.sa.serviceprovider.Service;
import cocome.cloud.sa.serviceprovider.ServiceProvider;

/**
 * Servlet implementation for the ServiceProviderDatabase-Service. This Service
 * provides access to the database
 *
 * @author Alessandro Giusa, alessandrogiusa@gmail.com
 */
@WebServlet("/Database/ServiceProviderDatabase")
public class ServiceProviderDatabase extends HttpServlet {

    private static final long serialVersionUID = 1L;

    static final String URL_SERVICE_PROVIDER_DATABASE = "/Database/ServiceProviderDatabase";
    static final String NAME_SERVICE_PROVIDER_DATABASE = "Database";

    static final String URL_SERVICE_DATABASE_GETDATA = "/Database/GetData";
    static final String NAME_SERVICE_DATABASE_GETDATA = "GetData";

    static final String URL_SERVICE_DATABASE_SETDATA = "/Database/SetData";
    static final String NAME_SERVICE_DATABASE_SETDATA = "SetData";

    private static final String MESSAGE_ENTRY_RESULT = "result";

    private static final String CONTENT_TYPE_CSV = "application/csv";
    private static final String CONTENT_TYPE_XML = "application/xml";

    private static final String NAME_SERVICE_SCHEMAS = "Schemas";

    private static final String URL_SERVICE_DATABASE_SCHEMAS = "/Database/Schemas";

    @EJB
    private DatabaseAccess databaseAccess;

    @EJB
    private ProductDAO productDAO;

    @EJB
    private PlantDAO plantDAO;

    @EJB
    private ProductSupplierDAO productSupplierDAO;

    @EJB
    private TradingEnterpriseDAO tradingEnterpriseDAO;

    @EJB
    private ProductOrderDAO productOrderDAO;

    @EJB
    private StockItemDAO stockItemDAO;

    @EJB
    private StoreDAO storeDAO;

    @EJB
    private CustomerDAO customerDAO;

    @EJB
    private LoginUserDAO loginUserDAO;

    private Map<String, DataAccessObject> daoMap = new HashMap<>();

    /**
     * Content-Type format
     */
    private String contentTypeFormat = CONTENT_TYPE_XML;

    @PostConstruct
    protected void initDAOMap() {
        daoMap.put(productDAO.getEntityTypeName(), productDAO);
        daoMap.put(plantDAO.getEntityTypeName(), plantDAO);
        daoMap.put(productSupplierDAO.getEntityTypeName(), productSupplierDAO);
        daoMap.put(tradingEnterpriseDAO.getEntityTypeName(), tradingEnterpriseDAO);
        daoMap.put(productOrderDAO.getEntityTypeName(), productOrderDAO);
        daoMap.put(stockItemDAO.getEntityTypeName(), stockItemDAO);
        daoMap.put(storeDAO.getEntityTypeName(), storeDAO);
        daoMap.put(customerDAO.getEntityTypeName(), customerDAO);
        daoMap.put(loginUserDAO.getEntityTypeName(), loginUserDAO);
    }

    // ********************************************************************
    // * HTTP SERVLET METHODS
    // ********************************************************************

    // TODO
    /*
	 * Problem: URLEncoding
	 * If the request url is encoded, it has to be decoded first!Otherwise the query is not
	 * working
	 */

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        dispatchWrite(request, response, IQueryConst.QUERY_INSERT);
    }

    @Override
    protected void doPut(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        dispatchWrite(request, response, IQueryConst.QUERY_UPDATE);
    }

    @Override
    protected void doDelete(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        dispatchWrite(request, response, IQueryConst.QUERY_DELETE);
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response) throws ServletException, IOException {
        final String requestedUri = request.getRequestURI();
        if (requestedUri.endsWith(URL_SERVICE_PROVIDER_DATABASE)) {
            ServiceProviderDescriptor.getDescription(request, response);

        } else if (requestedUri.endsWith(URL_SERVICE_DATABASE_GETDATA)) {
            this.dispatchQueryReadRequest(request, response);

        } else if (requestedUri.endsWith(URL_SERVICE_DATABASE_SCHEMAS)) {
            ServiceProviderSchemaHelp.getSchemas(request, response, this.getServletContext());
        }
    }

    private void dispatchWrite(final HttpServletRequest request, final HttpServletResponse response,
                               final String opt)
            throws ServletException, IOException {
        final String requestedUri = request.getRequestURI();
        if (requestedUri.endsWith(URL_SERVICE_DATABASE_SETDATA)) {
            String next;
            for (final Enumeration<String> param = request.getParameterNames(); param.hasMoreElements(); ) {
                next = param.nextElement();
                if(next.equals(opt)) {
                    this.dispatchQueryWriteRequest(next, request, response);
                }
            }
        }
    }

    // ********************************************************************
    // * QUERY-DISPATCHER
    // ********************************************************************

    /**
     * Provides following query functionality:<br>
     * <br>
     * <ul>
     * <li>{@link IQueryConst#QUERY_INSERT}</li>
     * <li>{@link IQueryConst#QUERY_UPDATE}</li>
     * <li>{@link IQueryConst#QUERY_DELETE}</li>
     * </ul>
     *
     * @param qm       the query string
     * @param request  the request object
     * @param response the response object
     * @throws IOException if the request could not be processed
     */
    private void dispatchQueryWriteRequest(final String qm,
                                           final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {

        final String requestedUri = request.getRequestURI();
        if (!requestedUri.endsWith(URL_SERVICE_DATABASE_SETDATA))
            return;
        this.contentTypeFormat = request.getHeader("Content-Type");
        this.contentTypeFormat = request.getContentType();
        if (this.contentTypeFormat.contains(";")) {
            this.contentTypeFormat = this.contentTypeFormat.substring(0,
                    this.contentTypeFormat.indexOf(";", 0)).trim().replaceAll(" ", "");
        }

        final Message message = new Message();
        message.appendHeader("RequestedTime", TimeUtils.getTime());
        message.appendHeader("RequestedUrl",
                request.getRequestURL().toString());
        message.appendHeader("RequestedParam", request.getQueryString());
        message.appendHeader("RequestedFormat", this.contentTypeFormat);

        final PrintWriter writer = response.getWriter();
        try {
            final BufferedReader br = request.getReader();
            final StringBuilder builder = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                builder.append(inputLine);
                builder.append(System.lineSeparator());
            }
            final String data = builder.toString();

            switch (qm.trim().toLowerCase()) {
                case IQueryConst.QUERY_SELECT:
                    final String queryselect = this.querySelect(request.getParameter(qm),
                            message);
                    System.out.println(queryselect);
                    message.appendBody(MESSAGE_ENTRY_RESULT, queryselect);
                    break;
                case IQueryConst.QUERY_INSERT:
                    this.queryInsert(request.getParameter(qm), data, message);
                    break;
                case IQueryConst.QUERY_UPDATE:
                    this.queryUpdate(request.getParameter(qm), data, message);
                    break;
                case IQueryConst.QUERY_DELETE:
                    // TODO delete impl
                    break;
                default:
                    message.appendBody("Error", "command " + qm + " not available!");
                    break;
            }

            br.close();
        } catch (final Exception e) { // NOCS
            e.printStackTrace();
            Throwable cause = e.getCause();
            while (cause != null) {
                message.appendBody("Error", cause.getMessage());
                cause = cause.getCause();
            }
        }
        final JAXBEngine engine = JAXBEngine.getInstance();
        final StringWriter strWriter = new StringWriter();
        engine.write(message, strWriter, Message.class, MessageEntry.class);
        final String strRes = strWriter.toString();
        writer.append(strRes);
        writer.close();
    }

    /**
     * Provides following query functionality:<br>
     * <br>
     * <ul>
     * <li>{@link IQueryConst#QUERY_SELECT}</li>
     * </ul>
     *
     * @param request  the request object
     * @param response the response object
     */
    private void dispatchQueryReadRequest(final HttpServletRequest request,
                                          final HttpServletResponse response) {

        this.contentTypeFormat = request.getContentType();
        final Message message = new Message();
        message.appendHeader("RequestedTime", TimeUtils.getTime());
        message.appendHeader("RequestedUrl",
                request.getRequestURL().toString());
        message.appendHeader("RequestedParam", request.getQueryString());
        message.appendHeader("RequestedFormat", this.contentTypeFormat);

        PrintWriter writer;
        try {
            writer = response.getWriter();
            String next;
            // iteration through all parameter
            for (final Enumeration<String> param = request.getParameterNames(); param
                    .hasMoreElements(); ) {
                next = param.nextElement();
                switch (next) {
                    case IQueryConst.QUERY_SELECT:
                        final String queryselect = this.querySelect(request.getParameter(next),
                                message);
                        message.appendBody(MESSAGE_ENTRY_RESULT, queryselect);
                        break;
                    default:
                        message.appendBody("Error", next + " not available!");
                        break;
                }
            }
        } catch (final Exception e) { // NOCS
            e.printStackTrace();
            Throwable cause = e.getCause();
            while (cause != null) {
                message.appendBody("Error", cause.getMessage());
                cause = cause.getCause();
            }
            return;
        }
        final JAXBEngine engine = JAXBEngine.getInstance();
        final StringWriter strWriter = new StringWriter();
        engine.write(message, strWriter, Message.class, MessageEntry.class);
        final String strRes = strWriter.toString();
        writer.append(strRes);
        writer.close();
    }

    // ********************************************************************
    // * QUERY WRITE
    // ********************************************************************

    /**
     * Query-Insert
     *
     * @param parameter the execution parameter
     * @param content   the query content
     * @param message   the result message
     */

    private void queryInsert(final String parameter, final String content, final Message message) {
        final String entityType = parameter.toLowerCase();
        if (!this.daoMap.containsKey(entityType)) {
            message.appendBody("Error", "parameter value "
                    + parameter + " not available!");
            return;
        }
        @SuppressWarnings("unchecked")
        DataAccessObject<Object> dao = this.daoMap.get(entityType);
        this.createEntiries(dao, content, message);
    }

    private void queryUpdate(final String parameter, final String content, final Message message) {
        final String entityType = parameter.toLowerCase();
        if (!this.daoMap.containsKey(entityType)) {
            message.appendBody("Error", "parameter value "
                    + parameter + " not available!");
            return;
        }
        @SuppressWarnings("unchecked")
        DataAccessObject<Object> dao = this.daoMap.get(entityType);
        this.updateEntities(dao, content, message);
    }

    // ********************************************************************
    // * QUERY READ
    // ********************************************************************

    @SuppressWarnings("unchecked")
    private String querySelect(final String parameter, final Message msg) {
        // TODO debug
        System.out.println("to parser->" + parameter);

        // create query
        final QueryParser parser = new QueryParser();
        parser.parse("query.select=" + parameter);
        final String localQuery = parser.getModel();

        // perform query

        String response;

        final List queryResult = this.databaseAccess.query(localQuery);
        msg.appendBody("Info Size Resultset", String.valueOf(queryResult.size()));

        // compute the query
        final CSVParser csvparser = new CSVParser();
        Table<String> table = new Table<>();


        final String entityType = parser.getEntityType().toLowerCase();
        if (!this.daoMap.containsKey(entityType)) {
            msg.appendBody("Error", "parameter value "
                    + parameter + " not available!");
        } else {
            DataAccessObject<Object> dao = this.daoMap.get(entityType);
            table = dao.toTable(queryResult);
        }

        // chose the format
        switch (this.contentTypeFormat.toLowerCase()) {
            case CONTENT_TYPE_CSV:
                csvparser.setModel(table);
                response = csvparser.toString();
                break;

            default/* XML */:
                final JAXBEngine engine = JAXBEngine.getInstance();
                final StringWriter writer = new StringWriter();
                engine.write(table, writer, table.getObjectFactory());
                response = writer.toString();
                break;
        }
        return response;
    }

    // ********************************************************************
    // * CREATE AND UPDATE ENTITY
    // ********************************************************************

    private <E> void createEntiries(final DataAccessObject<E> dao, final String content, final Message message) {
        Notification notification = dao.createEntities(this.createTable(content));
        this.includeNotification(notification.getNotification(), message);
    }


    private <E> void updateEntities(final DataAccessObject<E> dao, final String content, final Message message) {
        final Notification notification = dao.updateEntities(this.createTable(content));
        this.includeNotification(notification.getNotification(), message);
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
     * @param content the actual content
     * @return the corresponding table
     */
    @SuppressWarnings("unchecked")
    private Table<String> createTable(final String content) {
        final CSVParser parser = new CSVParser();
        Table<String> table;

        switch (this.contentTypeFormat.toLowerCase()) {
            case CONTENT_TYPE_CSV:
                parser.parse(content);
                table = parser.getModel();
                break;

            default:
                final JAXBEngine engine = JAXBEngine.getInstance();
                final JAXBElement<Table<String>> obj = (JAXBElement<Table<String>>) engine.read(content, TableObjectFactory.class);
                table = obj.getValue();
                break;
        }
        parser.setModel(table);
        return table;
    }

    /**
     * Include the notification into the message object
     *
     * @param table   the table
     * @param message the message
     */
    private void includeNotification(final Table<String> table, final Message message) {
        final CSVParser parser = new CSVParser();
        parser.setModel(table);
        message.appendBody("Notification", parser.toString());
    }

    // TODO does this construct serve a purpose or is it just broken.

    /**
     * Local class to encapsulate the GET-Request where the description of this
     * {@link ServiceProvider} is requested.
     *
     * @author Alessandro Giusa, alessandrogiusa@gmail.com
     */
    private static final class ServiceProviderDescriptor {

        /**
         * This class is actually a factory.
         */
        private ServiceProviderDescriptor() {
        }

        /**
         * Write the description to {@link HttpServletResponse}
         *
         * @param request  the request object
         * @param response the response object
         * @throws IOException if request could not be processed
         */
        static void getDescription(final HttpServletRequest request,
                                   final HttpServletResponse response) throws IOException {

            final String urlBase = request.getScheme()
                    + "://" + request.getServerName()
                    + ":" + request.getServerPort()
                    + request.getContextPath();

            final ServiceProvider spDatabase = new ServiceProvider();
            spDatabase.setName(NAME_SERVICE_PROVIDER_DATABASE);
            spDatabase.setUrl(urlBase + ServiceProviderInterface.URL_SERVICE_BASE
                    + URL_SERVICE_PROVIDER_DATABASE);

            final Service getData = new Service();
            getData.setName(NAME_SERVICE_DATABASE_GETDATA);
            getData.setUrl(urlBase + ServiceProviderInterface.URL_SERVICE_BASE
                    + URL_SERVICE_DATABASE_GETDATA);

            final Service setData = new Service();
            setData.setName(NAME_SERVICE_DATABASE_SETDATA);
            setData.setUrl(urlBase + ServiceProviderInterface.URL_SERVICE_BASE
                    + URL_SERVICE_DATABASE_SETDATA);

            final Service schemas = new Service();
            schemas.setName(NAME_SERVICE_SCHEMAS);
            schemas.setUrl(urlBase + ServiceProviderInterface.URL_SERVICE_BASE
                    + URL_SERVICE_DATABASE_SCHEMAS);

            spDatabase.getServices().add(getData);
            spDatabase.getServices().add(setData);
            spDatabase.getServices().add(schemas);

            response.setContentType("text/xml");
            final PrintWriter writer = response.getWriter();
            final XML xmlResult = JAXBEngine.getInstance().write(spDatabase);
            if(xmlResult != null) {
                writer.append(xmlResult.toString());
            }
            writer.close();
        }
    }

}
