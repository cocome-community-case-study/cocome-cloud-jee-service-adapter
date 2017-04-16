package cocome.cloud.sa.query.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import de.kit.ipd.java.utils.framework.statemachine.ILexer;
import de.kit.ipd.java.utils.framework.statemachine.ILexerVisitor;
import de.kit.ipd.java.utils.framework.statemachine.IParser;
import de.kit.ipd.java.utils.framework.statemachine.IParserVisitor;
import de.kit.ipd.java.utils.framework.statemachine.IStateMachine;

import cocome.cloud.sa.query.IQuery;
import cocome.cloud.sa.query.SelectQuery;
import cocome.cloud.sa.query.parsing.QueryLexer.State4;
import cocome.cloud.sa.query.parsing.QueryLexer.State5;
import cocome.cloud.sa.query.parsing.QueryLexer.State6;

public class QueryParser implements IParser<String>, ILexerVisitor<CharSequence> {

	

	private static final int INIT_STATE = 0;
	private static final int EOL_STATE = 5;

	private ILexer<CharSequence> lexer = new QueryLexer();

	private final Map<String, String> map = new HashMap<String, String>();

	private IQuery query;

	private String strQuery;

	private String entityType;

	/**
	 * Constructor.
	 */
	public QueryParser() {
		this.lexer.getMachine().setEOLState(QueryParser.EOL_STATE);
		this.lexer.getMachine().addVisitor(this);
	}

	@Override
	public String getModel() {
		return this.strQuery;
	}

	public String getEntityType() {
		return this.entityType;
	}

	@Override
	public void setModel(final String model) {
		this.strQuery = model;
	}

	/**
	 * This assigns a lexer and sets the state machine on state 4.
	 *
	 * @param lexer
	 *            the lexer to be set
	 */
	public void setLexer(final ILexer<CharSequence> lexer) {
		this.lexer = lexer;
		lexer.getMachine().setEOLState(4);
		lexer.getMachine().addVisitor(this);
	}

	@Override
	public void parse(final String content) {
		this.lexer.getMachine().setInput(content);
		this.lexer.getMachine().run(QueryParser.INIT_STATE);
	}

	@Override
	public void parse(final InputStream in) {
		try {
			if (in != null && in.available() != -1) {
				final BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String line = null;
				final StringBuilder content = new StringBuilder();
				while ((line = br.readLine()) != null) {
					content.append(line);
					content.append(System.lineSeparator());
				}
				this.lexer.getMachine().setInput(content);
				this.lexer.getMachine().run(QueryParser.INIT_STATE);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(final IStateMachine<CharSequence> machine, final int state, final CharSequence token) {
		final String val = String.valueOf(token);
		if (!val.isEmpty()) {
			switch (state) {
			case State6.INDEX:
				this.selectQueryClass(val);
				break;
			case State4.INDEX:
				if (val.toLowerCase().startsWith("entity.type")) {
					this.selectEntityType(val);
					this.appendProperty(val);
				} else {
					this.appendProperty(val);
				}
				break;
			case State5.INDEX:
				this.appendProperty(val);
				this.buildQuery();
				break;
			default:
				break;
			}
		}
	}

	@Override
	public String toString() {
		return super.toString();
	}

	private void selectQueryClass(final String token) {
		// TODO implementation missing
	}

	private void selectEntityType(final String token) {
		this.entityType = token.split("=")[1].toLowerCase();
		this.query = new SelectQuery();
		// switch (val) {
		// case "store":
		// query = new StoreQuery2();
		// break;
		// default:
		// break;
		// }
	}

	private void appendProperty(final String token) {
		final String name = token.substring(0, token.indexOf("=", 0));
		final String val = token.substring(token.indexOf("=", 0) + 1, token.length());
		this.map.put(name, val);
	}

	private void buildQuery() {
		this.strQuery = this.query.parse(this.map);
	}

	@Override
	public void addVisitor(final IParserVisitor<String> visitor) {
		// TODO Auto-generated method stub

	}
}
