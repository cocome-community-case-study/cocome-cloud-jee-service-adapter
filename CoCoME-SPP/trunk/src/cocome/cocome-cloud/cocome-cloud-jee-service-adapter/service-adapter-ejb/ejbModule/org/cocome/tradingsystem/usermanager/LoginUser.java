package org.cocome.tradingsystem.usermanager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.cocome.tradingsystem.usermanager.credentials.AbstractCredential;
import org.cocome.tradingsystem.usermanager.credentials.ICredential;
import org.cocome.tradingsystem.usermanager.credentials.PlainPassword;
import org.cocome.tradingsystem.usermanager.datatypes.CredentialType;
import org.cocome.tradingsystem.usermanager.datatypes.Role;

@Entity
@XmlRootElement(name="User")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "User", propOrder = {"username", "credentials", "roles"})
public class LoginUser implements Serializable {
	private static final long serialVersionUID = 1L;

//	private static final Logger LOG = Logger.getLogger(User.class);
	@XmlTransient
	private long id;
	
	@XmlElement(name="username")
	private String username;
	
	@XmlElementWrapper(name="credentials")
	@XmlElement(name="credential")
	private Map<CredentialType, AbstractCredential> credentials = new LinkedHashMap<CredentialType, AbstractCredential>((int) (CredentialType.SIZE / 0.75));
	
	@XmlElementWrapper(name="roles")
	@XmlElement(name="role")
	private Set<Role> roles = new LinkedHashSet<Role>((int) (Role.SIZE / 0.75));
	
//	protected void initRoles() {
//		// No default roles for a user
//	}
//	
//	protected void initCredentials() {
//		// No default credentials
//	}
//	
//	@PostConstruct
//	public void initUser() {
//		initRoles();
//		initCredentials();
//	}
	
//	public void initUser(UserTO userTO) {
//		this.username = userTO.getUsername();
//		
//		for (CredentialTO cred : userTO.getCredentials()) {
//			ICredential credential = credentialInstance.select(
//					new CredentialLiteral(cred.getType())).get();
//			credential.setCredentialChars(cred.getCredentialChars());
//			credentials.put(cred.getType(), credential);
//		}
//		
//		// Don't initialize the roles from the passed in TO but query them from the database
//	}
	
	@Basic
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setCredentials(Map<CredentialType, AbstractCredential> credentials) {
		this.credentials = credentials;
	}

	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@MapKeyEnumerated(EnumType.STRING)
	public Map<CredentialType, AbstractCredential> getCredentials() {
		return credentials;
	}
	
//	@Override
//	public void addCredential(ICredential credential) {
//		credentials.putIfAbsent(credential.getType(), credential);
//	}
//	
//	@Override
//	public void removeCredential(CredentialType credentialType) {
//		credentials.remove(credentialType);
//	}
//
//	@Override
//	public boolean checkCredential(ICredential credential) {
//		ICredential storedCred = credentials.get(credential.getType());
//		boolean result = false;
//		if (storedCred != null) {
//			result = storedCred.isMatching(credential);
//		}
//		return result;
//	}
	
//	@Override
//	public boolean checkUserCredentials(IUser userToCheck) {
//		boolean result = false;
//		
//		LOG.debug("Usernames " + username + " and " + userToCheck.getUsername());
//		
//		// Username has to match as part of the credentials
//		if (username.equals(userToCheck.getUsername())) {
//			result = true;
//		}
//		
//		for (ICredential credential : credentials.values()) {
//			ICredential credToCheck = userToCheck.getCredential(credential.getType());
//			LOG.debug("Checking credential with type " + credToCheck.getType());
//			
//			if (credToCheck.getType() == CredentialType.AUTH_TOKEN) {
//				// If an authentication token is present, check only this token
//				result = credential.isMatching(credential);
//				break;
//			}
//			
//			result = credential.isMatching(credToCheck) ? result : false;
//		}
//		LOG.debug("Endresult was " + result);
//		return result;
//	}

	public boolean hasRole(Role role) {
//		LOG.debug("Checking for permission " + role.label() + " in " + roles.toString());
		return roles.contains(role);
	}

	public void addRole(Role role) {
		if (role != null) {
//			LOG.debug("Adding role " + role.label() + " to user " + username);
			roles.add(role);
		}
	}

	public void withdrawRole(Role role) {
//		LOG.debug("Withdrawing role " + role.label() + " from user " + username);
		roles.remove(role);
	}

	@ElementCollection(fetch=FetchType.EAGER)
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		if (roles != null) {
//			LOG.debug("Setting roles for user " + username);
			this.roles = roles;
		}
	}
	
//	@Override
//	public String resetCredential(CredentialType credentialType) throws CredentialTypeNotFoundException {
//		ICredential cred = credentials.get(credentialType);
//		if (cred != null) {
//			return new String(cred.resetCredential(this));
//		}
//		throw new CredentialTypeNotFoundException("No credential with the type " + credentialType + "registered!");
//	}

	public ICredential getCredential(CredentialType credentialType) {
		return credentials.get(credentialType);
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

//	@Override
//	public boolean isLoggedIn() {
//		return loggedIn;
//	}
//
//	@Override
//	public boolean logOut() {
//		this.loggedIn = false;
//		return true;
//	}
//
//	@Override
//	public void logIn() {
//		try {
//			LOG.debug("Trying to login as " + username);
//			loggedIn = authenticator.checkCredentials(this);
//		} catch (NotInDatabaseException e) {
//			loggedIn = false;
//			LOG.error("Login for user " + username + " failed!");
//		}
//	}
	
	@Override
	public String toString() {
		return "[User: " + id + ":" + username + "]";
	}
}
