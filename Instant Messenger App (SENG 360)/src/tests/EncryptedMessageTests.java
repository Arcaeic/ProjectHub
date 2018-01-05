import org.junit.Before;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptedMessageTests {

    private EncryptedMessage m1;

    @Before
    public void main() {

        SecretKey testKey = new SecretKeyS;
        SecretKey testMAC = new SecretKeySpec();
        m1 = new EncryptedMessage("Msg.", testKey, testMAC, false, false);


    }

    @Test
    public void verifyMACInvalidShouldReturnFalse(){

    }

    @Test
    public void verifyMacValidShouldReturnTrue() {

    }
}
