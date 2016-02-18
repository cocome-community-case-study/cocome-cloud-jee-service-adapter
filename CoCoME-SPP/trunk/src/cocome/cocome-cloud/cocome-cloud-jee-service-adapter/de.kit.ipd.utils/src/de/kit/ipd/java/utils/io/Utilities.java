package de.kit.ipd.java.utils.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class Utilities {

	private Utilities(){}
	
	/**
	 * Close the input by yourself
	 * @param input
	 * @return
	 */
	public static String getString(InputStream input) {
		BufferedInputStream in = null;
		try {
			if (input.available() != -1) {
				in = new BufferedInputStream(input);
				StringBuilder builder = new StringBuilder();
				byte[] myBytes = new byte[256];
				while (in.read(myBytes) != -1) {
					builder.append(new String(myBytes, "UTF-8"));
					myBytes = null;
					myBytes = new byte[256];
				}
				return builder.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
