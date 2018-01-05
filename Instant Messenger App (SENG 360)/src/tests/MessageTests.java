import org.junit.Assert;
import org.junit.Test;

public class MessageTests {

    @Test
    public void getShouldReturnSet() {
        String theMessage = "Bird";
        Message testMessage = new Message(theMessage);
        Assert.assertEquals(theMessage, testMessage.get());
    }

    @Test
    public void MessageShouldEqualIdenticalMessage (){
        Message m1 = new Message("Something");
        Message m2 = new Message("Something");

        Assert.assertTrue(m1.equals(m2));
    }

    @Test
    public void MessageShouldNotEqualDifferentMessage() {
        Message m1 = new Message("Something");
        Message m2 = new Message("Something else");

        Assert.assertFalse(m1.equals(m2));
    }

}
