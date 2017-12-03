/*
 *************************************************************************
 * Copyright 2013 DFG SPP 1593 (http://dfg-spp1593.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************************************************************
 */

package cocome.cloud.sa.serviceprovider.impl;

import cocome.cloud.sa.entities.Message;
import cocome.cloud.sa.entities.MessageEntry;
import cocome.cloud.sa.query.IQueryConst;
import cocome.cloud.sa.query.parsing.QueryParser;
import cocome.cloud.sa.serviceprovider.Service;
import cocome.cloud.sa.serviceprovider.ServiceProvider;
import de.kit.ipd.java.utils.framework.table.Table;
import de.kit.ipd.java.utils.framework.table.TableObjectFactory;
import de.kit.ipd.java.utils.parsing.csv.CSVParser;
import de.kit.ipd.java.utils.time.TimeUtils;
import de.kit.ipd.java.utils.xml.JAXBEngine;
import de.kit.ipd.java.utils.xml.XML;
import org.apache.log4j.Logger;
import org.cocome.tradingsystem.remote.access.DatabaseAccess;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.DataAccessObject;
import org.cocome.tradingsystem.remote.access.dao.enterprise.ProductDAO;
import org.cocome.tradingsystem.remote.access.dao.enterprise.ProductSupplierDAO;
import org.cocome.tradingsystem.remote.access.dao.enterprise.TradingEnterpriseDAO;
import org.cocome.tradingsystem.remote.access.dao.enterprise.parameter.BooleanCustomProductParameterDAO;
import org.cocome.tradingsystem.remote.access.dao.enterprise.parameter.CustomProductParameterDAO;
import org.cocome.tradingsystem.remote.access.dao.enterprise.parameter.CustomProductParameterValueDAO;
import org.cocome.tradingsystem.remote.access.dao.enterprise.parameter.NorminalCustomProductParameterDAO;
import org.cocome.tradingsystem.remote.access.dao.plant.PlantDAO;
import org.cocome.tradingsystem.remote.access.dao.plant.expression.ConditionalExpressionDAO;
import org.cocome.tradingsystem.remote.access.dao.plant.expression.ExpressionDAO;
import org.cocome.tradingsystem.remote.access.dao.plant.parameter.BooleanPlantOperationParameterDAO;
import org.cocome.tradingsystem.remote.access.dao.plant.parameter.NorminalPlantOperationParameterDAO;
import org.cocome.tradingsystem.remote.access.dao.plant.parameter.PlantOperationParameterDAO;
import org.cocome.tradingsystem.remote.access.dao.plant.productionunit.ProductionUnitClassDAO;
import org.cocome.tradingsystem.remote.access.dao.plant.productionunit.ProductionUnitDAO;
import org.cocome.tradingsystem.remote.access.dao.plant.productionunit.ProductionUnitOperationDAO;
import org.cocome.tradingsystem.remote.access.dao.plant.recipe.*;
import org.cocome.tradingsystem.remote.access.dao.store.*;
import org.cocome.tradingsystem.remote.access.dao.usermanager.CustomerDAO;
import org.cocome.tradingsystem.remote.access.dao.usermanager.LoginUserDAO;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBElement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation for the ServiceProviderDatabase-Service. This Service
 * provides access to the database
 *
 * @author Alessandro Giusa, alessandrogiusa@gmail.com
 */
@WebServlet("/Database/ServiceProviderDatabase")
public class ServiceProviderDatabase extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(ServiceProviderDatabase.class);

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

    @Inject
    private DatabaseAccess databaseAccess;

    @Inject
    private ProductDAO productDAO;

    @Inject
    private PlantDAO plantDAO;

    @Inject
    private BooleanCustomProductParameterDAO booleanCustomProductParameterDAO;

    @Inject
    private NorminalCustomProductParameterDAO norminalCustomProductParameterDAO;

    @Inject
    private CustomProductParameterDAO customProductParameterDAO;

    @Inject
    private BooleanPlantOperationParameterDAO booleanPlantOperationParameterDAO;

    @Inject
    private NorminalPlantOperationParameterDAO norminalPlantOperationParameterDAO;

    @Inject
    private ProductionOrderDAO productionOrderDAO;

    @Inject
    private ProductionOrderEntryDAO productionOrderEntryDAO;

    @Inject
    private CustomProductParameterValueDAO customProductParameterValueDAO;

    @Inject
    private PlantOperationParameterDAO plantOperationParameterDAO;

    @Inject
    private PlantOperationOrderDAO plantOperationOrderDAO;

    @Inject
    private PlantOperationOrderEntryDAO plantOperationOrderEntryDAO;

    @Inject
    private PlantOperationParameterValueDAO plantOperationParameterValueDAO;

    @Inject
    private ConditionalExpressionDAO conditionalExpressionDAO;

    @Inject
    private ExpressionDAO expressionDAO;

    @Inject
    private EntryPointDAO entryPointDAO;

    @Inject
    private EntryPointInteractionDAO entryPointInteractionDAO;

    @Inject
    private ParameterInteractionDAO parameterInteractionDAO;

    @Inject
    private PlantOperationDAO plantOperationDAO;

    @Inject
    private RecipeDAO recipeDAO;

    @Inject
    private ProductionUnitClassDAO productionUnitClassDAO;

    @Inject
    private ProductionUnitDAO productionUnitDAO;

    @Inject
    private ProductionUnitOperationDAO productionUnitOperationDAO;

    @Inject
    private ProductSupplierDAO productSupplierDAO;

    @Inject
    private TradingEnterpriseDAO tradingEnterpriseDAO;

    @Inject
    private ProductOrderDAO productOrderDAO;

    @Inject
    private ItemDAO itemDAO;

    @Inject
    private OnDemandItemDAO onDemandItemDAO;

    @Inject
    private StockItemDAO stockItemDAO;

    @Inject
    private StoreDAO storeDAO;

    @Inject
    private CustomerDAO customerDAO;

    @Inject
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
        daoMap.put(booleanCustomProductParameterDAO.getEntityTypeName(), booleanCustomProductParameterDAO);
        daoMap.put(norminalCustomProductParameterDAO.getEntityTypeName(), norminalCustomProductParameterDAO);
        daoMap.put(customProductParameterDAO.getEntityTypeName(), customProductParameterDAO);
        daoMap.put(customProductParameterValueDAO.getEntityTypeName(), customProductParameterValueDAO);
        daoMap.put(plantOperationOrderDAO.getEntityTypeName(), plantOperationOrderDAO);
        daoMap.put(plantOperationOrderEntryDAO.getEntityTypeName(), plantOperationOrderEntryDAO);
        daoMap.put(plantOperationParameterValueDAO.getEntityTypeName(), plantOperationParameterValueDAO);
        daoMap.put(booleanPlantOperationParameterDAO.getEntityTypeName(), booleanPlantOperationParameterDAO);
        daoMap.put(norminalPlantOperationParameterDAO.getEntityTypeName(), norminalPlantOperationParameterDAO);
        daoMap.put(plantOperationParameterDAO.getEntityTypeName(), plantOperationParameterDAO);
        daoMap.put(conditionalExpressionDAO.getEntityTypeName(), conditionalExpressionDAO);
        daoMap.put(expressionDAO.getEntityTypeName(), expressionDAO);
        daoMap.put(entryPointDAO.getEntityTypeName(), entryPointDAO);
        daoMap.put(entryPointInteractionDAO.getEntityTypeName(), entryPointInteractionDAO);
        daoMap.put(parameterInteractionDAO.getEntityTypeName(), parameterInteractionDAO);
        daoMap.put(plantOperationDAO.getEntityTypeName(), plantOperationDAO);
        daoMap.put(recipeDAO.getEntityTypeName(), recipeDAO);
        daoMap.put(productionUnitOperationDAO.getEntityTypeName(), productionUnitOperationDAO);
        daoMap.put(productionUnitClassDAO.getEntityTypeName(), productionUnitClassDAO);
        daoMap.put(productionUnitDAO.getEntityTypeName(), productionUnitDAO);
        daoMap.put(productionOrderDAO.getEntityTypeName(), productionOrderDAO);
        daoMap.put(productionOrderEntryDAO.getEntityTypeName(), productionOrderEntryDAO);
        daoMap.put(productSupplierDAO.getEntityTypeName(), productSupplierDAO);
        daoMap.put(tradingEnterpriseDAO.getEntityTypeName(), tradingEnterpriseDAO);
        daoMap.put(productOrderDAO.getEntityTypeName(), productOrderDAO);
        daoMap.put(stockItemDAO.getEntityTypeName(), stockItemDAO);
        daoMap.put(itemDAO.getEntityTypeName(), itemDAO);
        daoMap.put(onDemandItemDAO.getEntityTypeName(), onDemandItemDAO);
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
        dispatchWrite(request, response);
    }

    @Override
    protected void doPut(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        dispatchWrite(request, response);
    }

    @Override
    protected void doDelete(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        //We cannot use HTTP DELETE + response body, so we have to stick with PUT or POST instead.
        throw new UnsupportedOperationException("DELETE not supported, use PUT instead");
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

    private void dispatchWrite(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final String requestedUri = request.getRequestURI();
        if (requestedUri.endsWith(URL_SERVICE_DATABASE_SETDATA)) {
            String next;
            for (final Enumeration<String> param = request.getParameterNames(); param.hasMoreElements(); ) {
                next = param.nextElement();
                if (next.startsWith("query.")) {
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
                    LOG.debug(queryselect);
                    message.appendBody(MESSAGE_ENTRY_RESULT, queryselect);
                    break;
                case IQueryConst.QUERY_INSERT:
                    this.queryInsert(request.getParameter(qm), data, message);
                    break;
                case IQueryConst.QUERY_UPDATE:
                    this.queryUpdate(request.getParameter(qm), data, message);
                    break;
                case IQueryConst.QUERY_DELETE:
                    this.queryDelete(request.getParameter(qm), data, message);
                    break;
                default:
                    message.appendBody("Error", "command " + qm + " not available!");
                    break;
            }

            br.close();
        } catch (final Exception e) { // NOCS
            LOG.error(e);
            final StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            message.appendBody("Error", sw.toString());
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
            LOG.error(e);
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

    private void queryInsert(final String parameter,
                             final String content,
                             final Message message) {
        final String entityType = parameter.toLowerCase();
        if (!this.daoMap.containsKey(entityType)) {
            message.appendBody("Error", "parameter value "
                    + parameter + " not available!");
            return;
        }
        @SuppressWarnings("unchecked")
        DataAccessObject<Object> dao = this.daoMap.get(entityType);
        Notification notification = dao.createEntities(this.createTable(content));
        this.includeNotification(notification.getNotification(), message);
    }

    private void queryUpdate(final String parameter,
                             final String content,
                             final Message message) {
        final String entityType = parameter.toLowerCase();
        if (!this.daoMap.containsKey(entityType)) {
            message.appendBody("Error", "parameter value "
                    + parameter + " not available!");
            return;
        }
        @SuppressWarnings("unchecked")
        DataAccessObject<Object> dao = this.daoMap.get(entityType);
        final Notification notification = dao.updateEntities(this.createTable(content));
        this.includeNotification(notification.getNotification(), message);
    }

    private void queryDelete(final String parameter,
                             final String content,
                             final Message message) {
        final String entityType = parameter.toLowerCase();
        if (!this.daoMap.containsKey(entityType)) {
            message.appendBody("Error", "parameter value "
                    + parameter + " not available!");
            return;
        }
        @SuppressWarnings("unchecked")
        DataAccessObject<Object> dao = this.daoMap.get(entityType);
        final Notification notification = dao.deleteEntities(this.createTable(content));
        this.includeNotification(notification.getNotification(), message);
    }

    // ********************************************************************
    // * QUERY READ
    // ********************************************************************

    @SuppressWarnings("unchecked")
    private String querySelect(final String parameter, final Message msg) {
        LOG.debug("to parser->" + parameter);

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
            if (xmlResult != null) {
                writer.append(xmlResult.toString());
            }
            writer.close();
        }
    }

}
