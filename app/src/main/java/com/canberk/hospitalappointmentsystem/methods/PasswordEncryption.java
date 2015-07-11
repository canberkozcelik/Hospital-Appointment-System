package com.canberk.hospitalappointmentsystem.methods;

import java.io.UnsupportedEncodingException; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 
 
/**
 * A class for password encryption.
 *
 */
public class PasswordEncryption { 
 
    /**
     * @param data
     * @return
     */
    private static String convertToHex(byte[] data) { 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    } 
 
    /**
     * Password encryption of user by SHA1
     * @param text
     * @return encrypted password of user
     */
    public static String SHA1(String text){ 
	    try {
			MessageDigest md;
			md = MessageDigest.getInstance("SHA-1");
			byte[] sha1hash = new byte[40];
			md.update(text.getBytes("iso-8859-1"), 0, text.length());
			sha1hash = md.digest();
			return convertToHex(sha1hash);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
    } 
} 
