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

public class AESDecode {
	
	private static byte[] hexToBytes(String hexString) {
		char[] hex = hexString.toCharArray();
		
		int length = hex.length / 2;
		byte[] rawData = new byte[length];
		for (int i = 0; i < length; i++) {
			
			int high = Character.digit(hex[i * 2], 16);
			int low = Character.digit(hex[i * 2 + 1], 16);
			//ex: 00001000 => 10000000 (8=>128)
			//ex: 10000000 | 00001100 => 10001100 (137)
			int value = (high << 4) | low;
			
			if( value > 127 ) {
				value -= 256;
			}
			
			rawData [i] = (byte) value;
		}
		return rawData;
	}
	
	public static String Decode( String encrypt_code , String k ) {
		try {
			
			KeyGenerator keyG = KeyGenerator.getInstance("AES");
				
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			
			secureRandom.setSeed( k.getBytes() );
			keyG.init( 128, secureRandom );
			//keyG.init( 128 , new SecureRandom(k.getBytes() ) );
			
			
			SecretKey secuK = keyG.generateKey();
			
			
			byte[] key = secuK.getEncoded();

			
			SecretKeySpec spec = new SecretKeySpec(key, "AES");
			Cipher cipher;
	
			cipher = Cipher.getInstance("AES");
		
			
			cipher.init(Cipher.DECRYPT_MODE, spec);
			
			String data = new String( cipher.doFinal( hexToBytes(encrypt_code) ) );
			
			return data;
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
