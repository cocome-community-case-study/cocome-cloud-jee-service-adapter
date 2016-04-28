package de.kit.ipd.java.utils.framework.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Row")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Row", propOrder = { "name", "index", "columns" })
public class Row<T> implements Serializable {
	
	private static final long serialVersionUID = 1257889213019604259L;

	@XmlAttribute(name = "name")
	private String name;

	@XmlAttribute(name = "index",required=true)
	private int index;

	@XmlElementWrapper(name = "Columns",required=true)
	@XmlElement(name = "Column",required=true)
	private List<Column<T>> columns = new ArrayList<>();

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

	public List<Column<T>> getColumns() {
		return columns;
	}

	public void setColumns(List<Column<T>> columns) {
		this.columns = columns;
	}
	
	public int size(){
		return this.columns.size();
	}

}
