import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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
	public byte[] generate32BitSessionKey(){
		
		byte[] key = new byte[32]; //32 bytes = 256 bits
		SecureRandom rng = new SecureRandom();
		rng.nextBytes(key);
		
		return key;
	
	}
	
	public String encryptMessage(String msg, byte[] key){
		System.out.println();
		String base64key = Base64.encode(key);
		System.out.println("SymEnc: 256 bit key (with Base64 encoding): " + base64key );
	
		SecretKeySpec sessionKeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher;
		String base64EncodedEncryptedMsg = null;
		try {
			
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecureRandom rngIV = new SecureRandom();
			cipher.init(Cipher.ENCRYPT_MODE, sessionKeySpec, rngIV); 
			byte[] encryptedMessageInBytes = cipher.doFinal(msg.getBytes("UTF-8")); 
			base64EncodedEncryptedMsg = Base64.encode(encryptedMessageInBytes); 

			
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
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			System.out.println("SymEnc: invalid key");
			e.printStackTrace();
		} 
		
		return base64EncodedEncryptedMsg;
		
	}
	
	

}
