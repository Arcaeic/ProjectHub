import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


public class SymmetricKeyGen {
	
	public SymmetricKeyGen(){
		
	}
	
	/*
	 * Question 3: You should use symmetric encryption for 
	 * confidentiality, as it is much faster than asymmetric crypto. 
	 * I recommend AES - but you can use a different cipher. 
	 * I suggest to create a new symmetric session key for 
	 * each new session. This can be done on the client. 
	 * The key can be sent to the server using 
	 * asymmetric crypto (encrypted with the server's public key).
	 * Question 5: Yes, AES works only with symmetrical keys. 
	 * Use DES or another asymmetrical cipher for establishing 
	 * the symmetrical session key 
	 * and ensuring integrity in that process.
	 */
	public static byte[] generateSessionKey(){
		
		byte[] key = new byte[16]; //16 bytes = 128 bits
		SecureRandom rng = new SecureRandom();
		rng.nextBytes(key);
		
		return key;
	
	}
	
	public static String encryptMessage(String msg, byte[] key){
		String base64key = Base64.encode(key);
		System.out.println("SymEnc: 128 bit key (with Base64 encoding): " + base64key );
	
		SecretKeySpec sessionKeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher;
		String base64EncodedEncryptedMsg = null;
		try {
			
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecureRandom rngIV = new SecureRandom();
			byte[] iv = new byte[16];
			rngIV.nextBytes(iv);
	        IvParameterSpec ivspec = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, sessionKeySpec, ivspec); 
			byte[] encryptedMessageInBytes = cipher.doFinal(msg.getBytes()); 
			base64EncodedEncryptedMsg = Base64.encode(iv) + ":" + Base64.encode(encryptedMessageInBytes); 

			
		} catch (NoSuchAlgorithmException e) {
			System.out.println("SymEnc: algorithm not found");
		} catch (NoSuchPaddingException e) {
			System.out.println("SymEnc: padding scheme not found");
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (InvalidKeyException e) {
			System.out.println("SymEnc: invalid key");
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return base64EncodedEncryptedMsg;
		
	}
	
	
	public static String decryptMessage(String encryptedMsg, byte[] key){

		String base64key = Base64.encode(key);
		System.out.println("SymDec: 128 bit key (with Base64 encoding): " + base64key );
	
		SecretKeySpec sessionKeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher;
		String msg = null;
		try {
			
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			byte[] iv = new byte[16];
			
			String[] encodedData = encryptedMsg.split(":");
			
			String encodedIvStr = encodedData[0];
			iv = Base64.decode(encodedIvStr);

			byte[] encryptedMessageBytes = Base64.decode(encodedData[1]);
			
			
	        IvParameterSpec ivspec = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, sessionKeySpec, ivspec); 
			byte[] decryptedMessageBytes = cipher.doFinal(encryptedMessageBytes); 
			msg = new String(decryptedMessageBytes);
			
		} catch (NoSuchAlgorithmException e) {
			System.out.println("SymEnc: algorithm not found");
		} catch (NoSuchPaddingException e) {
			System.out.println("SymEnc: padding scheme not found");
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (InvalidKeyException e) {
			System.out.println("SymEnc: invalid key");
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return msg;
		
	}
	

}
