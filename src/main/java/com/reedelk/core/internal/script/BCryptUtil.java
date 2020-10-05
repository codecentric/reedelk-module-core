package com.reedelk.core.internal.script;

import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.annotation.TypeFunction;
import org.mindrot.jbcrypt.BCrypt;

@Type(global = true, description =
        "Provides a set of functions to hash a password, generate salt and check passwords using OpenBSD BCrypt scheme.")
public class BCryptUtil {

    // Protected
    BCryptUtil() {
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "hashpw(String password, String salt)",
            example = "BCryptUtil.hashpw('myPassword', 'aabbcc')",
            description = "Hash a password using the OpenBSD bcrypt scheme.",
            returnType = String.class)
    public String hashpw(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "checkpw(String plaintext, String hashed)",
            example = "BCryptUtil.checkpw('myPassword', 'aabbcc')",
            description = "Check that a plaintext password matches a previously hashed one.",
            returnType = boolean.class)
    public boolean checkpw(String plaintext, String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    }

    @TypeFunction(
            cursorOffset = 0,
            signature = "gensalt()",
            example = "BCryptUtil.gensalt()",
            description = "Generate a salt for use with the BCryptUtil.hashpw() method, " +
                    "selecting a reasonable default for the number of hashing rounds to apply.",
            returnType = String.class)
    public String gensalt() {
        return BCrypt.gensalt();
    }

    @TypeFunction(
            cursorOffset = 0,
            signature = "gensalt(int logRounds)",
            example = "BCryptUtil.gensalt(12)",
            description = "Generate a salt for use with the BCryptUtil.hashpw() method using the provided " +
                    "log2 of the number of rounds of hashing to apply.",
            returnType = String.class)
    public String gensalt(int logRounds) {
        return BCrypt.gensalt(logRounds);
    }
}
