package org.cocome.tradingsystem.usermanager.credentials;

import org.cocome.tradingsystem.usermanager.LoginUser;
import org.cocome.tradingsystem.usermanager.datatypes.CredentialType;
import org.cocome.tradingsystem.usermanager.datatypes.Role;

public class AuthToken implements ICredential {
	private String tokenString;

	@Override
	public CredentialType getType() {
		return CredentialType.AUTH_TOKEN;
	}

	@Override
	public boolean isMatching(ICredential credential) {
		boolean isAuthToken = credential.getType() == CredentialType.AUTH_TOKEN;
		boolean matchesAuthToken = getCredentialString().equals(credential.getCredentialString()); 
		return isAuthToken ? matchesAuthToken : false;
	}

	@Override
	public String getCredentialString() {
		return tokenString;
	}

	@Override
	public void setCredentialString(String credential) {
		tokenString = credential;
	}

	@Override
	public char[] resetCredential(LoginUser user) {
		// Set the authentication token to a random password + the username + 
		// one char to indicate if the user has admin access or not
		int passLength = RandomPasswordGenerator.PASSWORD_LENGTH;
		int passUserLength = passLength + user.getUsername().length();
		
		char[] buffer = new char[passUserLength];
		char[] password = RandomPasswordGenerator.generatePassword();
		
		for (int i = 0; i < passUserLength; i++) {
			if (i < RandomPasswordGenerator.PASSWORD_LENGTH) {
				buffer[i] = password[i];
			} else if (i < passUserLength) {
				buffer[i] = user.getUsername().charAt(i - passLength);
			}
		}
		
		tokenString = new String(buffer);
		return buffer;
	}

}
