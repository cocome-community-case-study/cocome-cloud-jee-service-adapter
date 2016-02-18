package org.cocome.tradingsystem.usermanager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.usermanager.datatypes.Role;
import java.io.Serializable;

@Entity
@XmlRootElement(name="Customer")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Customer", propOrder = { "firstName", "lastName", "mailAddress",
	"creditCardInfoSet", "preferredStore", "user"})
public class Customer implements Serializable, ICustomer {
	@XmlTransient
	private static final long serialVersionUID = 1L;
	
	@XmlTransient
	private long id;
	
	@XmlElement(name="firstName")
	private String firstName;
	
	@XmlElement(name="lastName")
	private String lastName;
	
	@XmlElement(name="mailAddress")
	private String mailAddress;
	
	@XmlElementWrapper(name="creditCardInfoSet", nillable=true)
	@XmlElement(name="creditCardInfo")
	private Set<String> creditCardInfoSet = new LinkedHashSet<String>();
	
	@XmlElement(name="preferredStore", nillable=true)
	private Store preferredStore;
	
	@XmlElement(name="user", required=true)
	private LoginUser user;

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#getMailAddress()
	 */
	@Basic
	@Override
	public String getMailAddress() {
		return mailAddress;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#setMailAddress(java.lang.String)
	 */
	@Override
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#getPreferredStore()
	 */
	@ManyToOne(optional=true, fetch=FetchType.EAGER)
	@Override
	public Store getPreferredStore() {
		return preferredStore;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#setPreferredStore(org.cocome.tradingsystem.inventory.data.store.Store)
	 */
	@Override
	public void setPreferredStore(Store preferredStore) {
		this.preferredStore = preferredStore;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#getLastName()
	 */
	@Basic
	@Override
	public String getLastName() {
		return lastName;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#setLastName(java.lang.String)
	 */
	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#getFirstName()
	 */
	@Basic
	@Override
	public String getFirstName() {
		return firstName;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#setFirstName(java.lang.String)
	 */
	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#getCreditCardInfo()
	 */
	@ElementCollection(fetch=FetchType.EAGER)
	@Override
	public Set<String> getCreditCardInfo() {
		return creditCardInfoSet;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#setCreditCardInfo(java.util.Set)
	 */
	@Override
	public void setCreditCardInfo(Set<String> creditCardInfo) {
		this.creditCardInfoSet = creditCardInfo;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#addCreditCardInfo(java.lang.String)
	 */
	@Override
	public void addCreditCardInfo(String creditCardInfo) {
		if (creditCardInfo != null && !creditCardInfo.isEmpty()) {
			// TODO Add some validity check for this info perhaps
			creditCardInfoSet.add(creditCardInfo);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#removeCreditCardInfo(java.lang.String)
	 */
	@Override
	public void removeCreditCardInfo(String creditCardInfo) {
		creditCardInfoSet.remove(creditCardInfo);
	}

	@OneToOne(optional=false, fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	public LoginUser getUser() {
		return user;
	}

	public void setUser(LoginUser user) {
		this.user = user;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
