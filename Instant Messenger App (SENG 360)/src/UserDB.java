import java.util.HashMap;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


/**
 * UserDB
 * HashMap only instantiates with user information once, always holds two users: admin and user.
 * See the enclosed technical manual for plaintext passwords.
 */
 class UserDB {

	private static HashMap<String, String> users = new HashMap<>();
	static {
		users.put("admin", "dm4yyPhP9hS3dokd+pEi3Q==DOU8Iz42ZPP74H7WrK2ijnWPNKoMmW4n3lQRNkvoh44=");
		users.put("user", "fs844Wb9ue6KjORQkt+EWA==dxGE7z9Su3cmGo5kOEp48hTKuXzBAI6azX3bjdA8WCA=");
	}

     /**
      * authenticate
      * @param userid The userid fetched via user input. To be checked against UserDB
      * @param pass The password fetched via user input. To be checked against UserDB
      * @return true if username/password exist, false otherwise
      */
    boolean authenticate(String userid, String pass){
		String DBHash = users.get(userid);
		if(DBHash == null){ return false; }

		byte[] freshHash = computeHashWithGivenPassword(pass, DBHash.substring(0, 24));
		String encodedFreshHash = SymKeyGen.encode64(freshHash);
        String encodedHashFromDB = DBHash.substring(24, DBHash.length());

        assert encodedFreshHash != null;
        return encodedFreshHash.equals(encodedHashFromDB);
	}

     /**
      * computeHashWithGivenPassword
      * Is a helper method for authenticate
      * @param pass The password fetched via user input. The password to be hashed.
      * @param salt A copy of the salt being stored in UserDB
      * @return a byte[] representation of the newly generated hash.
      */
     byte[] computeHashWithGivenPassword(String pass, String salt) {
        byte[] decodedSalt = SymKeyGen.decode64(salt);
        byte[] freshHash = null;

        KeySpec spec = new PBEKeySpec(pass.toCharArray(), decodedSalt, 65536, 256);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            freshHash = f.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) { ex.printStackTrace(); }

        return freshHash;
    }

    void addUser(String user, String pass) {
        users.put(user, pass);
    }

}