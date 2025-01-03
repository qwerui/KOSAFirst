package org.kosa.bookmanagement.util;

public class MemberState {
    private static String loggedInMemberId;
    private static boolean isAdmin;
    private static boolean isMember;

    public static String getLoggedInMemberId() {
        return loggedInMemberId;
    }

    public static void setLoggedInMemberId(String memberId) {
        loggedInMemberId = memberId;
    }

    public static void clear() {
        loggedInMemberId = null;
        isAdmin = false;
    }

    public static boolean isAdmin() {
        return isAdmin;
    }

    public static void setIsAdmin(boolean isAdmin) {
        MemberState.isAdmin = isAdmin;
    }

    public static boolean isMember() {
        return isMember;
    }

    public static void setIsMember(boolean isMember) {
        MemberState.isMember = isMember;
    }
}
