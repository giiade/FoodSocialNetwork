package se.FSN.foodsocialnetwork.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.FSN.foodsocialnetwork.Friend;
import se.FSN.foodsocialnetwork.R;

/**
 * Created by JulioLopez on 30/12/14.
 */
public class UsefulFunctions {

    public static final String LOGIN_URL = "http://83.254.221.239:9000/login";
    public static final String CRECIPE_URL = "http://83.254.221.239:9000/createRecipe";
    public static final String LOGOUT_URL = "http://83.254.221.239:9000/logout";
    public static final String SHOWALL_URL = "http://83.254.221.239:9000/showAll";
    public static final String SHOWMYRECIPES_URL = "http://83.254.221.239:9000/showMyRecipes";
    public static final String SINGLERECIPE_URL = "http://83.254.221.239:9000/recipe/";
    public static final String REQUESTIMAGE_URL = "http://83.254.221.239:9000/recipePicture/";
    public static final String FAVREQUEST_URL = "http://83.254.221.239:9000/addFavorite";
    public static final String UNFAVREQUEST_URL = "http://83.254.221.239:9000/deleteFavorite";
    public static final String SEARCHREQUEST_URL = "http://83.254.221.239:9000/searchRecipe";
    public static final String DELETERECIPE_URL = "http://83.254.221.239:9000/deleteRecipe";

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
    public static final String MYRECIPEIDS_KEY = "myrecipeid";


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
    public static final String IMG_KEY = "img";


    //Social Stuff
    public static final String SEARCHUSER_URL = "http://83.254.221.239:9000/searchUser";
    public static final String SHOWMYFRIENDS_URL = "http://83.254.221.239:9000/showMyFriends";
    public static final String ADDFRIEND_URL = "http://83.254.221.239:9000/addFriend";
    public static final String DELETEFRIEND_URL = "http://83.254.221.239:9000/deleteFriend";
    public static final String USERIMAGE_URL = "http://83.254.221.239:9000/userPicture";
    public static final String COMMENT_URL = "http://83.254.221.239:9000/rateAndComment";

    public static final String MYFRIENDS_KEY = "friendsid";

    public static final String FRIENDARRAY_KEY = "friends";


    public static final String COUNTRY_KEY = "country";
    public static final String USERNAME_KEY = "username";
    public static final String FOLLOWER_KEY = "follower";

    public static final String COMMENTS_KEY = "comments";
    public static final String COMMENT_KEY = "comment";
    public static final String RATE_KEY = "rate";
    public static final String USERCOMMENT_KEY = "user";
    public static final String SCORE_KEY = "score";
    public static final String COMMENTBODY_KEY = "commentText";

    public static final String MYACCOUNT_KEY = "myaccount";

    //Account Management
    public static final String CHANGEUSER_URL = "http://83.254.221.239:9000/editUsername";
    public static final String CHANGEPASS_URL = "http://83.254.221.239:9000/editEmail";
    public static final String CHANGECOUNTRY_URL = "http://83.254.221.239:9000/editCountry";



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

    public static String createImageURL(String sessionID, String recipeID) {
        StringBuilder result = new StringBuilder();
        result.append(REQUESTIMAGE_URL);
        result.append(recipeID + "?");
        result.append(SESSIONID_KEY);
        result.append("=" + sessionID);

        return result.toString();
    }

    public static String createUserImageURL(String sessionID, String userMail) {
        StringBuilder result = new StringBuilder();
        result.append(USERIMAGE_URL);
        result.append("?");
        result.append(SESSIONID_KEY);
        result.append("=" + sessionID);
        result.append("&" + MAIL_KEY);
        result.append("=" + userMail);

        return result.toString();
    }



    /**
     * Used to parse an ArrayList into a string separing the items with ",".
     * Used to save ArrayList in SharedPreferences as it is imposible to
     * save ArrayList objects in SharedPreferences
     *
     * @param list ArrayList Object containing list of String items
     * @return String with the form of "Item1,Item2
     */
    public static String convertToString(ArrayList<String> list) {
        StringBuilder result = new StringBuilder();
        for (String item : list) {
            result.append(item);
            result.append(",");
        }
        result.deleteCharAt(result.length() - 1);

        return result.toString();
    }

    /**
     * Used to parse a string separed by "," into an array.
     *
     * @param item each word of the string
     * @return ArrayList with all the items
     */
    public static ArrayList<String> convertToArray(String item) {
        if (item != null) {
            ArrayList<String> list = new ArrayList<String>(Arrays.asList(item.split(",")));
            return list;
        } else {
            return null;
        }
    }

    public static Friend HasItem(List<Friend> items, String item) {
        Friend result = new Friend();

        for (Friend friend : items) {
            if (friend.getEmail() == item)
                result = friend;
        }

        return result;

    }

    public static List<Friend> HasItem(List<Friend> items, ArrayList<String> item) {
        List<Friend> result = new ArrayList<>();
        for (int i = 0; i < item.size(); i++) {
            Friend f = HasItem(items, item.get(i));
            result.add(f);
        }
        return result;
    }

}
