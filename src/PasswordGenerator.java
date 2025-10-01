import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+[]{};:,.<>?/";

    public static String generate(int length, boolean useSymbols) {
        String pool = LETTERS + DIGITS + (useSymbols ? SYMBOLS : "");
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(pool.charAt(rnd.nextInt(pool.length())));
        }
        return sb.toString();
    }
}
