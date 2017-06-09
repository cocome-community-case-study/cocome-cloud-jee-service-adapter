package org.cocome.tradingsystem.usermanager.credentials;

import org.cocome.tradingsystem.usermanager.LoginUser;
import org.cocome.tradingsystem.usermanager.datatypes.CredentialType;


public interface ICredential {
	CredentialType getType();
	
	boolean isMatching(ICredential credential);
	
	String getCredentialString();
	
	void setCredentialString(String credential);
	
	char[] resetCredential(LoginUser user);
}
