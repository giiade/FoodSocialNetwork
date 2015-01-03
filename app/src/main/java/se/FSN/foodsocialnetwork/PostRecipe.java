package se.FSN.foodsocialnetwork;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import se.FSN.foodsocialnetwork.utils.UsefulFunctions;


public class PostRecipe extends Activity {

    /*
    TODO:
    Pasar los ingredientes a la actividad
    Pasar las herramientas a la actividad
    Gestionar el return de la actividad al crear receta.
     */

    private String recipeName, recipeInstructions, Time;
    private int recipeTime;
    TextView NameTxt, InstructionsTxt, TimeTxt;
    private ArrayList<HashMap<String, String>> ingredients = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> tools = new ArrayList<String>();
    private HashMap<String, String> Values = new HashMap<String, String>();


    private String urlJsonObject = " http://83.254.221.239:9000/createRecepie";
    private boolean post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_recipe);


        NameTxt = (TextView) findViewById(R.id.RecipeNameTxt);
        InstructionsTxt = (TextView) findViewById(R.id.RecipeIngredientsTxt);
        TimeTxt = (TextView) findViewById(R.id.editRecipeTimeTxt);

        final Button addIngredients = (Button) findViewById(R.id.RecipeIngredientsBtn);
        Button addTools = (Button) findViewById(R.id.RecipeToolsBtn);


        //POST RECIPE FUNCTION
        Button postRecipeBtn = (Button) findViewById(R.id.PostRecipeBtn);
        postRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = NameTxt.getText().toString();
                if (tmp != "") {
                    recipeName = tmp;
                    tmp = InstructionsTxt.getText().toString();
                    if (tmp != "") {
                        recipeInstructions = tmp;
                        tmp = TimeTxt.getText().toString();
                        int temp = Integer.parseInt(tmp);
                        if (temp != 0) {
                            recipeTime = temp;


                            postRecipe(recipeName, recipeInstructions, recipeTime);

                            Intent intent = new Intent(getApplicationContext(), Main.class);
                            startActivity(intent);
                            finish();
                        } else Log.i("ERROR", "No time");
                    } else Log.i("ERROR", "NO instructions");
                } else {
                    Toast.makeText(getApplicationContext(), "Something was wrong..", Toast.LENGTH_SHORT);
                    Log.i("ERROR", "No name");
                }

            }
        });

        //ADD INGREDIENTS FUNCTION
        addIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), addIngredients.class);
                intent.putExtra("List", ingredients);
                intent.putExtra("Type", 1);
                startActivityForResult(intent, 1);
            }
        });

        //ADD TOOLS FUNCTION
        addTools.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), addIngredients.class);
                intent.putExtra("List", tools);
                intent.putExtra("Type", 0);
                startActivityForResult(intent, 1);
            }
        }));
    }

    public void postRecipe(String recipeName, String recipeInstructions, int recipeTime) {
    /*
    TODO:
    Request for post the recipe.
    need to get sessionID from login.
    category and images also need to defined in the layout.
    ingredients and tools should be passed to this function.

     */

        SharedPreferences preferences = getSharedPreferences(UsefulFunctions.PREFERENCES_KEY, Context.MODE_PRIVATE);
        post = false;
        String ingredientsarray = "";

        String URL = urlJsonObject + "?" + UsefulFunctions.SESSIONID_KEY + "=" + preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000") + "&"
                + UsefulFunctions.TITLE_KEY + "=" + recipeName + "&" + UsefulFunctions.INSTRUCTION_KEY + "="
                + recipeInstructions + "&" + UsefulFunctions.TIME_KEY + "=" + recipeTime + "&"
                + UsefulFunctions.CATEGORY_KEY + "=" + "Food" + "&" + UsefulFunctions.IMAGE_KEY + "=" + "blank" + "&"
                + UsefulFunctions.INGREDIENT_KEY + "=" + ingredientsarray.toString()
                + "&" + UsefulFunctions.TOOLS_KEY + "=" + tools.toString();

        JsonObjectRequest jObjReq = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getBoolean(UsefulFunctions.SUC_KEY)) {
                        post = jsonObject.getBoolean(UsefulFunctions.SUC_KEY);

                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
