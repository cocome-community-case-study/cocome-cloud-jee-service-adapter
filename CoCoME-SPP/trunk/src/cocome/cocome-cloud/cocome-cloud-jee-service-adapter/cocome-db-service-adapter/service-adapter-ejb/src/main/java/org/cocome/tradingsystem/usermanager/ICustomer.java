package org.cocome.tradingsystem.usermanager;

import java.util.Set;

import org.cocome.tradingsystem.inventory.data.store.Store;

public interface ICustomer {

	public String getMailAddress();

	public void setMailAddress(String mailAddress);

	public Store getPreferredStore();

	public void setPreferredStore(Store preferredStore);

	public String getLastName();

	public void setLastName(String lastName);

	public String getFirstName();

	public void setFirstName(String firstName);

	public Set<String> getCreditCardInfo();

	public void setCreditCardInfo(Set<String> creditCardInfo);

	public void addCreditCardInfo(String creditCardInfo);

	public void removeCreditCardInfo(String creditCardInfo);

	public void setUser(LoginUser user);
	
	public LoginUser getUser();
}