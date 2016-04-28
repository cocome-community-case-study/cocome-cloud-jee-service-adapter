package org.cocome.tradingsystem.usermanager.credentials;

import java.security.SecureRandom;

public final class RandomPasswordGenerator {
	private static final SecureRandom RAND = new SecureRandom();
	
	private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
	private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String DIGITS = "1234567890";
	private static final String ALL = LOWER_CASE + UPPER_CASE + DIGITS;
	private static final char[] ALL_CHARS = ALL.toCharArray();
	
	public static final int PASSWORD_LENGTH = 12;
	
	/**
	 * Generates a new password string. It uses a secure random number
	 * generator and returns alpha-numeric strings without special characters. 
	 * 
	 * @return a randomly generated password string
	 */
	public static char[] generatePassword() {
		// TODO Add some kind of password rules
		char[] buffer = new char[PASSWORD_LENGTH];
		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			buffer[i] = ALL_CHARS[RAND.nextInt(ALL_CHARS.length)];
		}
		return buffer;
	}
}
