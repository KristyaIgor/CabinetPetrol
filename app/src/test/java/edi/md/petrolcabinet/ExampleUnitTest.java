package edi.md.petrolcabinet;

import org.junit.Test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getMD5_PIN_Code() {
        String message = "8108";
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.reset();
        m.update(message.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);

        // Now we need to zero pad it if you actually want the full 32 chars.
        while(hashtext.length() < 32 ){
            hashtext = "0" + hashtext;
        }
        assertEquals("Unexpected value for d.getFormType(): " + hashtext, "25b07af81d8c74341f00dc139652fdb0", hashtext);
    }

    @Test
    public void getBytes() {
        String message = "MIKOF";
        byte[] msgBytes = message.getBytes();
        assertArrayEquals(new byte[]{77, 73, 75, 79, 70}, msgBytes);
    }
}