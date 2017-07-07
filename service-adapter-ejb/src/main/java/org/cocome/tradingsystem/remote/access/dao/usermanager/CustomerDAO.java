package org.cocome.tradingsystem.remote.access.dao.usermanager;

import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.remote.access.dao.DataAccessObject;
import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.store.StoreDAO;
import org.cocome.tradingsystem.usermanager.Customer;
import org.cocome.tradingsystem.usermanager.LoginUser;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.*;

/**
 * DAO for {@link Store}
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class CustomerDAO implements DataAccessObject<Customer> {

    @EJB
    private LoginUserDAO loginUserDAO;

    @EJB
    private StoreDAO storeDAO;

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Override
    public String getEntityTypeName() {
        return "customer";
    }

    @Override
    public Notification createEntities(List<Customer> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();

        LoginUser _user;
        Store _store = null;

        for (final Customer nextCustomer : entities) {
            // query product
            _user = loginUserDAO.queryUser(em, nextCustomer.getUser());
            if (_user == null) {
                notification.addNotification(
                        "createCustomer", Notification.FAILED,
                        "User not available:" + nextCustomer.getUser());
                continue;
            }
            // query store
            if (nextCustomer.getPreferredStore() != null) {
                _store = storeDAO.queryStoreById(em, nextCustomer.getPreferredStore());
                if (_store == null) {
                    notification.addNotification(
                            "createCustomer", Notification.FAILED,
                            "Store not available:" + nextCustomer.getPreferredStore());
                    continue;
                }
            }

            // update object with actual database objects
            nextCustomer.setPreferredStore(_store);
            nextCustomer.setUser(_user);

            // persist
            em.persist(nextCustomer);
            notification.addNotification(
                    "createCustomer", Notification.SUCCESS,
                    "Creation customer:" + nextCustomer);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Notification updateEntities(List<Customer> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();

        for (final Customer nextCustomer : entities) {
            Customer persistedCustomer = this.queryCustomer(em, nextCustomer);

            if (persistedCustomer == null) {
                notification.addNotification("updateCustomer", Notification.FAILED,
                        "Update Customer: No such customer: " + nextCustomer.getId());
                continue;
            }

            persistedCustomer.setFirstName(nextCustomer.getFirstName());
            persistedCustomer.setLastName(nextCustomer.getLastName());
            persistedCustomer.setCreditCardInfo(nextCustomer.getCreditCardInfo());
            persistedCustomer.setPreferredStore(nextCustomer.getPreferredStore());
            // Do not change mail address and users because they are used for
            // the login and should not change

            em.merge(nextCustomer);
            notification.addNotification(
                    "updateCustomer", Notification.SUCCESS,
                    "Update Customer: " + nextCustomer);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Table<String> toTable(List<Customer> list) {
        final Table<String> table = new Table<>();
        table.addHeader("CustomerId", "FirstName",
                "LastName", "MailAddress", "CreditCardInfo",
                "PreferredStoreEnterpriseName", "PreferredStoreId",
                "PreferredStoreName", "PreferredStoreLocation",
                "UserId", "UserName");

        int row = 0;

        for (final Customer nextCustomer : list) {
            if (nextCustomer.getCreditCardInfo().size() > 0) {
                for (final String creditCardInfo : nextCustomer.getCreditCardInfo()) {
                    this.createCustomerEntry(table, row, nextCustomer, creditCardInfo);
                    row++;
                }
            } else {
                this.createCustomerEntry(table, row, nextCustomer, "null");
                row++;
            }
        }
        return table;
    }

    @Override
    public List<Customer> fromTable(Table<String> table) {
        final List<Customer> list = new ArrayList<>();
        final int len = table.size();

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

            final Customer customer = new Customer();

            if (colCustomerId != null) {
                customer.setId(Long.parseLong(colCustomerId.getValue()));
            }

            if (colPreferredStoreId != null) {
                final Store preferredStore = new Store();
                preferredStore.setId(Long.parseLong(colPreferredStoreId.getValue()));
                preferredStore.setName(colPreferredStoreName.getValue());
                preferredStore.setLocation(colPreferredStoreLocation.getValue());

                final TradingEnterprise enterprise = new TradingEnterprise();
                enterprise.setName(colPreferredStoreEnterpriseName.getValue());
                preferredStore.setEnterprise(enterprise);

                customer.setPreferredStore(preferredStore);
            }

            final LoginUser user = new LoginUser();
            if (colUserId != null) {
                user.setId(Long.parseLong(colUserId.getValue()));
            }

            if (colUsername != null) {
                user.setUsername(colUsername.getValue());
            }
            customer.setUser(user);

            final Set<String> creditCardInfo = new HashSet<>();
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

    private void createCustomerEntry(final Table<String> table, final int row, final Customer nextCustomer, final String creditCardInfo) {
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

    Customer queryCustomer(final EntityManager em, final Customer customer) {
        return querySingleInstance(em.createQuery(
                "SELECT c FROM Customer c WHERE c.id = :cId",
                Customer.class).setParameter("cId", customer.getId()));
    }

}
