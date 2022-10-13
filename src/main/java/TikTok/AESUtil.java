package TikTok;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {
    public static final String SECRET_KEY = "webapp1.0+202106";
    public static final String ALORIGHTM = "AES/CBC/PKCS5Padding";

    public static SecretKeySpec getSecretKey() throws NoSuchAlgorithmException {
        SecretKeySpec keyGenerator = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        return keyGenerator;
    }

    public static IvParameterSpec generateIV() throws UnsupportedEncodingException {
        return new IvParameterSpec(SECRET_KEY.getBytes("UTF-8"));
    }

    public static String decrypt(String text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance(ALORIGHTM);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), generateIV());
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(text));
        return new String(plainText);
    }

    public static String encrypt(String value) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(ALORIGHTM);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), generateIV());

        byte[] encrypted = cipher.doFinal((value+"&is_encryption=1").getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }
}
