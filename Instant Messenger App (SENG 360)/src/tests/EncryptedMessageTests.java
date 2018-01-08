import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.SecretKey;

public class EncryptedMessageTests {

    private byte[] masterKey;
    private SecretKey[] sessionKeys;
    private EncryptedMessage[] msgArr;

    @Before
    public void main() {

        masterKey = SymKeyGen.generateMasterKey();
        sessionKeys = SymKeyGen.convertKeyBytes(SymKeyGen.splitMasterKey(masterKey));
        EncryptedMessage m1 = new EncryptedMessage("Msg.", sessionKeys[0], sessionKeys[1], true, true);
        EncryptedMessage m2 = new EncryptedMessage("Msg2.", sessionKeys[0], sessionKeys[1], false, true);
        msgArr = new EncryptedMessage[] {m1, m2};
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
        for(EncryptedMessage msg: msgArr)
        Assert.assertTrue(msg.verifyMAC(sessionKeys[1]));
    }
}
