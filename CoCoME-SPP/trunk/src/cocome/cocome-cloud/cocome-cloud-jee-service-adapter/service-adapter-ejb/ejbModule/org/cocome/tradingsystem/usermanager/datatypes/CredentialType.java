package org.cocome.tradingsystem.usermanager.datatypes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "CredentialType")
@XmlEnum
public enum CredentialType {
	@XmlEnumValue("PASSWORD")
	PASSWORD("PASSWORD"),
	@XmlEnumValue("AUTH_TOKEN")
	AUTH_TOKEN("AUTH_TOKEN");
	
	public static final int SIZE = CredentialType.values().length;
	
	private final String __label;
	
	CredentialType(String label) {
		__label = label;
	}
	
	public String label() {
		return __label;
	}
}
