import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

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

public class SymmetricKeyGen {
	
	public static int KEY_SIZE = 128;
	public static String ALGO = "AES";
	public static String FULL_ALGO = "AES/CBC/PKCS5Padding";
	public static int NUM_BYTES_IV = 16;
	
	public static SecretKey generateSessionKey(){
		KeyGenerator gen = null;
		SecretKey key = null;
		try {
			gen = KeyGenerator.getInstance(ALGO);
			gen.init(KEY_SIZE);
			key = gen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;
	}
	
	public static IvParameterSpec generateInitVector(){
		SecureRandom rngIV = new SecureRandom();
		byte[] iv = new byte[NUM_BYTES_IV];
		rngIV.nextBytes(iv);
		return new IvParameterSpec(iv);
	}
	
	public static String encode64(byte[] arr){
		return Base64.encode(arr);
	}
	
	public static byte[] decode64(String arr){
		return Base64.decode(arr);
	}
	
	public static byte[] encrypt(String msg, SecretKey key, byte[] iv){
		SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), ALGO);
		Cipher cipher;
		byte[] encryptedMessage = null;
		
		try {
			cipher = Cipher.getInstance(FULL_ALGO);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv)); 
			encryptedMessage = cipher.doFinal(msg.getBytes()); 
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("SymEnc: Exception");
		}
		return encryptedMessage;
	}

	public static String decrypt(byte[] eMsg, SecretKey key, byte[] iv){
		SecretKeySpec sessionKeySpec = new SecretKeySpec(key.getEncoded(), ALGO);
		Cipher cipher;
		String msg = null;
		
		try {
			cipher = Cipher.getInstance(FULL_ALGO);			
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, sessionKeySpec, ivspec); 
			byte[] decryptedMessageBytes = cipher.doFinal(eMsg); 
			msg = new String(decryptedMessageBytes);
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("SymDec: Exception");
			e.printStackTrace();
		}
		
		return msg;
		
	}
	


}


