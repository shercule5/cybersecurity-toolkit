import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.Base64;

public class CryptoUtil {
    private static final int AES_KEY_BITS = 256;
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static final int PBKDF2_ITER = 65536;
    private static final int SALT_LEN = 16;

    public static String encryptWithPassword(String plaintext, String password) throws Exception {
        SecureRandom rnd = new SecureRandom();
        byte[] salt = new byte[SALT_LEN]; rnd.nextBytes(salt);
        SecretKey key = deriveKey(password.toCharArray(), salt);

        byte[] iv = new byte[GCM_IV_LENGTH]; rnd.nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF-8"));
        byte[] out = new byte[salt.length + iv.length + ciphertext.length];

        System.arraycopy(salt, 0, out, 0, salt.length);
        System.arraycopy(iv, 0, out, salt.length, iv.length);
        System.arraycopy(ciphertext, 0, out, salt.length + iv.length, ciphertext.length);

        return Base64.getEncoder().encodeToString(out);
    }

    public static String decryptWithPassword(String b64Input, String password) throws Exception {
        byte[] data = Base64.getDecoder().decode(b64Input);

        byte[] salt = new byte[SALT_LEN];
        System.arraycopy(data, 0, salt, 0, SALT_LEN);

        byte[] iv = new byte[GCM_IV_LENGTH];
        System.arraycopy(data, SALT_LEN, iv, 0, GCM_IV_LENGTH);

        byte[] cipherBytes = new byte[data.length - SALT_LEN - GCM_IV_LENGTH];
        System.arraycopy(data, SALT_LEN + GCM_IV_LENGTH, cipherBytes, 0, cipherBytes.length);

        SecretKey key = deriveKey(password.toCharArray(), salt);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] plain = cipher.doFinal(cipherBytes);
        return new String(plain, "UTF-8");
    }

    private static SecretKey deriveKey(char[] password, byte[] salt) throws Exception {
        KeySpec spec = new PBEKeySpec(password, salt, PBKDF2_ITER, AES_KEY_BITS);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = skf.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static void generateRSAKeyPair(int bits) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(bits);
        KeyPair kp = kpg.generateKeyPair();

        System.out.println("Public: " + Base64.getEncoder().encodeToString(kp.getPublic().getEncoded()));
        System.out.println("Private: " + Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded()));
    }
}
