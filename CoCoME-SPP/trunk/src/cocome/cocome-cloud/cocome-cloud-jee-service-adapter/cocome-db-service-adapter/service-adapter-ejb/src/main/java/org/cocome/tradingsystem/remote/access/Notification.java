package org.cocome.tradingsystem.remote.access;

import java.io.Serializable;

import de.kit.ipd.java.utils.framework.table.Table;
import de.kit.ipd.java.utils.time.TimeUtils;


/**
 * 
 * @author AlessandroGiusa@gmail.com
 *
 */
public class Notification implements Serializable{
	
	// **********************************************************************
	// * STATIC															    *
	// **********************************************************************
	
	private static final long serialVersionUID = -3464575709846719646L;

	public static final String SUCCESS = "SUCCESS";
	
	public static final String FAILED = "FAILED";
	
	public static final String WARNING = "WARNING";
	
	// **********************************************************************
	// * FIELDS																*
	// **********************************************************************
	
	private final Table<String> table = new Table<>();
	
	private int row = 0;
	
	// **********************************************************************
	// * CONSTRUCTORS														*
	// **********************************************************************
	
	public Notification() {
		table.addHeader("Time","Label","Status","Description");
	}
	
	// **********************************************************************
	// * PUBLIC																*
	// **********************************************************************
	
	public Table<String> getNotification() {
		return table;
	}
	
	public void addNotification(String label,String status, String text) {
		createTag(label,status, text);
	}
	
	// **********************************************************************
	// * PRIVATE															*
	// **********************************************************************
	
	private void createTag(String label,String status, String text){
		table.addColumn(row, 0, TimeUtils.getTime(),true);
		table.addColumn(row, 1, label,true);
		table.addColumn(row, 2, status,true);
		table.addColumn(row, 3, "\""+text+"\"",true);
		row++;
	}
	
}
