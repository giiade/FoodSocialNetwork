package se.FSN.foodsocialnetwork;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import se.FSN.foodsocialnetwork.utils.AppController;
import se.FSN.foodsocialnetwork.utils.UsefulFunctions;


public class ShowSingleRecipe extends Activity {

    private SharedPreferences preferences;
    private TextView timeTxt, ingredientsTxt, toolsTxt, instructionsTxt, titleTxt;
    private TextView ingredientsBodyTxt, toolsBodyTxt, instructionsBodyTxt;
    private String timeStr;
    private Recipe recipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singlerecipe_layout);

        preferences = getSharedPreferences(UsefulFunctions.PREFERENCES_KEY, MODE_PRIVATE);

        timeTxt = (TextView) findViewById(R.id.singleRTime);
        titleTxt = (TextView) findViewById(R.id.singleRName);
        ingredientsTxt = (TextView) findViewById(R.id.singleRIngredientsBody);
        toolsTxt = (TextView) findViewById(R.id.singleRToolsBody);
        instructionsTxt = (TextView) findViewById(R.id.singleRInstructionsBody);

        instructionsBodyTxt = (TextView) findViewById(R.id.singleRInstructionsBody);
        ingredientsBodyTxt = (TextView) findViewById(R.id.singleRIngredientsBody);
        toolsBodyTxt = (TextView) findViewById(R.id.singleRToolsBody);


        recipe = new Recipe();

        Bundle extras = getIntent().getExtras();
        String id = extras.getString(UsefulFunctions.ID_KEY);

        RequestRecipe(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), id);

        toolsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolsBodyTxt.setVisibility(toolsBodyTxt.isShown()
                        ? View.GONE
                        : View.VISIBLE);
            }
        });

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

    private void RequestRecipe(String sessionID, String ID) {

        String url = UsefulFunctions.SINGLERECIPE_URL + ID + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID;

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


                        //Handle recipe as Title and instructions.
                        JSONObject jRecipe = (JSONObject) response.get(UsefulFunctions.RECIPEREQUEST_KEY);

                        recipe.setTitle(jRecipe.getString(UsefulFunctions.RECIPETITLE_KEY));
                        recipe.setID(jRecipe.getString(UsefulFunctions.RECIPEIDREQUEST_KEY));
                        recipe.setDescription(jRecipe.getString(UsefulFunctions.INSTRUCTION_KEY));
                        recipe.setTime(jRecipe.getInt(UsefulFunctions.TIME_KEY));
                        recipe.setCreator(jRecipe.getString(UsefulFunctions.CREATOR_KEY));
                        recipe.setIngredients(ingredients);
                        recipe.setTools(tools);

                        //Add data to the Layout

                        titleTxt.setText(recipe.getTitle());
                        ingredientsTxt.setText(printIngredients(recipe.getIngredients()));
                        toolsTxt.setText(printTools(recipe.getTools()));
                        timeStr = timeTxt.getText().toString();
                        timeTxt.setText(timeStr + recipe.getTime() + " minutes.");
                        instructionsTxt.setText(recipe.getDescription());


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
}
