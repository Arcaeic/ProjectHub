import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.crypto.SecretKey;

public class EncryptedMessage extends Message{
		
	
	private static final long serialVersionUID = 1L;
		public byte[] iv = new byte[SymmetricKeyGen.NUM_BYTES_IV];
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
			this.iv = SymmetricKeyGen.generateInitVector().getIV();
			this.encryptedMessage = SymmetricKeyGen.encrypt(message, key, this.iv);
			this.messageAuthCode = null; //TODO implement
		}
		
		private String encoded(){
			String eMsg = SymmetricKeyGen.encode64(this.iv) + ":" 
						+ SymmetricKeyGen.encode64(this.encryptedMessage) + ":"
						+ SymmetricKeyGen.encode64(this.messageAuthCode);
			return eMsg;
		}
		
		public String decrypt(SecretKey key){
			String decryptedMsg = SymmetricKeyGen.decrypt(this.encryptedMessage, key, this.iv);
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