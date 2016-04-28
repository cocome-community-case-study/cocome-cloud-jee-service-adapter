package cocome.cloud.sa.query.parsing;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import cocome.cloud.sa.query.Query;
import cocome.cloud.sa.query.SelectQuery;
import cocome.cloud.sa.query.parsing.QueryLexer.State4;
import cocome.cloud.sa.query.parsing.QueryLexer.State5;
import cocome.cloud.sa.query.parsing.QueryLexer.State6;

import de.kit.ipd.java.utils.framework.statemachine.Lexer;
import de.kit.ipd.java.utils.framework.statemachine.LexerVisitor;
import de.kit.ipd.java.utils.framework.statemachine.Parser;
import de.kit.ipd.java.utils.framework.statemachine.ParserVisitor;
import de.kit.ipd.java.utils.framework.statemachine.StateMachine;


public class QueryParser implements Parser<String>, LexerVisitor<CharSequence> {
	
	public static void main(String[] args) {
		String query = "query.select=entity.type=Store;Enterprise.name=Kaufland";
		QueryParser parser = new QueryParser();
		parser.parse(query);
	}
	
	/**************************************************************************
	 * FIELDS
	 *************************************************************************/
	
	private Lexer<CharSequence> lexer = new QueryLexer();
	
	private Map<String,String> map = new HashMap<String, String>();
	
	private Query query;
	
	private String strQuery;
	
	private int INIT_STATE = 0;
	private int EOL_STATE = 5;
	
	private String entityType;
	
	/**************************************************************************
	 * CONSTRUCTOR
	 *************************************************************************/
	
	public QueryParser() {
		lexer.getMachine().setEOLState(EOL_STATE);
		lexer.getMachine().addVisitor(this);
	}
	
	/**************************************************************************
	 * PUBLIC
	 *************************************************************************/
	
	public String getModel() {
		return strQuery;
	}
	
	public String getEntityType() {
		return entityType;
	}
	
	@Override
	public void setModel(String model) {
		this.strQuery = model;
	}
	
	public void setLexer(Lexer<CharSequence> lexer){
		this.lexer = lexer;
		lexer.getMachine().setEOLState(4);
		lexer.getMachine().addVisitor(this);
	}
	
	@Override
	public void parse(String content) {
		lexer.getMachine().setInput(content);
		lexer.getMachine().run(INIT_STATE);
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
				lexer.getMachine().run(INIT_STATE);
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
			case State6.INDEX:
				_selectQueryClass(val);
				break;
			case State4.INDEX:
				if(val.toLowerCase().startsWith("entity.type")){
					_selectEntityType(val);
					_appendProperty(val);
				} else {
					_appendProperty(val);
				}
				break;
			case State5.INDEX:
				_appendProperty(val);
				_buildQuery();
				break;
			}
		}
	}

	@Override
	public void addVisitor(ParserVisitor... visitors) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		return super.toString();
	}

	/**************************************************************************
	 * PRIVATE
	 *************************************************************************/
	
	private void _selectQueryClass(String token){
		//TODO implementation missing
	}
	
	private void _selectEntityType(String token) {
		entityType = token.split("=")[1].toLowerCase();
		query = new SelectQuery();
//		switch (val) {
//		case "store":
//			query = new StoreQuery2();
//			break;
//		default:
//			break;
//		}
	}
	
	private void _appendProperty(String token){
		String name = token.substring(0, token.indexOf("=", 0));
		String val = token.substring(token.indexOf("=", 0)+1, token.length());
		map.put(name, val);
	}
	
	private void _buildQuery() {
		strQuery = query.parse(map);
	}
}
