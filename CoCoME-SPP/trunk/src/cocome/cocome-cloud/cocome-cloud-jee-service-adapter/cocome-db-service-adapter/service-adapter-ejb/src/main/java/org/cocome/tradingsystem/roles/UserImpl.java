package org.cocome.tradingsystem.roles;

import java.util.ArrayList;
import java.util.List;


class UserImpl {
	
	private final String name;
	private final List<String> permissions = new ArrayList<>();
	private String sessionId = null;
	
	public UserImpl(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean setSessionId(String sessionId) {
		if(this.sessionId==null){
			this.sessionId = sessionId;
			return true;
		}
		return false;
	}
	
	public boolean checkPermission(String value, String sessionId) {
		if(value!=null && sessionId!=null && this.sessionId!=null){
			for(String next:permissions){
				if(value.equals(next)){
					return true;
				}
			}
		}
		return false;
	}
	
	public void addPermission(String permission){
		permissions.add(permission);
	}

}
