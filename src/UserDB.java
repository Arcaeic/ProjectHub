import java.util.HashMap;
import java.util.Map;


public class UserDB {
	
	private static HashMap<String, String> users = new HashMap<String,String>();
	
	static {
		users.put("admin", "admin");
	}
	public UserDB(){
		
	}
	
	public UserDB(String userid, String pass){
		users.put(userid, pass);
	}
	
	//TODO compare password hashes
	public boolean authenticate(String userid, String pass){
		
		boolean success = false;
		String password = users.get(userid);
		
		if(password != null && pass.equals(password)){
			success = true;
		}
		
		return success;
	}
	
	
	
}
