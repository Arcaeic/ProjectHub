import org.junit.Assert;
import org.junit.Test;

public class UserDBTests {

    @Test
    public void authenticateValidLoginShouldReturnTrue() {
        UserDB DB = new UserDB();
        Assert.assertTrue(DB.authenticate("admin","adminpassword"));

        addUserToUserDB("test", "something", DB);
        Assert.assertTrue(DB.authenticate("test", "something"));
    }

    @Test
    public void authenticateInvalidLoginShouldReturnFalse() {
        UserDB DB = new UserDB();
        Assert.assertFalse(DB.authenticate("Horatio", "1234"));
        Assert.assertFalse(DB.authenticate("aDmIn", "aDmInPaSsWoRd"));
    }

    private void addUserToUserDB(String user, String pass, UserDB DB) {
        String salt = "fs844Wb9ue6KjORQkt+EWA==";
        byte[] byteHash = DB.computeHashWithGivenPassword(pass, salt);
        pass = SymKeyGen.encode64(byteHash);
        DB.addUser(user, salt + pass);
    }
}
