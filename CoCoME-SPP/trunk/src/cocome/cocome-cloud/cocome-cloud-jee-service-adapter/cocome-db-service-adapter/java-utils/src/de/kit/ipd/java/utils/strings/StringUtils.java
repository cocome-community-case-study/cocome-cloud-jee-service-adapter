package de.kit.ipd.java.utils.strings;

public final class StringUtils {

	
	private StringUtils(){}
	
	public static String firstUpper(String text){
		return text.substring(0, 1).toUpperCase() + text.substring(1, text.length());
	}
}
