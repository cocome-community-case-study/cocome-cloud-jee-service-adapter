package org.cocome.tradingsystem.roles;

import javax.ejb.Remote;

@Remote
public interface UserManager {
	
	String BEAN_NAMING = "UserManager";
	String JDNI_NAMING = "ejb/stateful/UserManagerBean";

	boolean checkPermission(String name, String value, String sessionId);
	
	boolean login(String sessionId, String user, String password);
	
}
