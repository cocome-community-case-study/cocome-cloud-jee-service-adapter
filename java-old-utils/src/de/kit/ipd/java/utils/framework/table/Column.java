package de.kit.ipd.java.utils.framework.table;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Column")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Column", propOrder = { "name", "index", "value" })
public class Column<T> implements Serializable {

	private static final long serialVersionUID = -924839233083176478L;
	
	@XmlAttribute(name = "name",required= true)
	private String name;

	@XmlAttribute(name = "index",required=true)
	private int index;

	@XmlElement(name = "Value",required=true)
	private T value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

}
