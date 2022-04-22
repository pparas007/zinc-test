package com.test.demo.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordHashingUtility {
	/**
	 * Salt to prevent dictionary attacks
	 */
	private static String salt="912hf18nbr5b3h1hhqpm";
	
	/**
	 * Utility method to calculate hash of the pin using SHA-512
	 *
	 * @param  unencryptedPassword
	 * @return hashedPassword   
	 */
	public static String calculatePasswordHash(String unencryptedPassword) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			//addding salt to the hashing algorithm
			md.update(salt.getBytes());
			
			byte[] hashedPassword = md.digest(unencryptedPassword.getBytes(StandardCharsets.UTF_8));
			
			return new String(hashedPassword);
		}catch(Exception e) {
			
		}
		return unencryptedPassword;
	}
}
