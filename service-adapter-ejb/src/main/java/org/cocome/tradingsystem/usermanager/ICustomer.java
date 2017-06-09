package org.cocome.tradingsystem.usermanager;

import java.util.Set;

import org.cocome.tradingsystem.inventory.data.store.Store;

public interface ICustomer {

	String getMailAddress();

	void setMailAddress(String mailAddress);

	Store getPreferredStore();

	void setPreferredStore(Store preferredStore);

	String getLastName();

	void setLastName(String lastName);

	String getFirstName();

	void setFirstName(String firstName);

	Set<String> getCreditCardInfo();

	void setCreditCardInfo(Set<String> creditCardInfo);

	void setUser(LoginUser user);
	
	LoginUser getUser();
}