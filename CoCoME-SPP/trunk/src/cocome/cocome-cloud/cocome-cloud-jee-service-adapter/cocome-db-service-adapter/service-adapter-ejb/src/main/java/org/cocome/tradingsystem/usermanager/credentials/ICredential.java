package org.cocome.tradingsystem.usermanager.credentials;

import org.cocome.tradingsystem.usermanager.LoginUser;
import org.cocome.tradingsystem.usermanager.datatypes.CredentialType;


public interface ICredential {
	public CredentialType getType();
	
	public boolean isMatching(ICredential credential);
	
	public String getCredentialString();
	
	public void setCredentialString(String credential);
	
	public char[] resetCredential(LoginUser user);
}
