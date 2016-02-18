package org.cocome.tradingsystem.roles;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

@SessionScoped
@Stateful(mappedName=UserManager.JDNI_NAMING, name=UserManager.BEAN_NAMING)
public class UserManagerBean implements UserManager{
	
	private final Map<String, UserImpl> mapUser = new HashMap<>();
	
	@PostConstruct
	private void stubInitUser(){
		UserImpl user = new UserImpl("user");
		user.addPermission("permission.chashier");
		
		UserImpl admin = new UserImpl("admin");
		admin.addPermission("permission.chashier");
		admin.addPermission("permission.manager");
		
		mapUser.put("user", user);
		mapUser.put("admin", admin);
	}
	

	@Override
	public boolean checkPermission(String user, String value, String sessionId) {
		System.out.println(this);
		if(user!=null && value!=null && sessionId!=null){
			UserImpl userObj = mapUser.get(user);
			if(userObj!=null){
				return userObj.checkPermission(value, sessionId);
			}
		}
		return false;
	}
	
	@Override
	public boolean login(String sessionId, String user, String password) {
		// TODO Auto-generated method stub
		// ask database for information
		UserImpl userObj = mapUser.get(user);
		if(userObj!=null && checkPassword(userObj, password) 
				&& userObj.setSessionId(sessionId)
				
				){
			return true;
		}
		return false;
	}
	
	private boolean checkPassword(UserImpl user, String password){
		//ask the database
		if((user.getName().equals("user") && password.equals("user12:-"))||
				(user.getName().equals("admin") && password.equals("root12:-"))
				){
			return true;
		}
		return false;
	}
	

}
