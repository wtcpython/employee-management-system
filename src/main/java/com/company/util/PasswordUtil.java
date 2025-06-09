package com.company.util;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.company.pojo.User;

public class PasswordUtil {
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    public static User createUser(String username, String plainPassword) throws Exception {
        byte[] saltBytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(saltBytes);
        String salt = Base64.getEncoder().encodeToString(saltBytes);

        String hash = hashPassword(plainPassword, saltBytes);
        return new User(username, salt, hash);
    }

    public static boolean verifyPassword(String inputPassword, User user) throws Exception {
        byte[] saltBytes = Base64.getDecoder().decode(user.getSalt());
        String computedHash = hashPassword(inputPassword, saltBytes);
        return slowEquals(Base64.getDecoder().decode(user.getHash()), Base64.getDecoder().decode(computedHash));
    }

    private static String hashPassword(String password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}
