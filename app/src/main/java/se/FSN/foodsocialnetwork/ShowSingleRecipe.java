package se.FSN.foodsocialnetwork;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import se.FSN.foodsocialnetwork.utils.AppController;
import se.FSN.foodsocialnetwork.utils.UsefulFunctions;


public class ShowSingleRecipe extends Activity {

    private SharedPreferences preferences;
    private TextView timeTxt, titleTxt;
    private TextView ingredientsBodyTxt, toolsBodyTxt, instructionsBodyTxt, commentBodyTxt;
    private LinearLayout commentCont;
    private RatingBar commentRating;
    private NetworkImageView imageView;
    private ImageButton imgButton;
    private String timeStr, id;
    private Recipe recipe;

    private ArrayList<String> myFavIDS = new ArrayList<>();
    private ArrayList<String> myRecipeIDS = new ArrayList<>();

    MenuItem itemFavo, itemMyRec, itemMyAcc, itemMyFri, itemShowAll;

    RatingBar ratingBarLauncher;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singlerecipe_layout);

        preferences = getSharedPreferences(UsefulFunctions.PREFERENCES_KEY, MODE_PRIVATE);

        timeTxt = (TextView) findViewById(R.id.singleRTime);
        titleTxt = (TextView) findViewById(R.id.singleRName);
        imageView = (NetworkImageView) findViewById(R.id.singleRImage);

        instructionsBodyTxt = (TextView) findViewById(R.id.singleRInstructionsBody);
        ingredientsBodyTxt = (TextView) findViewById(R.id.singleRIngredientsBody);
        toolsBodyTxt = (TextView) findViewById(R.id.singleRToolsBody);
        commentBodyTxt = (TextView) findViewById(R.id.singleRCommentsBody);

        commentCont = (LinearLayout) findViewById(R.id.commentsContainer);

        commentRating = (RatingBar) findViewById(R.id.singleRRating);


        imgButton = (ImageButton) findViewById(R.id.RecipeFavoriteButton);

        ratingBarLauncher = (RatingBar) findViewById(R.id.ratingLauncher);


        recipe = new Recipe();
        Resources res = getResources();


        myFavIDS = UsefulFunctions.convertToArray(preferences.getString(UsefulFunctions.FAVIDS_KEY, null));
        myRecipeIDS = UsefulFunctions.convertToArray(preferences.getString(UsefulFunctions.MYRECIPEIDS_KEY, null));


        Bundle extras = getIntent().getExtras();
        id = extras.getString(UsefulFunctions.ID_KEY);


        if (myFavIDS.contains(id)) {
            imgButton.setImageDrawable(res.getDrawable(R.drawable.star_on));
        } else {
            imgButton.setImageDrawable(res.getDrawable(R.drawable.star_off));
        }


        RequestRecipe(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), id);


        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActionBar actionBar = getActionBar();
                actionBar.setTitle(recipe.getTitle());
            }
        }, 500);


        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resources res = getResources();

                if (myFavIDS != null) {
                    if (myFavIDS.contains(id)) {
                        unfavRequest(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), id);
                        imgButton.setImageDrawable(res.getDrawable(R.drawable.star_off));
                    } else {
                        favRequest(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), id);
                        imgButton.setImageDrawable(res.getDrawable(R.drawable.star_on));
                    }
                } else {

                    myFavIDS = new ArrayList<String>();
                    favRequest(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), id);
                    imgButton.setImageDrawable(res.getDrawable(R.drawable.star_on));
                }

            }
        });

        ratingBarLauncher.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Intent i = new Intent(getApplicationContext(), Comments.class);
                i.putExtra(UsefulFunctions.ID_KEY, id);
                i.putExtra(UsefulFunctions.RATE_KEY, rating);
                startActivity(i);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        RequestRecipe(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), id);
    }

    private void favRequest(String sessionID, String id) {

        String url = UsefulFunctions.FAVREQUEST_URL + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID
                + "&" + UsefulFunctions.ID_KEY + "=" + id;

        Log.d("URL_FAV", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        Toast.makeText(getApplicationContext(),
                                recipe.getTitle() + " added to Favorites", Toast.LENGTH_SHORT).show();
                        myFavIDS.add(recipe.getID());
                        /*String id = recipe.getID();
                        myFavIDS.add(id);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(UsefulFunctions.FAVIDS_KEY, UsefulFunctions.convertToString(myFavIDS));
                        editor.commit();*/
                    } /*else {
                        String id = recipe.getID();
                        myFavIDS.add(id);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(UsefulFunctions.FAVIDS_KEY, UsefulFunctions.convertToString(myFavIDS));
                        editor.commit();
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        );


        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    private void unfavRequest(String sessionID, String id) {

        String url = UsefulFunctions.UNFAVREQUEST_URL + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID
                + "&" + UsefulFunctions.ID_KEY + "=" + id;
        Log.d("URL_UNFAV", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        Toast.makeText(getApplicationContext(),
                                recipe.getTitle() + " deleted from Favorites", Toast.LENGTH_SHORT).show();
                        myFavIDS.remove(recipe.getID());
                        /*SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(UsefulFunctions.FAVIDS_KEY, UsefulFunctions.convertToString(myFavIDS));
                        editor.commit();*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        );


        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


    public void onInstructionClick(View v) {
        if (instructionsBodyTxt.isShown()) {
            UsefulFunctions.slide_up(this, instructionsBodyTxt);
            instructionsBodyTxt.setVisibility(View.GONE);
        } else {
            UsefulFunctions.slide_down(this, instructionsBodyTxt);
            instructionsBodyTxt.setVisibility(View.VISIBLE);
        }
    }

    public void onToolClick(View v) {
        if (toolsBodyTxt.isShown()) {
            UsefulFunctions.slide_up(this, toolsBodyTxt);
            toolsBodyTxt.setVisibility(View.GONE);
        } else {
            UsefulFunctions.slide_down(this, toolsBodyTxt);
            toolsBodyTxt.setVisibility(View.VISIBLE);
        }
    }

    public void onIngredientClick(View v) {
        if (ingredientsBodyTxt.isShown()) {
            UsefulFunctions.slide_up(this, ingredientsBodyTxt);
            ingredientsBodyTxt.setVisibility(View.GONE);
        } else {
            UsefulFunctions.slide_down(this, ingredientsBodyTxt);
            ingredientsBodyTxt.setVisibility(View.VISIBLE);
        }
    }

    public void onCommentsClick(View v) {
        if (commentCont.isShown()) {
            UsefulFunctions.slide_up(this, commentCont);
            commentCont.setVisibility(View.GONE);
        } else {
            UsefulFunctions.slide_down(this, commentCont);
            commentCont.setVisibility(View.VISIBLE);
        }
    }

    private void RequestRecipe(final String sessionID, String ID) {

        final String url = UsefulFunctions.SINGLERECIPE_URL + ID + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("URL_SHOWSINGLE", response.toString());

                try {

                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {


                        //Handle the ingredients
                        JSONArray jsonIngredients = null;
                        jsonIngredients = response.getJSONArray(UsefulFunctions.INGREDIENTARRAY_KEY);
                        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

                        for (int i = 0; i < jsonIngredients.length(); i++) {
                            //We create a Recipe Object
                            Ingredient ingredient = new Ingredient();
                            //We create a JsonObject that will contain the data of this ingredient
                            JSONObject jIngredient = (JSONObject) jsonIngredients.get(i);
                            //We pass the data to the ingredient object
                            ingredient.setTitle(jIngredient.getString(UsefulFunctions.INGREDIENTNAME_KEY));
                            ingredient.setQuantity(jIngredient.getString(UsefulFunctions.AMOUNT_KEY));
                            ingredient.setInputType(jIngredient.getString(UsefulFunctions.AMOUNTTYPE_KEY));
                            ingredient.setOptional((jIngredient.getInt((UsefulFunctions.OPTIONAL_KEY)) == 0));
                            //Add the ingredient object to the array
                            ingredients.add(ingredient);
                        }

                        //Handle the tools

                        JSONArray jsonTools = null;
                        jsonTools = response.getJSONArray(UsefulFunctions.TOOLS_KEY);
                        ArrayList<String> tools = new ArrayList<String>();

                        for (int i = 0; i < jsonTools.length(); i++) {
                            //String object for tool
                            String tool = new String();
                            tool = jsonTools.getString(i);

                            //Add the tool to the array
                            tools.add(tool);

                        }

                        //Handle Comments

                        JSONArray jsonComments = null;
                        jsonComments = response.getJSONArray(UsefulFunctions.COMMENTS_KEY);
                        final String NEWLINE = System.getProperty("line.separator");

                        ArrayList<String> comments = new ArrayList<>();
                        double score = 0;

                        if (jsonComments.length() > 0) {
                            for (int i = 0; i < jsonComments.length(); i++) {

                                StringBuilder comment = new StringBuilder();

                                JSONObject jsonComment = (JSONObject) jsonComments.get(i);

                                comment.append("Comment " + (i + 1) + "->" + NEWLINE);
                                comment.append("User: " + jsonComment.get(UsefulFunctions.USERCOMMENT_KEY) + "." + NEWLINE);
                                comment.append(jsonComment.get(UsefulFunctions.COMMENTBODY_KEY) + NEWLINE);

                                comments.add(comment.toString());

                                score += jsonComment.getDouble(UsefulFunctions.SCORE_KEY);


                            }

                            score = score / jsonComments.length();
                        } else {
                            comments.add("NO COMMENTS");
                        }

                        //Handle recipe as Title and instructions.
                        JSONObject jRecipe = (JSONObject) response.get(UsefulFunctions.RECIPEREQUEST_KEY);


                        recipe.setTitle(jRecipe.getString(UsefulFunctions.RECIPETITLE_KEY));
                        recipe.setID(jRecipe.getString(UsefulFunctions.RECIPEIDREQUEST_KEY));
                        recipe.setDescription(jRecipe.getString(UsefulFunctions.INSTRUCTION_KEY));
                        recipe.setTime(jRecipe.getInt(UsefulFunctions.TIME_KEY));
                        recipe.setCreator(jRecipe.getString(UsefulFunctions.CREATOR_KEY));
                        recipe.setIngredients(ingredients);
                        recipe.setTools(tools);
                        recipe.setImageUrl(UsefulFunctions.createImageURL(sessionID, recipe.getID()));

                        //Add data to the Layout

                        titleTxt.setText(recipe.getTitle());
                        ingredientsBodyTxt.setText(printIngredients(recipe.getIngredients()));
                        toolsBodyTxt.setText(printTools(recipe.getTools()));
                        timeStr = "Time: ";
                        timeTxt.setText(timeStr + recipe.getTime() + " minutes.");
                        instructionsBodyTxt.setText(recipe.getDescription());

                        commentRating.setRating((float) score);
                        commentBodyTxt.setText(printComments(comments));

                        String img = jRecipe.getString(UsefulFunctions.IMG_KEY);

                        if (imageLoader == null)
                            imageLoader = AppController.getInstance().getImageLoader();
                        String url = "";
                        if (img.length() > 0) {
                            url = recipe.getImageUrl();
                            Log.i("URLIMAGE", url);
                        } else {
                            url = "http://img4.wikia.nocookie.net/__cb20130819001030/lego/images/a/ac/No-Image-Basic.png";
                        }

                        imageView.setImageUrl(url, imageLoader);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        );


        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


    private String printIngredients(ArrayList<Ingredient> ingredients) {
        StringBuilder result = new StringBuilder();
        final String NEWLINE = System.getProperty("line.separator");

        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = new Ingredient();
            ingredient = ingredients.get(i);
            result.append(ingredient.getTitle() + ": ");
            result.append(ingredient.getQuantity() + " ");
            result.append(ingredient.getInputType() + ", ");

            if (ingredient.isOptional()) {
                result.append("Optional.");
            } else {
                result.append("Mandatory.");
            }
            result.append(NEWLINE);
        }

        return result.toString();
    }

    private String printTools(ArrayList<String> tools) {
        StringBuilder result = new StringBuilder();
        final String NEWLINE = System.getProperty("line.separator");

        for (int i = 0; i < tools.size(); i++) {
            String tool = tools.get(i);
            result.append(tool + ".");
            result.append(NEWLINE);
        }

        return result.toString();
    }

    private String printComments(ArrayList<String> comments) {
        StringBuilder result = new StringBuilder();
        final String NEWLINE = System.getProperty("line.separator");

        for (int i = 0; i < comments.size(); i++) {
            String comment = comments.get(i);
            result.append(comment);
            result.append(NEWLINE);
        }

        return result.toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if (myRecipeIDS.contains(id)) {
            getMenuInflater().inflate(R.menu.main, menu);
        } else {
            getMenuInflater().inflate(R.menu.main_menu, menu);
        }

        itemFavo = menu.findItem(R.id.mnuFavourites);
        itemMyAcc = menu.findItem(R.id.mnuMyAccount);
        itemMyFri = menu.findItem(R.id.mnuMyFriends);
        itemShowAll = menu.findItem(R.id.mnuShowAll);
        itemMyRec = menu.findItem(R.id.mnuMyRecipes);

        itemShowAll.setVisible(true);
        itemFavo.setVisible(false);
        itemMyRec.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch (item.getItemId()) {
            case R.id.mnuLogOut:
                Log.i("MENU" + Main.class.toString(), "LOGOUT");
                requestLogout(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"));
                return true;
            case R.id.mnuMyAccount:
                Log.i("MENU" + Main.class.toString(), "MY ACCOUNT");
                //We go to our friends Class
                Intent My = new Intent(getApplicationContext(), ShowUser.class);
                My.putExtra(UsefulFunctions.MAIL_KEY, preferences.getString(UsefulFunctions.MAIL_KEY, "Null"));
                My.putExtra(UsefulFunctions.MYACCOUNT_KEY, true);
                startActivity(My);
                return true;
            case R.id.mnuShowAll:
                Log.i("MENU" + Main.class.toString(), "Show All");
                item.setVisible(false);
                Intent all = new Intent(getApplicationContext(), Main.class);
                startActivity(all);
                return true;
            case R.id.mnuMyFriends:
                Log.i("MENU" + Main.class.toString(), "My Friends");
                Intent i = new Intent(getApplicationContext(), MyFriends.class);
                startActivity(i);
                return true;

            case R.id.mnuDelete:
                Log.i("MENU" + Main.class.toString(), "Delete");

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Recipe")
                        .setMessage("Delete " + recipe.getTitle() + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteRequest(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), recipe.getID());
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * We make a request for log out of the server.
     *
     * @param sessionID The actual session of the user.
     */
    private void requestLogout(String sessionID) {


        String url = UsefulFunctions.LOGOUT_URL + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID;
        Log.d("URL_LOGOUT", url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("URL_LOGOUT", response.toString());
                SharedPreferences.Editor editor = preferences.edit();
                try {

                 /*
                TODO: Do something with the result.
                 */

                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        editor.putBoolean(UsefulFunctions.LOGED_KEY, false);
                        editor.commit();
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        startActivity(i);
                        finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        );


        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    private void deleteRequest(String sessionID, String id) {


        String url = UsefulFunctions.DELETERECIPE_URL + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID
                + "&" + UsefulFunctions.ID_KEY + "=" + id;

        Log.d("URL_FAV", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        Toast.makeText(getApplicationContext(),
                                recipe.getTitle() + " was deleted.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), Main.class);
                        startActivity(i);
                        finish();
                        /*String id = recipe.getID();
                        myFavIDS.add(id);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(UsefulFunctions.FAVIDS_KEY, UsefulFunctions.convertToString(myFavIDS));
                        editor.commit();*/
                    } else {
                        Toast.makeText(getApplicationContext(), response.getString(UsefulFunctions.ERROR_KEY), Toast.LENGTH_SHORT).show();
                    }/*else {
                        String id = recipe.getID();
                        myFavIDS.add(id);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(UsefulFunctions.FAVIDS_KEY, UsefulFunctions.convertToString(myFavIDS));
                        editor.commit();
                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        );


        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


}

