package de.kit.ipd.java.utils.framework.table;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;



@XmlRegistry
public class TableObjectFactory {
	
	private final static QName _Table_QName = new QName("", "Table");
	private final static QName _Header_QName = new QName("", "Header");
	private final static QName _Row_QName = new QName("", "Row");
	private final static QName _Column_QName = new QName("", "Column");
	
	

    public TableObjectFactory() {}
    
    public Table createTable() {
        return new Table();
    }
    
    @XmlElementDecl(namespace = "", name = "Table")
    public JAXBElement createTable(Table value) {
        return new JAXBElement<Table>(_Table_QName, Table.class, null, value);
    }
    
    public TableHeader createTableHeader() {
        return new TableHeader();
    }
    
    @XmlElementDecl(namespace = "", name = "Header")
    public JAXBElement createTableHeader(TableHeader value) {
        return new JAXBElement<TableHeader>(_Header_QName, TableHeader.class, null, value);
    }
    
    public Row createRow() {
    	return new Row();
    }
    
    @XmlElementDecl(namespace = "", name = "Row")
    public JAXBElement<Row> createTable(Row value) {
    	return new JAXBElement<Row>(_Row_QName, Row.class, null, value);
    }
    
    public Column createColumn() {
    	return new Column();
    }
    
    @XmlElementDecl(namespace = "", name = "Column")
    public JAXBElement<Column> createColumn(Column value) {
    	return new JAXBElement<Column>(_Column_QName, Column.class, null, value);
    }
    

}
