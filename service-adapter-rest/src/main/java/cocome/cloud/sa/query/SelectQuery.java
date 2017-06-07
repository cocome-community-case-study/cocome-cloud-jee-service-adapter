package cocome.cloud.sa.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.kit.ipd.java.utils.strings.StringUtils;

public class SelectQuery implements IQuery {

	

	private static final String ENTITY_TYPE = "entity.type";

	private static final String ENTITY_TYPE_VAR = "e";

	private final Map<String, String> variables = new HashMap<>();
	private String entityType;

	private final Random random = new Random(System.currentTimeMillis());

	/** Empty constructor. */
	public SelectQuery() {}

	@Override
	public String parse(final Map<String, String> param) {
		this.entityType = this.getParameter(ENTITY_TYPE, param);
		if (this.entityType == null || this.entityType.isEmpty())
			throw new IllegalArgumentException("the query-item " + ENTITY_TYPE + " is missing!");
		this.entityType = StringUtils.firstUpper(this.entityType);
		param.remove(ENTITY_TYPE);
		// calc the variables names
		this.calcNeededVariables(param);

		final StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append(" DISTINCT ");
		query.append(ENTITY_TYPE_VAR);
		query.append(" FROM ");
		query.append(this.entityType);
		query.append(" ");
		query.append(ENTITY_TYPE_VAR);

		// JOIN-PART
		if (this.variables.size() > 1) {
			final String innerjoin = this.createJointStatement();
			query.append(innerjoin);
		}

		// WHERE-PART
		if (this.variables.size() > 0) {
			final String where = this.createWhereStatement(param);
			query.append(where);
		}
		System.out.println("Query build:" + query.toString());
		return query.toString();
	}

	private String createWhereStatement(final Map<String, String> param) {
		final StringBuilder sb = new StringBuilder();
		sb.append(" WHERE ");
		// s.name LIKE
		String typeName;
		String typeProperty;
		String varName;
		final int len = param.size();
		int counter = 0;
		for (final String nextParam : param.keySet()) {
			typeName = nextParam.substring(0, nextParam.indexOf("."));
			typeProperty = nextParam.substring(nextParam.indexOf(".") + 1, nextParam.length());
			varName = this.getParameter(typeName, this.variables);
			sb.append(varName);
			sb.append(".");
			sb.append(typeProperty);
			sb.append(" ");
			sb.append(param.get(nextParam).replaceAll("\\*", "%"));
			sb.append(" ");
			if (counter + 1 < len) {
				sb.append(" AND ");
			}
			counter++;
		}
		return sb.toString();
	}

	private String createJointStatement() {
		final StringBuilder sb = new StringBuilder();
		final String join = " INNER JOIN ";
		for (final String next : this.variables.keySet()) {
			if (next.equalsIgnoreCase(this.entityType)) {
				continue;
			}
			sb.append(join);
			sb.append(ENTITY_TYPE_VAR);
			sb.append(".");
			sb.append(next);
			sb.append(" ");
			sb.append(this.variables.get(next));
		}
		return sb.toString();
	}

	private void calcNeededVariables(final Map<String, String> param) {
		String tmp;
		String varName;
		this.variables.put(this.entityType, ENTITY_TYPE_VAR);
		for (final String nextParam : param.keySet()) {
			tmp = nextParam.substring(0, nextParam.indexOf("."));
			if (!tmp.toLowerCase().equals(this.entityType.toLowerCase())) {
				varName = this.getRandomLetter();
				while (this.isVarNameAvailable(varName)) {
					varName += this.getRandomLetter();
				}
				this.variables.put(tmp, varName);
			} else {
				// is the done more then once but doesn't matter
				this.variables.put(this.entityType, ENTITY_TYPE_VAR);
			}
		}
	}

	private boolean isVarNameAvailable(final String name) {
		if (name.equals(ENTITY_TYPE_VAR))
			return true;
		for (final String nextName : this.variables.values()) {
			if (name.equals(nextName))
				return true;
		}
		return false;
	}

	private String getParameter(final String name, final Map<String, String> param) {
		String paramVal = param.get(name);
		if (paramVal == null) {
			for (final String nextParam : param.keySet()) {
				if (nextParam.toLowerCase().equals(name.toLowerCase())) {
					paramVal = param.get(nextParam);
					paramVal = paramVal.replaceAll("\\*", "%");
					break;
				}
			}
		}
		return paramVal;
	}

	private String getRandomLetter() {
		// ASCII 97-122 lower-letters
		return String.valueOf((char) (97 + this.random.nextInt(26)));
	}

}
