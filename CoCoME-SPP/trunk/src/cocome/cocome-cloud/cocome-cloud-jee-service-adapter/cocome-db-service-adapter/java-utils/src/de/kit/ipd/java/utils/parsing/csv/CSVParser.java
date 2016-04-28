package de.kit.ipd.java.utils.parsing.csv;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.kit.ipd.java.utils.framework.statemachine.Lexer;
import de.kit.ipd.java.utils.framework.statemachine.LexerVisitor;
import de.kit.ipd.java.utils.framework.statemachine.Parser;
import de.kit.ipd.java.utils.framework.statemachine.ParserVisitor;
import de.kit.ipd.java.utils.framework.statemachine.StateMachine;
import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Row;
import de.kit.ipd.java.utils.framework.table.Table;
import de.kit.ipd.java.utils.framework.table.TableHeader;
import de.kit.ipd.java.utils.parsing.csv.CSVLexer.State4;
import de.kit.ipd.java.utils.parsing.csv.CSVLexer.State5;
import de.kit.ipd.java.utils.parsing.csv.CSVLexer.State6;


public class CSVParser implements Parser<Table<String>>, LexerVisitor<CharSequence> {
	
	/**************************************************************************
	 * FIELDS
	 *************************************************************************/
	
	private Lexer<CharSequence> lexer = new CSVLexer();
	
	private int colCounter = 0;
	
	private int rowCounter = 0;
	
	private Table<String> table = new Table<>();
	
	private List<TableHeader> header = new ArrayList<TableHeader>();
	
	private Row<String> currentRow;
	
	/**************************************************************************
	 * CONSTRUCTOR
	 *************************************************************************/
	
	public CSVParser() {
		lexer.getMachine().setEOLState(4);
		lexer.getMachine().addVisitor(this);
		
		currentRow = new Row<>();
		currentRow.setIndex(0);
		
		table.setHeader(header);
		
		
	}
	
	/**************************************************************************
	 * PUBLIC
	 *************************************************************************/
	
	public void setLexer(Lexer<CharSequence> lexer){
		this.lexer = lexer;
		lexer.getMachine().setEOLState(4);
		lexer.getMachine().addVisitor(this);
	}
	
	@Override
	public void parse(String content) {
		lexer.getMachine().setInput(content);
		lexer.getMachine().run(0);
	}
	
	public Table<String> getTable() {
		return table;
	}
	
	@Override
	public void parse(InputStream in) {
		try {
			if(in!=null && in.available()!=-1) {
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String line = null;
				StringBuilder content = new StringBuilder();
				while((line=br.readLine())!=null){
					content.append(line);
					content.append(System.lineSeparator());
				}
				lexer.getMachine().setInput(content);
				lexer.getMachine().run(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(StateMachine<CharSequence> machine, int state, CharSequence token) {
		String val = String.valueOf(token);
		if(!val.isEmpty()) {
			switch (state) {
			case State5.INDEX:
				_buildHeader(String.valueOf(token));
				break;
			case State6.INDEX:
				_buildHeader(String.valueOf(token));
				break;
			case State4.INDEX:
				_buildTable(String.valueOf(token));
				break;
			}
		}
	}
	
	@Override
	public void addVisitor(ParserVisitor... visitors) {
		// TODO Auto-generated method stub
		
	}
	
	public void setTable(Table<String> table){
		this.table = table;
	}
	
	@Override
	public String toString() {
		return _generateCSVString();
	}
	
	@Override
	public Table<String> getModel() {
		return this.table;
	}
	
	@Override
	public void setModel(Table<String> model) {
		this.table = model;
	}

	/**************************************************************************
	 * PRIVATE
	 *************************************************************************/
	
	private void _buildHeader(String token){
		TableHeader header = new TableHeader();
		header.setName(String.valueOf(token.trim()));
		header.setIndex(this.header.size());
		this.header.add(header);
	}
	
	private void _buildTable(String token){
		Column<String> col = new Column<>();
		col.setName(table.getHeader(colCounter).getName());
		col.setIndex(colCounter);
		col.setValue(String.valueOf(token));
		
		currentRow.getColumns().add(col);
		colCounter++;
		
		if(colCounter==table.getHeader().size()){
			colCounter = 0;
			rowCounter++;
			table.getRows().add(currentRow);
			currentRow = new Row<String>();
			currentRow.setIndex(rowCounter);
		}
	}
	
	private String _generateCSVString() {
		StringBuilder builder = new StringBuilder();
		List<TableHeader> header = table.getHeader();
		
		//HEADER
		int len = header.size();
		for (int i = 0; i < len; i++) {
			builder.append(header.get(i).getName());
			if ((i + 1) < len) {
				builder.append(";");// TODO externalize
			}
		}
		builder.append(System.lineSeparator());
		
		//BODY
		List<Row<String>> _rows = table.getRows();
		int lenRow = _rows.size();
		for (int j = 0; j < lenRow; j++) {
			for (int i = 0; i < len; i++) {
				builder.append(_rows.get(j).getColumns().get(i).getValue());
				if ((i + 1) < len) {
					builder.append(";");// TODO externalize
				}
			}
			if ((j + 1) < lenRow) {
				builder.append(System.lineSeparator());// TODO externalize
			}
		}
		return builder.toString();
	}
	
}
