package se.FSN.foodsocialnetwork.utils;

/**
 * Created by JulioLopez on 30/12/14.
 */
public class UsefulFunctions {

    public static final String MAIL_KEY = "email";
    public static final String PASS_KEY = "password";
    public static final String SUC_KEY = "success";
    public static final String SESSIONID_KEY = "sessionID";

    private static String SESSIONID;


    public String getSessionId() {
        return SESSIONID;
    }

    public void setSessionId(String sessionId) {
        SESSIONID = sessionId;
    }
}
