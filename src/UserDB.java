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
		users.put("admin", "admin");
	}
	
	public UserDB(){
		
	}

	//TODO compare password hashes
	public boolean authenticate(String userid, String pass){
		
		boolean success = false;
		
		//get "userid's" salt from hashmap
		//h = hash(pass+salt)
		
		//compare h with hashmap data
		
		//if equal 
		//user is authed.
		
		
		//String password = users.get(userid);
		
		//if(password != null && pass.equals(password)){
			//success = true;
		//}
		
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

	}

}
