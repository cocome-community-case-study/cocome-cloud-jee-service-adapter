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

import de.kit.ipd.java.utils.xml.IMarshable;

/**
 * @param <T> field type
 * @author unknown
 */
@XmlRootElement(name = "Table")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Table", propOrder = {"name", "rows", "header"})
public class Table<T> implements IMarshable, Serializable {

    private static final long serialVersionUID = 4238619456650651713L;

    @XmlAttribute(name = "name")
    private String name;

    @XmlElementWrapper(name = "Rows", required = true)
    @XmlElement(name = "Row", required = true)
    private List<Row<T>> rows = new ArrayList<>();

    @XmlElementWrapper(name = "Headers", required = true)
    @XmlElement(name = "Header", required = true)
    private List<TableHeader> header = new ArrayList<>();

    /**
     * Should be replaced by one with parameters.
     */
    public Table() {

    }

    public String getName() {
        return this.name;
    }

    public List<Row<T>> getRows() {
        return this.rows;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int size() {
        return this.rows.size();
    }

    public List<TableHeader> getHeader() {
        return this.header;
    }

    public void setHeader(final List<TableHeader> header) {
        this.header = header;
    }

    @Override
    public Class<?> getObjectFactory() {
        return TableObjectFactory.class;
    }

    /*************************************************************************
     * BUSINESS METHODS
     ************************************************************************/

    public void addHeader(final String... names) {
        if (names != null) {
            final int len = names.length;
            for (int i = 0; i < len; i++) {
                final TableHeader newHeader = new TableHeader();
                newHeader.setIndex(i);
                newHeader.setName(names[i]);
                this.header.add(newHeader);
            }
        }
    }

    private void addRows(final int amount) {
        final int start = this.rows.size();
        for (int i = 0; i < amount; i++) {
            final Row<T> row = new Row<>();
            row.setIndex(start + i);
            this.rows.add(start + i, row);
        }
    }

    public void addColumn(final int rowIndex, final int columnIndex, final T value) {
        final boolean test = (this.header.size() - 1) >= columnIndex;
        if (test) {
            final Row<T> row = this.getRow(rowIndex);
            final Column<T> col = new Column<>();
            col.setIndex(columnIndex);
            col.setName(this.header.get(columnIndex).getName());
            col.setValue(value);
            row.getColumns().add(col);
        }
    }

    public void set(final int rowIndex, final int column, final T value) {
        this.addColumn(rowIndex, column, value, true);
    }

    public void addColumn(final int rowIndex, final int column, final T value, final boolean createRowIfAbsent) {
        final boolean test = (this.header.size() - 1) >= column;
        if (test) {
            final Row<T> row = this.getRow(rowIndex, createRowIfAbsent);
            final Column<T> col = new Column<>();
            col.setIndex(column);
            col.setName(this.header.get(column).getName());
            col.setValue(value);
            row.getColumns().add(col);
            return;
        }
        throw new IllegalArgumentException("row index not valid! Give only 0-" + (this.header.size() - 1));
    }

    public TableHeader getHeader(final int index) {
        if (index >= 0) {
            for (final TableHeader nextHeader : this.header) {
                if (nextHeader.getIndex() == index)
                    return nextHeader;
            }
        }
        throw new IllegalArgumentException("index not valid! Give only >= 0");
    }

    private Row<T> getRow(final int index, final boolean createIfAbsent) {
        if ((index <= (this.size() - 1)) && (index >= 0))
            return this.rows.get(index);
        if (createIfAbsent) {
            final int start = (this.rows.size() != 0) ? this.rows.size() - 1 : 0;
            final int amount = ((index - start) != 0) ? index - start : 1;
            this.addRows(amount);
            return this.getRow(index);
        }
        throw new IllegalArgumentException("row index not valid! Give only 0-" + (this.size() - 1));
    }

    private Row<T> getRow(final int index) {
        if ((index <= (this.size() - 1)) && (index >= 0))
            return this.rows.get(index);
        throw new IllegalArgumentException("row index not valid! Give only 0-" + (this.size() - 1));
    }

    public Column<T> getColumn(final int rowIndex, final int columnIndex) {
        final Row<T> row = this.getRow(rowIndex);
        if ((row.size() <= (row.size() - 1)) && (row.size() >= 0))
            return row.getColumns().get(columnIndex);
        throw new IllegalArgumentException("column index not valid! Give only 0-" + (row.size() - 1));
    }

    public Column<T> getColumnByName(final int rowIndex, final String columnName) {
        final Row<T> row = this.getRow(rowIndex);
        if (columnName != null) {
            for (final Column<T> nextCol : row.getColumns()) {
                if (nextCol.getName().equals(columnName))
                    return nextCol;
            }
            return null;
        }
        throw new IllegalArgumentException("name is null!");
    }
}
