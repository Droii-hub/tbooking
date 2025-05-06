package com.walking.tbooking;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordProvider {
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
