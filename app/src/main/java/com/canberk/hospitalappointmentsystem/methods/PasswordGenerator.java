package com.canberk.hospitalappointmentsystem.methods;

import java.util.Random;

/**
 * A class for simple password generation.
 *
 */
public class PasswordGenerator {

	private static final char[] symbols;

	  static {
	    StringBuilder tmp = new StringBuilder();
	    for (char ch = '0'; ch <= '9'; ++ch)
	      tmp.append(ch);
	    for (char ch = 'a'; ch <= 'z'; ++ch)
	      tmp.append(ch);
	    symbols = tmp.toString().toCharArray();
	  } 
	private final Random random = new Random();

	  private final char[] buf;

	  /**
	 * @param length of password to be generated
	 *  
	 */
	public PasswordGenerator(int length) {
	    if (length < 1)
	      throw new IllegalArgumentException("length < 1: " + length);
	    buf = new char[length];
	  }

	  /**
	 * @return generated password
	 */
	public String generate() {
	    for (int idx = 0; idx < buf.length; ++idx) 
	      buf[idx] = symbols[random.nextInt(symbols.length)];
	    return new String(buf);
	  }
	}
