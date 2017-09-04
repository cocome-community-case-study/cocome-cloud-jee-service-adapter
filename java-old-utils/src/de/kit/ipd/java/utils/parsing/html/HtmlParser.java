package de.kit.ipd.java.utils.parsing.html;

import de.kit.ipd.java.utils.framework.table.Table;
import de.kit.ipd.java.utils.framework.table.TableHeader;

public class HtmlParser {
	
	
	// **********************************************************************
	// * FIELDS																*
	// **********************************************************************
	
	private Table<String> table;
	
	// **********************************************************************
	// * CONSTRUCTORS														*
	// **********************************************************************
	public HtmlParser() {}
	
	// **********************************************************************
	// * PUBLIC																*
	// **********************************************************************

	
	public void setTable(Table<String> table){
		this.table = table;
	}
	
	
	@Override
	public String toString() {
		return _generateHtml();
	}
	
	// **********************************************************************
	// * PRIVATE															*
	// **********************************************************************
	
	private String _generateHtml(){
		StringBuilder builder = new StringBuilder();
		builder.append("<table border=\"1\" cellspacing=\"2\" cellpadding=\"2\">");
		builder.append("<tr>");
		for(TableHeader nextHeader:table.getHeader()){
			builder.append("<th>"+nextHeader.getName()+"</th>");
		}
		builder.append("</tr>");
		builder.append("</table>");
		return builder.toString();
	}
	
//	<table>
//	  <tr>
//	    <th id="name">Name</th>
//	    <th id="email">Email</th>
//	    <th id="phone">Phone</th>
//	    <th id="addr">Address</th>
//	  </tr>
//	  <tr>
//	    <td headers="name">John Doe</td>
//	    <td headers="email">someone@example.com</td>
//	    <td headers="phone">+45342323</td>
//	    <td headers="addr">Rosevn 56,4300 Sandnes,Norway</td>
//	  </tr>
//	</table> 
	
}
