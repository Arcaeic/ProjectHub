import java.util.HashMap;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class UserDB {

    //Init HashMap and store Server/Client login
	private static HashMap<String, String> users = new HashMap<String,String>();
	static {
		users.put("admin", "dm4yyPhP9hS3dokd+pEi3Q==DOU8Iz42ZPP74H7WrK2ijnWPNKoMmW4n3lQRNkvoh44=");
		users.put("user", "fs844Wb9ue6KjORQkt+EWA==dxGE7z9Su3cmGo5kOEp48hTKuXzBAI6azX3bjdA8WCA=");
	}
	
	UserDB(){
	    
    }

    boolean authenticate(String userid, String pass){
        //Look up user
		String saltHashFromDB = users.get(userid);
		if(saltHashFromDB == null){ return false; }

		//Get encodedSalt and encodedHashFromDB
		String encodedSalt = saltHashFromDB.substring(0,24);
		String encodedHashFromDB = saltHashFromDB.substring(24,saltHashFromDB.length());

		//Decode salt and generate the fresh hash
		byte[] decodedSalt = SymKeyGen.decode64(encodedSalt);
		byte[] freshHash = null;
		KeySpec spec = new PBEKeySpec(pass.toCharArray(), decodedSalt, 65536, 256);
		try {
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			freshHash = f.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException ex) { ex.printStackTrace(); }

		//Compare generated hash with stored hash
		String encodedFreshHash = SymKeyGen.encode64(freshHash);
		assert encodedFreshHash != null;
        return encodedFreshHash.equals(encodedHashFromDB);
	}
	
	public static void main(String[] args){
		/* Testing Suite

		//generate hash for initial user (will store in static init)
		byte[] salt = SymKeyGen.generateSalt(16);
		byte[] hash = null;
		KeySpec spec = new PBEKeySpec("adminpassword".toCharArray(), salt, 65536, 256);
		try {
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			hash = f.generateSecret(spec).getEncoded();
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException ex) { ex.printStackTrace(); }

		//print encoded hashes
		Base64.Encoder enc = Base64.getEncoder();
		System.out.printf("salt: %s%n", enc.encodeToString(salt));
		System.out.printf("hash: %s%n", enc.encodeToString(hash));
		
		//auth test here
		System.out.println("Test auth begins");
		UserDB database = new UserDB();
		String user = "admin";
		String password = "adminpassword";
		
		boolean auth = database.authenticate(user,password);
		
		if(auth){ System.out.println(user + " is authenicated!"); } 
		else{ System.out.println(user + " is NOT authenicated!"); }
		*/
		
		

	}

}
