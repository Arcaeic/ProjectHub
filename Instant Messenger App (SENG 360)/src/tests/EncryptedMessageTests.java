import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.SecretKey;

public class EncryptedMessageTests {

    private byte[] masterKey;
    private SecretKey[] sessionKeys;
    private EncryptedMessage m1, m2, m3;
    @Before
    public void main() {

        masterKey = SymKeyGen.generateMasterKey();
        sessionKeys = SymKeyGen.convertKeyBytes(SymKeyGen.splitMasterKey(masterKey));
        m1 = new EncryptedMessage("Msg.", sessionKeys[0], sessionKeys[1], true, true);
        m2 = new EncryptedMessage("Msg2.", sessionKeys[0], sessionKeys[1], false, true);
        m3 = new EncryptedMessage("Msg3.", sessionKeys[0], sessionKeys[1], true, false);
    }

    @Test
    public void verifyMACInvalidShouldReturnFalse(){
        SecretKey[] oldSessionKeys = sessionKeys;
        masterKey = SymKeyGen.generateMasterKey();
        sessionKeys = SymKeyGen.convertKeyBytes(SymKeyGen.splitMasterKey(masterKey));
        EncryptedMessage someMsg = new EncryptedMessage("Hello", sessionKeys[0], sessionKeys[1], true, true );

        Assert.assertFalse(someMsg.verifyMAC(oldSessionKeys[1]));

    }

    @Test
    public void verifyMacValidShouldReturnTrue() {
        Assert.assertTrue(m1.verifyMAC(sessionKeys[1]));
        Assert.assertTrue(m2.verifyMAC(sessionKeys[1]));

    }

    @Test
    public void getShouldReturnSet() {
        Assert.assertEquals(m1.get(), "Msg.");
        Assert.assertEquals(m2.get(), "Msg2.");
    }

    @Test
    public void decryptShouldReturnMsg() {
        Assert.assertEquals(m1.decrypt(sessionKeys[0]), "Msg.");
        Assert.assertEquals(m3.decrypt(sessionKeys[0]), "Msg3.");
    }

}
