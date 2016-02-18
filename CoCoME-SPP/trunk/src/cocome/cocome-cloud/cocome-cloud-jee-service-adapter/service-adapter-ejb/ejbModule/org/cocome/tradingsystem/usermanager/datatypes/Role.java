package org.cocome.tradingsystem.usermanager.datatypes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Role")
@XmlEnum
public enum Role {
	@XmlEnumValue("Customer")
	CUSTOMER("Customer"),
	@XmlEnumValue("Admin")
	ADMIN("Admin"),
	@XmlEnumValue("Cashier")
	CASHIER("Cashier"),
	@XmlEnumValue("Store Manager")
	STORE_MANAGER("Store Manager"),
	@XmlEnumValue("Stock Manager")
	STOCK_MANAGER("Stock Manager"),
	@XmlEnumValue("Enterprise Manager")
	ENTERPRISE_MANAGER("Enterprise Manager");
	
	private final String __label;
	
	public static final int SIZE = Role.values().length;
	
	Role(String label) {
		this.__label = label;
	}
	
	public String label() {
		return __label;
	}
}
