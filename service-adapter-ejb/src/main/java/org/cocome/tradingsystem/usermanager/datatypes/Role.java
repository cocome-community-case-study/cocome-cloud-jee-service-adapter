package org.cocome.tradingsystem.usermanager.datatypes;

public enum Role {
	CUSTOMER("Customer"),
	ADMIN("Admin"),
	CASHIER("Cashier"),
	STORE_MANAGER("Store Manager"),
	STOCK_MANAGER("Stock Manager"),
	ENTERPRISE_MANAGER("Enterprise Manager"),
	PLANT_MANAGER("Plant Manager");
	
	private final String __label;
	
	public static final int SIZE = Role.values().length;
	
	Role(String label) {
		this.__label = label;
	}
	
	public String label() {
		return __label;
	}
}
