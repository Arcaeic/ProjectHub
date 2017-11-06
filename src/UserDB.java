import java.util.HashMap;
import java.util.Map;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class UserDB {
	
	private static HashMap<String, String> users = new HashMap<String,String>();
	//key:username data:[hash(passwordsalt)salt]
	
	static {
		users.put("admin", "dm4yyPhP9hS3dokd+pEi3Q==DOU8Iz42ZPP74H7WrK2ijnWPNKoMmW4n3lQRNkvoh44=");
	}
	
	public UserDB(){
		
	}

	//TODO compare password hashes
	public boolean authenticate(String userid, String pass){
		
		boolean success = false;
		
		//get "userid's" salt from hashmap
		String hashSaltFromDB = users.get(userid);
		
		//return if user not found
		if(hashSaltFromDB == null){
			return success;
		}
		
		String encodedSalt = hashSaltFromDB.substring(0,24);
		String encodedHash = hashSaltFromDB.substring(24,hashSaltFromDB.length());
		System.out.println("Database: [" + userid + "]-[" + encodedSalt + "]-[" + encodedHash + "]");

		
		byte[] salt = SymKeyGen.decode64(encodedSalt);
		byte[] hash = SymKeyGen.decode64(encodedHash);
	
		//compute hash from user supplied password
		byte[] userHash = null;
		KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, 65536, 256);
		try {
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			userHash = f.generateSecret(spec).getEncoded();
		}
		catch (NoSuchAlgorithmException ex) {
		}
		catch (InvalidKeySpecException ex) {
		}
		
		String encodedUserHash = SymKeyGen.encode64(userHash);
		//print encoded hashes
		System.out.println("User supplied: [" + userid + "]-[" + SymKeyGen.encode64(salt) + "]-[" + encodedUserHash + "]");
		
		//compare hashes
		if(encodedUserHash.equals(encodedHash)){
			success = true;
		}
		
		return success;
	}
	
	public static void main(String[] args){
		//generate hash for initial user (will store in static init)
		byte[] salt = SymKeyGen.generateSalt(16);
		byte[] hash = null;
		KeySpec spec = new PBEKeySpec("adminpassword".toCharArray(), salt, 65536, 256);
		try {
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			hash = f.generateSecret(spec).getEncoded();
		}
		catch (NoSuchAlgorithmException ex) {
		}
		catch (InvalidKeySpecException ex) {
		}
		
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
		
		if(auth){
			System.out.println(user + " is authenicated :)");
		}else{
			System.out.println(user + " is NOT authenicated! :(");

		}

		
		

	}

}
