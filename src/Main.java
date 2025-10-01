import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Cybersecurity Toolkit ===");
            System.out.println("1) IP scan");
            System.out.println("2) Password generator");
            System.out.println("3) RSA keypair generator");
            System.out.println("4) AES encrypt/decrypt");
            System.out.println("5) Exit");
            System.out.print("Choose: ");

            String choice = in.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        System.out.print("Target host: ");
                        String host = in.nextLine().trim();
                        System.out.print("Start port: ");
                        int start = Integer.parseInt(in.nextLine().trim());
                        System.out.print("End port: ");
                        int end = Integer.parseInt(in.nextLine().trim());
                        IPScanner.scanHost(host, start, end);
                        break;
                    case "2":
                        System.out.print("Length: ");
                        int len = Integer.parseInt(in.nextLine().trim());
                        System.out.print("Include symbols? (y/n): ");
                        boolean symbols = in.nextLine().trim().equalsIgnoreCase("y");
                        System.out.println("Password: " + PasswordGenerator.generate(len, symbols));
                        break;
                    case "3":
                        System.out.print("RSA key size (e.g., 2048): ");
                        int bits = Integer.parseInt(in.nextLine().trim());
                        CryptoUtil.generateRSAKeyPair(bits);
                        break;
                    case "4":
                        System.out.print("Encrypt or Decrypt? (e/d): ");
                        String ed = in.nextLine().trim();
                        if (ed.equalsIgnoreCase("e")) {
                            System.out.print("Plaintext: ");
                            String pt = in.nextLine();
                            System.out.print("Password: ");
                            String pw = in.nextLine();
                            String cipher = CryptoUtil.encryptWithPassword(pt, pw);
                            System.out.println("Cipher (Base64): " + cipher);
                        } else {
                            System.out.print("Cipher (Base64): ");
                            String cipher = in.nextLine();
                            System.out.print("Password: ");
                            String pw = in.nextLine();
                            String plain = CryptoUtil.decryptWithPassword(cipher, pw);
                            System.out.println("Plaintext: " + plain);
                        }
                        break;
                    case "5":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        in.close();
    }
}

