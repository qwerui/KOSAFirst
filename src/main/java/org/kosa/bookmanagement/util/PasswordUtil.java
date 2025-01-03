/*
 * 작업자 : 이하린
 */

package org.kosa.bookmanagement.util;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

public class PasswordUtil {

    // 비밀번호 해싱 메서드
    public static String hashPassword(String plainPassword) {
        return Hashing.sha256()
                .hashString(plainPassword, StandardCharsets.UTF_8)
                .toString();
    }

    // 비밀번호 비교 검증 메서드
    public static boolean checkPassword(String plainPassword, String storedHash) {
        return hashPassword(plainPassword).equals(storedHash);
    }
}