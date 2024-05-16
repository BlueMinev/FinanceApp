import java.security.SecureRandom;

public class Salter {
    public static byte[] getRandomSalt(){
        return generateRandomSalt();
    }

    private static byte[] generateRandomSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
