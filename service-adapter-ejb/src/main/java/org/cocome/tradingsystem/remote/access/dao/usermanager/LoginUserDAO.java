package org.cocome.tradingsystem.remote.access.dao.usermanager;

import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.remote.access.dao.DataAccessObject;
import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.remote.access.DatabaseAccess;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.usermanager.LoginUser;
import org.cocome.tradingsystem.usermanager.credentials.AbstractCredential;
import org.cocome.tradingsystem.usermanager.credentials.ICredential;
import org.cocome.tradingsystem.usermanager.credentials.PlainPassword;
import org.cocome.tradingsystem.usermanager.datatypes.CredentialType;
import org.cocome.tradingsystem.usermanager.datatypes.Role;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.*;

/**
 * DAO for {@link Store}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class LoginUserDAO implements DataAccessObject<LoginUser> {

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Override
    public String getEntityTypeName() {
        return "loginuser";
    }

    @Override
    public Notification createEntities(List<LoginUser> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();

        for (final LoginUser nextUser : entities) {
            // persist
            em.persist(nextUser);
            notification.addNotification(
                    "createUser", Notification.SUCCESS,
                    "Creation User:" + nextUser);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Notification updateEntities(List<LoginUser> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();

        for (final LoginUser nextUser : entities) {
            LoginUser persistedUser = this.queryUser(em, nextUser);

            if (persistedUser == null) {
                notification.addNotification("updateUser", Notification.FAILED,
                        "Update user: No such user: " + nextUser.getUsername());
                continue;
            }

            persistedUser.setUsername(nextUser.getUsername());
            persistedUser.setRoles(nextUser.getRoles());
            persistedUser.setCredentials(nextUser.getCredentials());

            em.merge(nextUser);
            notification.addNotification(
                    "updateUser", Notification.SUCCESS,
                    "Update User:" + nextUser);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Table<String> toTable(List<LoginUser> list) {
        final Table<String> table = new Table<>();
        table.addHeader("UserId", "UserName",
                "CredentialType", "CredentialString", "Role");

        int row = 0;

        for (final LoginUser nextUser : list) {
            final Collection<AbstractCredential> credentials = nextUser.getCredentials().values();
            final Set<Role> roles = nextUser.getRoles();

            final Iterator<AbstractCredential> credIterator = credentials.iterator();
            final Iterator<Role> roleIterator = roles.iterator();

            ICredential currCred = null;
            Role currRole = null;

            while (credIterator.hasNext() || roleIterator.hasNext()) {
                if (credIterator.hasNext()) {
                    currCred = credIterator.next();
                }
                if (roleIterator.hasNext()) {
                    currRole = roleIterator.next();
                }

                table.addColumn(row, 0, String.valueOf(nextUser.getId()), true);
                table.addColumn(row, 1, nextUser.getUsername(), true);
                table.addColumn(row, 2, currCred == null ? "" : currCred.getType().label(), true);
                table.addColumn(row, 3, currCred == null ? "" : currCred.getCredentialString(), true);
                table.addColumn(row, 4, currRole == null ? "" : currRole.label(), true);
                row++;
            }
        }
        return table;
    }

    @Override
    public List<LoginUser> fromTable(Table<String> table) {
        final int len = table.size();
        System.out.println("User table size: " + table.size());
        final Map<String, LoginUser> list = new HashMap<>((int) (len / 0.75));

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
                final String username = colUsername.getValue();
                user = list.get(username);

                if (user == null) {
                    user = new LoginUser();
                    user.setUsername(username);
                }
            } else {
                // user.setUsername("default");
                continue;
            }

            if (colUserId != null) {
                user.setId(Long.parseLong(colUserId.getValue()));
            }

            Map<CredentialType, AbstractCredential> credentials = user.getCredentials();

            if (credentials == null) {
                credentials = new HashMap<>();
            }

            if (colCredentialType != null
                    && colCredentialType.getValue().equals(CredentialType.PASSWORD.label())) {
                final PlainPassword password = new PlainPassword();
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

    LoginUser queryUser(final EntityManager em, final LoginUser user) {
        return querySingleInstance(em.createQuery(
                "SELECT u FROM LoginUser u WHERE u.username = :uName",
                LoginUser.class).setParameter("uName", user.getUsername()));
    }
}