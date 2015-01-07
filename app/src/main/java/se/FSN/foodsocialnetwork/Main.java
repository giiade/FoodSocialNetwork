package se.FSN.foodsocialnetwork;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import se.FSN.foodsocialnetwork.utils.AppController;
import se.FSN.foodsocialnetwork.utils.UsefulFunctions;


public class Main extends Activity {

    private SharedPreferences preferences;
    private ListAdapter adapter;
    private ListView recipeList;
    private List<Recipe> recipes = new ArrayList<Recipe>();
    private EditText searchString;


    private ArrayList<String> myFavIDS = new ArrayList<>();
    private ArrayList<String> myRecipeIDS = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getActionBar();
        actionBar.setTitle("Recipes");


        preferences = getSharedPreferences(UsefulFunctions.PREFERENCES_KEY, MODE_PRIVATE);

        searchString = (EditText) findViewById(R.id.searchText);

        //LIST
        recipeList = (ListView) findViewById(R.id.recipeList);
        adapter = new ListAdapter(this, recipes);
        recipeList.setAdapter(adapter);


        requestAllRecipes(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"));


        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), ShowSingleRecipe.class);
                i.putExtra(UsefulFunctions.ID_KEY, recipes.get(position).getID());

                startActivity(i);
            }
        });



        Button newrecipeBtn = (Button) findViewById(R.id.newrecipeBtn);
        newrecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PostRecipe.class);
                startActivity(intent);
            }
        });

        ImageButton searchBTN = (ImageButton) findViewById(R.id.searchBtn);
        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagToSearch = "";
                if (searchString.length() > 0) {
                    try {
                        tagToSearch = URLEncoder.encode(searchString.getText().toString(), "UTF8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                } else {
                    tagToSearch = "";
                }
                requestSearch(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), tagToSearch);
            }


        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestSearch(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), "");
        requestFavIds(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"));
        requestMyRecipesIds(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //TODO: Finish the menu and actions of it.

        switch (item.getItemId()) {
            case R.id.mnuLogOut:
                Log.i("MENU" + Main.class.toString(), "LOGOUT");
                requestLogout(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"));
                return true;
            case R.id.mnuMyAccount:
                Log.i("MENU" + Main.class.toString(), "MY ACCOUNT");
                return true;
            case R.id.mnuFavourites:
                Log.i("MENU" + Main.class.toString(), "Favorites");
                return true;
            case R.id.mnuMyRecipes:
                Log.i("MENU" + Main.class.toString(), "My Recipes");
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

    private void requestAllRecipes(final String sessionID) {
        String url = UsefulFunctions.SHOWALL_URL + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID;
        Log.d("URL_SHOWALL", url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("URL_SHOWALL", response.toString());

                try {


                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        JSONArray jsonRecipes = null;
                        jsonRecipes = response.getJSONArray(UsefulFunctions.RECIPEARRAY_KEY);

                        for (int i = 0; i < jsonRecipes.length(); i++) {
                            //We create a Recipe Object
                            Recipe recipe = new Recipe();
                            //We create a JsonObject that will contain the data of this recipe
                            JSONObject jRecipe = (JSONObject) jsonRecipes.get(i);
                            //We pass the data to the recipe object
                            recipe.setID(jRecipe.getString(UsefulFunctions.ID_KEY));
                            recipe.setTitle(jRecipe.getString(UsefulFunctions.RECIPETITLE_KEY));
                            recipe.setCreator(jRecipe.getString(UsefulFunctions.CREATOR_KEY));
                            recipe.setImageUrl(UsefulFunctions.createImageURL(sessionID, recipe.getID()));
                            //Add the recipe object to the array
                            recipes.add(recipe);
                            adapter.notifyDataSetChanged();
                        }

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


    private void requestSearch(final String sessionID, String tag) {

        String url = UsefulFunctions.SEARCHREQUEST_URL + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID
                + "&" + UsefulFunctions.SEARCHTAG_KEY + "=" + tag
                + "&" + UsefulFunctions.FAVORITETAG + "=" + false;
        Log.d("URL_SEARCH", url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("URL_SEARCH", response.toString());

                try {


                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        JSONArray jsonRecipes = null;
                        jsonRecipes = response.getJSONArray(UsefulFunctions.RECIPEARRAY_KEY);

                        List<Recipe> auxList = new ArrayList<Recipe>();

                        for (int i = 0; i < jsonRecipes.length(); i++) {
                            //We create a Recipe Object
                            Recipe recipe = new Recipe();
                            //We create a JsonObject that will contain the data of this recipe
                            JSONObject jRecipe = (JSONObject) jsonRecipes.get(i);
                            //We pass the data to the recipe object
                            recipe.setID(jRecipe.getString(UsefulFunctions.ID_KEY));
                            recipe.setTitle(jRecipe.getString(UsefulFunctions.RECIPETITLE_KEY));
                            recipe.setImageUrl(UsefulFunctions.createImageURL(sessionID, recipe.getID()));
                            //Add the recipe object to the array
                            auxList.add(recipe);
                            adapter.swapItems(auxList);
                        }

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

    private void requestFavIds(final String sessionID) {

        String url = UsefulFunctions.SEARCHREQUEST_URL + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID
                + "&" + UsefulFunctions.SEARCHTAG_KEY + "=" + ""
                + "&" + UsefulFunctions.FAVORITETAG + "=" + true;
        Log.d("URL_SEARCH", url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("URL_SEARCH", response.toString());

                try {


                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        JSONArray jsonRecipes = null;
                        jsonRecipes = response.getJSONArray(UsefulFunctions.RECIPEARRAY_KEY);
                        myFavIDS.clear();
                        for (int i = 0; i < jsonRecipes.length(); i++) {
                            //We create a Recipe Object
                            Recipe recipe = new Recipe();
                            //We create a JsonObject that will contain the data of this recipe
                            JSONObject jRecipe = (JSONObject) jsonRecipes.get(i);
                            //We pass the data to the recipe object
                            recipe.setID(jRecipe.getString(UsefulFunctions.ID_KEY));
                            //Add the recipe object to the array
                            myFavIDS.add(recipe.getID());
                        }
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(UsefulFunctions.FAVIDS_KEY, UsefulFunctions.convertToString(myFavIDS));
                        editor.commit();
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

    private void requestMyRecipesIds(final String sessionID) {

        String url = UsefulFunctions.SHOWMYRECIPES_URL + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID;
        Log.d("URL_SEARCH", url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("URL_SEARCH", response.toString());

                try {


                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        JSONArray jsonRecipes = null;
                        jsonRecipes = response.getJSONArray(UsefulFunctions.RECIPEARRAY_KEY);
                        myRecipeIDS.clear();
                        for (int i = 0; i < jsonRecipes.length(); i++) {
                            //We create a Recipe Object
                            Recipe recipe = new Recipe();
                            //We create a JsonObject that will contain the data of this recipe
                            JSONObject jRecipe = (JSONObject) jsonRecipes.get(i);
                            //We pass the data to the recipe object
                            recipe.setID(jRecipe.getString(UsefulFunctions.ID_KEY));
                            //Add the recipe object to the array
                            myRecipeIDS.add(recipe.getID());
                        }
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(UsefulFunctions.MYRECIPEIDS_KEY, UsefulFunctions.convertToString(myRecipeIDS));
                        editor.commit();
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

    private void requestMyRecipes(final String sessionID) {
        String url = UsefulFunctions.SHOWMYRECIPES_URL + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID;
        Log.d("URL_MYRECIPES", url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("URL_MYRECIPES", response.toString());

                try {


                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        JSONArray jsonRecipes = null;
                        jsonRecipes = response.getJSONArray(UsefulFunctions.RECIPEARRAY_KEY);

                        List<Recipe> auxList = new ArrayList<Recipe>();

                        for (int i = 0; i < jsonRecipes.length(); i++) {
                            //We create a Recipe Object
                            Recipe recipe = new Recipe();
                            //We create a JsonObject that will contain the data of this recipe
                            JSONObject jRecipe = (JSONObject) jsonRecipes.get(i);
                            //We pass the data to the recipe object
                            recipe.setID(jRecipe.getString(UsefulFunctions.ID_KEY));
                            recipe.setTitle(jRecipe.getString(UsefulFunctions.RECIPETITLE_KEY));
                            recipe.setCreator(jRecipe.getString(UsefulFunctions.CREATOR_KEY));
                            recipe.setImageUrl(UsefulFunctions.createImageURL(sessionID, recipe.getID()));
                            //Add the recipe object to the array
                            auxList.add(recipe);
                            adapter.swapItems(auxList);
                        }

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
}
