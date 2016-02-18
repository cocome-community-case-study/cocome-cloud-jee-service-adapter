package cocome.cloud.sa.query;

import java.util.Map;

public interface Query {
	
	String parse(Map<String,String> param);

}
