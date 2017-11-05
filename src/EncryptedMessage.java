import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.crypto.SecretKey;

public class EncryptedMessage extends Message{
		
	
	private static final long serialVersionUID = 1L;
		public byte[] iv = new byte[SymKeyGen.NUM_BYTES_IV];
		public byte[] encryptedMessage;
		public byte[] messageAuthCode;
		
		EncryptedMessage(byte[] iv, byte[] enc, byte[] mac){
			super();
			this.iv = iv;
			this.encryptedMessage = enc;
			this.messageAuthCode = mac;
		}
		
		EncryptedMessage(String message, SecretKey key){
			super();
			this.iv = SymKeyGen.generateInitVector().getIV();
			this.encryptedMessage = SymKeyGen.encrypt(message, key, this.iv);
			this.messageAuthCode = null; //TODO implement
		}
		
		private String encoded(){
			String eMsg = SymKeyGen.encode64(this.iv) + ":" 
						+ SymKeyGen.encode64(this.encryptedMessage) + ":"
						+ SymKeyGen.encode64(this.messageAuthCode);
			return eMsg;
		}
		
		public String decrypt(SecretKey key){
			String decryptedMsg = SymKeyGen.decrypt(this.encryptedMessage, key, this.iv);
			return decryptedMsg;
		}
		
		//TODO implement
		public boolean verify(){
			return false;
			
		}
		
		public void send(ObjectOutputStream out) throws IOException{
			out.writeObject(this.encoded());
		}
		
	}