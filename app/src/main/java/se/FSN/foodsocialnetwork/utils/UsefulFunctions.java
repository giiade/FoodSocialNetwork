package se.FSN.foodsocialnetwork.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import se.FSN.foodsocialnetwork.R;

/**
 * Created by JulioLopez on 30/12/14.
 */
public class UsefulFunctions {

    public static final String LOGIN_URL = "http://83.254.221.239:9000/login";
    public static final String CRECIPE_URL = "http://83.254.221.239:9000/createRecipe";
    public static final String LOGOUT_URL = "http://83.254.221.239:9000/logout";
    public static final String SHOWALL_URL = "http://83.254.221.239:9000/showAll";
    public static final String SINGLERECIPE_URL = "http://83.254.221.239:9000/recipe/";
    public static final String REQUESTIMAGE_URL = "http://83.254.221.239:9000/recipePicture/";
    public static final String FAVREQUEST_URL = "http://83.254.221.239:9000/addFavorite";
    public static final String UNFAVREQUEST_URL = "http://83.254.221.239:9000/deleteFavorite";
    public static final String SEARCHREQUEST_URL = "http://83.254.221.239:9000/searchRecipe";

    public static final String RECIPEARRAY_KEY = "recipes";
    public static final String RECIPETITLE_KEY = "recipeTitle";
    public static final String CREATOR_KEY = "creator";
    public static final String INGREDIENTARRAY_KEY = "ingridients";
    public static final String RECIPEREQUEST_KEY = "recipe";
    public static final String RECIPEIDREQUEST_KEY = "id";
    public static final String SEARCHTAG_KEY = "searchString";
    public static final String FAVORITETAG = "favorites";

    public static final String PREFERENCES_KEY = "saves";
    public static final String ID_KEY = "recipeID";
    public static final String MAIL_KEY = "email";
    public static final String PASS_KEY = "password";
    public static final String SUC_KEY = "success";
    public static final String SESSIONID_KEY = "sessionID";
    public static final String LOGED_KEY = "IsLoged";
    public static final String ERROR_KEY = "error";

    public static final String INTENTLIST_KEY = "list";
    public static final String FAVIDS_KEY = "favoriteid";


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


    public static void slide_down(Context context, View v) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.slidedown);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void slide_up(Context context, View v) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.slideup);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }
}
