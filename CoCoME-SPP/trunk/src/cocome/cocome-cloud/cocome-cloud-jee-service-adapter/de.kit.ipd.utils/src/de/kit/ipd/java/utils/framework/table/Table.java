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

import de.kit.ipd.java.utils.xml.Marshable;

@XmlRootElement(name = "Table")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Table", propOrder = { "name", "rows","header" })
public class Table<T> implements Marshable, Serializable {
	
	private static final long serialVersionUID = 4238619456650651713L;
	
	public static final Class<TableObjectFactory> OBJECT_FACTORY =
			TableObjectFactory.class;

	@XmlAttribute(name = "name")
	private String name;

	@XmlElementWrapper(name = "Rows",required=true)
	@XmlElement(name = "Row",required=true)
	private List<Row<T>> rows = new ArrayList<Row<T>>();
	
	@XmlElementWrapper(name="Headers",required=true)
	@XmlElement(name="Header",required=true)
	private List<TableHeader> header = new ArrayList<TableHeader>();

	public String getName() {
		return name;
	}

	public List<Row<T>> getRows() {
		return rows;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRows(List<Row<T>> rows) {
		this.rows = rows;
	}
	
	public int size(){
		return this.rows.size();
	}
	
	
	public List<TableHeader> getHeader() {
		return header;
	}

	public void setHeader(List<TableHeader> header) {
		this.header = header;
	}
	
	@Override
	public Class<?> getObjectFactory() {
		return TableObjectFactory.class;
	}

	/*************************************************************************
	 * BUSINESS METHODS
	 ************************************************************************/
	
	public void addHeader(String... names) {
		if (names != null) {
			int len = names.length;
			for (int i = 0; i < len; i++) {
				TableHeader _header = new TableHeader();
				_header.setIndex(i);
				_header.setName(names[i]);
				header.add(_header);
			}
		}
	}
	
	public void addRows(int amount) {
		int start = rows.size();
		for (int i = 0; i < amount; i++) {
			Row<T> _row = new Row<>();
			_row.setIndex(start+i);
			rows.add(start+i,_row);
		}
	}
	
	public void addColumn(int row, int column, T value) {
		boolean test = (header.size() - 1) >= column;
		if(test){
			Row<T> _row = this.getRow(row);
			Column<T> col = new Column<>();
			col.setIndex(column);
			col.setName(header.get(column).getName());
			col.setValue(value);
			_row.getColumns().add(col);
		}
	}
	
	public void addColumn(int row, int column, T value, boolean createRowIfAbsent) {
		boolean test = (header.size() - 1) >= column;
		if(test){
			Row<T> _row = this.getRow(row,createRowIfAbsent);
			Column<T> col = new Column<>();
			col.setIndex(column);
			col.setName(header.get(column).getName());
			col.setValue(value);
			_row.getColumns().add(col);
			return;
		}
		throw new IllegalArgumentException("row index not valid! Give only 0-"+(header.size()-1));
	}
	
	public TableHeader getHeader(int index){
		if(index >= 0){
			for(TableHeader nextHeader:header){
				if(nextHeader.getIndex()==index){
					return nextHeader;
				}
			}
		}
		throw new IllegalArgumentException("index not valid! Give only >= 0");
	}
	
	public Row<T> getRow(int index, boolean createIfAbsent){
		if (index <= (this.size() - 1) && index >= 0) {
			return rows.get(index);
		}
		if (createIfAbsent) {
			int start = (rows.size()!=0)?rows.size() - 1:0;
			int amount = (index-start!=0)?index-start:1;
			this.addRows(amount);
			return this.getRow(index);
		}
		throw new IllegalArgumentException("row index not valid! Give only 0-"+(this.size()-1));
	}
	
	public Row<T> getRow(int index){
		if (index <= (this.size() - 1) && index >= 0) {
			return rows.get(index);
		}
		throw new IllegalArgumentException("row index not valid! Give only 0-"+(this.size()-1));
	}
	
	public Column<T> getColumn(int row, int column){
		Row<T> _row = this.getRow(row);
		if ( _row.size() <= (_row.size() - 1) && _row.size() >= 0) {
			return _row.getColumns().get(column);
		}
		throw new IllegalArgumentException("column index not valid! Give only 0-"+(_row.size()-1));
	}
	
	public Column<T> getColumnByName(int row, String name){
		Row<T> _row = getRow(row);
		if(name != null){
			for(Column<T> nextCol : _row.getColumns()){
				if(nextCol.getName().equals(name)){
					return nextCol;
				}
			}
			return null;
		}
		throw new IllegalArgumentException("name is null!");
	}
}
