package cocome.cloud.sa.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.kit.ipd.java.utils.strings.StringUtils;

public class SelectQuery implements Query {
	
	public static void main(String[] args) {
		HashMap<String, String> map = new HashMap<>();
		map.put("entity.type", "StockItem");
		map.put("store.name", "like'*'");
		
		SelectQuery qu = new SelectQuery();
		qu.parse(map);
		
		
	}
	
	private static final String ENTITY_TYPE = "entity.type";
	
	private Map<String, String> variables = new HashMap<String, String>();
	private String entityType;
	
	private static final String entityTypeVar = "e";
	
	private Random random = new Random(System.currentTimeMillis());
	
	@Override
	public String parse(Map<String, String> param) {
		entityType = getParameter(ENTITY_TYPE, param);
		if(entityType==null || entityType.isEmpty()){
			throw new IllegalArgumentException("the query-item "+ENTITY_TYPE+" is missing!");
		}
		entityType = StringUtils.firstUpper(entityType);
		param.remove(ENTITY_TYPE);
		//calc the variables names
		calcNeededVariables(param);
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append(" DISTINCT ");
		query.append(entityTypeVar);
		query.append(" FROM ");
		query.append(entityType);
		query.append(" ");
		query.append(entityTypeVar);
		
		//JOIN-PART
		if(variables.size()>1){
			String innerjoin = createJointStatement();
			query.append(innerjoin);
		}
		
		//WHERE-PART	
		if(variables.size()>0){
			String where = createWhereStatement(param);
			query.append(where);
		}
		System.out.println("Query build:"+query.toString());
		return query.toString();
	}
	
	private String createWhereStatement(Map<String, String> param) {
		StringBuilder sb = new StringBuilder();
		sb.append(" WHERE ");
//		 s.name LIKE
		String typeName;
		String typeProperty;
		String varName;
		int len = param.size();
		int counter = 0;
		int cmpMeth = -1;
		for(String nextParam:param.keySet()){
			typeName = nextParam.substring(0, nextParam.indexOf("."));
			typeProperty = nextParam.substring(nextParam.indexOf(".")+1, nextParam.length());
			varName = getParameter(typeName, variables);
			sb.append(varName);
			sb.append(".");
			sb.append(typeProperty);
			sb.append(" ");
			sb.append(param.get(nextParam).replaceAll("\\*", "%"));
			sb.append(" ");
			if((counter+1)<len){
				sb.append(" AND ");
			}
			counter++;
		}
		return sb.toString();
	}

	private String createJointStatement(){
		StringBuilder sb = new StringBuilder();
		String join = " INNER JOIN ";
//		sb.append(join);
		int len = variables.size();
		int counter = 0;
		for(String next:variables.keySet()){
			if(next.equalsIgnoreCase(entityType)){
				counter++;
				continue;
			}
			sb.append(join);
			sb.append(entityTypeVar);
			sb.append(".");
			sb.append(next);
			sb.append(" ");
			sb.append(variables.get(next));
//			if((counter+1)<len){
//				sb.append(join);
//			}
			counter++;
		}
		return sb.toString();
	}
	
	private void calcNeededVariables(Map<String, String> param){
		String tmp;
		String varName;
		variables.put(entityType, entityTypeVar);
		for(String nextParam:param.keySet()){
			tmp = nextParam.substring(0, nextParam.indexOf("."));
			if(!tmp.toLowerCase().equals(entityType.toLowerCase())){
				varName = getRandomLetter();
				while(isVarNameAvailable(varName)){
					varName+=getRandomLetter();
				}
				variables.put(tmp, varName);
				varName = "";
			} else {
				//is the done more then once but doesn't matter
				variables.put(entityType, entityTypeVar);
			}
		}
	}
	
	private boolean isVarNameAvailable(String name){
		if(name.equals(entityTypeVar)){
			return true;
		}
		for(String nextName:variables.values()){
			if(name.equals(nextName)){
				return true;
			}
		}
		return false;
	}
	
	private String getParameter(String name, Map<String, String> param){
		String paramVal = param.get(name);
		if(paramVal==null){
			for(String nextParam:param.keySet()){
				if(nextParam.toLowerCase().equals(name.toLowerCase())){
					paramVal = param.get(nextParam);
					paramVal = paramVal.replaceAll("\\*", "%");
					break;
				}
			}
		}
		return paramVal;
	}
	
	private String getRandomLetter(){
		//ASCII 97-122 lower-letters
		return String.valueOf((char)(97+random.nextInt(26)));
	}
	
}
