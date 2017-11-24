package Core.util.Encryption;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESEncode {
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	public static String Encode( String data , String k ) {
		try {
			
			KeyGenerator keyG = KeyGenerator.getInstance("AES");
			
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(k.getBytes());
			
			keyG.init(128, secureRandom);
			
			SecretKey secuK = keyG.generateKey();
			
			byte[] key = secuK.getEncoded();
			
			
			SecretKeySpec spec = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			
			
			cipher.init(Cipher.ENCRYPT_MODE, spec);
			
			byte[] encryptData = cipher.doFinal( data.getBytes() );
	
			String tempx = bytesToHex( encryptData );

			return tempx;
		} catch (IllegalBlockSizeException e) {
			return null;
		} catch (BadPaddingException e) {
			return null;
		} catch( NoSuchAlgorithmException e ) {
			return null;
		} catch (InvalidKeyException e) {
			return null;
		} catch( NoSuchPaddingException e ) {
			return null;
		}
	}
}
