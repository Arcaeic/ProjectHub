import org.junit.Assert;
import org.junit.Test;

public class UserDBTests {

    @Test
    public void authenticateValidLoginShouldReturnTrue() {
        UserDB DB = new UserDB();
        Assert.assertTrue(DB.authenticate("admin","adminpassword"));
    }

    @Test
    public void authenticateInvalidLoginShouldReturnFalse() {
        UserDB DB = new UserDB();
        Assert.assertFalse(DB.authenticate("Horatio", "1234"));
        Assert.assertFalse(DB.authenticate("aDmIn", "aDmInPaSsWoRd"));
    }
}
