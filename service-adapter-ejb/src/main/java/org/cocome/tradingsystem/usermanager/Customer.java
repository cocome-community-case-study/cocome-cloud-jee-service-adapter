package org.cocome.tradingsystem.usermanager;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.*;

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;
import org.cocome.tradingsystem.inventory.data.store.Store;
import java.io.Serializable;

@Entity
public class Customer implements Serializable, ICustomer, QueryableById {
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String firstName;
	private String lastName;
	private String mailAddress;
	private Set<String> creditCardInfoSet = new LinkedHashSet<>();
	private Store preferredStore;
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
	@ManyToOne(fetch=FetchType.EAGER)
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
