import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class EncryptedMessage extends Message{
		
	
	private static final long serialVersionUID = 1L;
		public byte[] iv = new byte[SymKeyGen.NUM_BYTES_IV];
		public byte[] message;
		public byte[] messageAuthCode;
		
		EncryptedMessage(byte[] iv, byte[] enc, byte[] mac){
			super();
			this.iv = iv;
			this.message = enc;
			this.messageAuthCode = mac;
		}
		
		/**
		 * Encryption only. Does not store any plaintext.
		 * @param message
		 * @param key
		 */
		EncryptedMessage(String message, SecretKey key){
			super();
			this.iv = SymKeyGen.generateInitVector().getIV();
			this.message = SymKeyGen.encrypt(message, key, this.iv);
			this.messageAuthCode = null;
		}
		
		/**
		 * Allows toggling Confidentiality and Integrity through {@code boolean} parameters.
		 * @param message
		 * @param key
		 * @param macKey
		 * @param enableConfidential
		 * @param enableIntegrity
		 */
		EncryptedMessage(String message, SecretKey key, SecretKey macKey, boolean enableConfidential, boolean enableIntegrity){
			super();
			
			this.iv = SymKeyGen.generateInitVector().getIV();
			
			if(enableConfidential){
				this.message = SymKeyGen.encrypt(message, key, this.iv);
			}else{
				this.message = message.getBytes();
			}
			
			if(enableIntegrity){
				this.messageAuthCode = generateMAC(this.message, macKey);
			}else{
				this.messageAuthCode = null;
			}
		}
		
		private String encoded(){
			String eMsg = SymKeyGen.encode64(this.iv) + ":" 
						+ SymKeyGen.encode64(this.message) + ":"
						+ SymKeyGen.encode64(this.messageAuthCode);
			return eMsg;
		}
		
		public String decrypt(SecretKey key){
			String decryptedMsg = SymKeyGen.decrypt(this.message, key, this.iv);
			return decryptedMsg;
		}
		
		private byte[] generateMAC(byte[] data, SecretKey key){

			byte[] macSig = null;
			try {

			      Mac theMac = Mac.getInstance("HmacSHA256");
			      theMac.init(key);

			      macSig = theMac.doFinal(data);
			}
			catch (NoSuchAlgorithmException | InvalidKeyException ex) { ex.printStackTrace(); }
			
			return macSig;
		}
		
		public boolean verifyMAC(SecretKey macKey){
			boolean success = false;
			
			if(this.message != null){
				byte[] newMac = generateMAC(this.message, macKey);
				String encodedNewMac = SymKeyGen.encode64(newMac);
				String encodedMac = SymKeyGen.encode64(this.messageAuthCode);
				if(encodedNewMac.equals(encodedMac) ){
					success = true;
				}
			}else{
				success = false;
			}
			
			return success;
		}
		
		public void send(ObjectOutputStream out) throws IOException{
			out.writeObject(this.encoded());
		}
		
	}