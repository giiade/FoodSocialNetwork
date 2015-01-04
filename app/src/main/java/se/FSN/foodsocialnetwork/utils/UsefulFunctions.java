package se.FSN.foodsocialnetwork.utils;

/**
 * Created by JulioLopez on 30/12/14.
 */
public class UsefulFunctions {

    public static final String LOGIN_URL = "http://83.254.221.239:9000/login";
    public static final String CRECIPE_URL = " http://83.254.221.239:9000/createRecipe";

    public static final String PREFERENCES_KEY = "saves";
    public static final String ID_KEY = "id";
    public static final String MAIL_KEY = "email";
    public static final String PASS_KEY = "password";
    public static final String SUC_KEY = "success";
    public static final String SESSIONID_KEY = "sessionID";
    public static final String LOGED_KEY = "IsLoged";
    public static final String ERROR_KEY = "error";

    public static final String INTENTLIST_KEY = "list";


    public static final String TITLE_KEY = "title";
    public static final String INGREDIENTNAME_KEY = "name";
    public static final String INSTRUCTION_KEY = "instruction";
    public static final String TIME_KEY = "time";
    public static final String CATEGORY_KEY = "category";
    public static final String IMAGE_KEY = "image";
    public static final String INGREDIENT_KEY = "ingredients";
    public static final String OPTIONAL_KEY = "isOptional";
    public static final String AMOUNT_KEY = "amount";
    public static final String AMOUNTTYPE_KEY = "amountType";
    public static final String TOOLS_KEY = "tools";


    private static String SESSIONID;


    public String getSessionId() {
        return SESSIONID;
    }

    public void setSessionId(String sessionId) {
        SESSIONID = sessionId;


    }
}
