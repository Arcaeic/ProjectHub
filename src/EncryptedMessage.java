import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

class EncryptedMessage extends Message{

    private static final long serialVersionUID = 1L;
		private byte[] iv = new byte[SymKeyGen.NUM_BYTES_IV];
		private byte[] message;
		private byte[] messageAuthCode;
	
	/**
	 * Allows toggling Confidentiality and Integrity through {@code boolean} parameters.
	 * @param message
     *      The message being sent
	 * @param key
     *      The key being used to encrypt the message
	 * @param macKey
     *      The message authentication code
	 * @param enableConfidential
     *      Inherited from paramArray[0]: Toggles encryption
	 * @param enableIntegrity
     *      Inherited from paramArray[1]: Toggles MAC
	 */
	EncryptedMessage(String message, SecretKey key, SecretKey macKey, boolean enableConfidential, boolean enableIntegrity){
		super();
		this.iv = SymKeyGen.generateInitVector().getIV();

		if (enableConfidential) { this.message = SymKeyGen.encryptMessage(message, key, this.iv); }
		else { this.message = message.getBytes(); }

		if (enableIntegrity) { this.messageAuthCode = generateMAC(this.message, macKey); }
		else { this.messageAuthCode = null; }
	}

    byte[] getMessage() {return this.message;}

    String decrypt(SecretKey key){ return SymKeyGen.decryptMessage(this.message, key, this.iv); }

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

    boolean verifyMAC(SecretKey macKey) {
	    //Generates a MAC and compares against the message's MAC
        boolean result = false;

        if (this.message != null) {
            byte[] newMac = generateMAC(this.message, macKey);
            String encodedNewMac = SymKeyGen.encode64(newMac);
            String encodedMac = SymKeyGen.encode64(this.messageAuthCode);
            if (encodedNewMac.equals(encodedMac)) { result = true; }
        } else { result = false; }

        return result;
    }
}