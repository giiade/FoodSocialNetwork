package se.FSN.foodsocialnetwork;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
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


public class PostRecipe extends Activity {

    //TODO:
    //ADD VARIABLES HERE AS PRIVATE OR WHAT YOU WANT.
    //No define them here. just declare.
    private String recipeName, recipeInstructions, Time;
    private int recipeTime;
    TextView NameTxt, InstructionsTxt, TimeTxt;
    private ArrayList<HashMap<String, String>> ingredients = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> Values = new HashMap<String, String>();

    private final String INAME_KEY = "name";
    private final String IQUAN_KEY = "quantity";
    private final String ITYPE_KEY = "type";

    private String urlJsonObject = " http://83.254.221.239:9000/createRecepie";
    String TITLE_KEY = "title";
    String INSTRUCTION_KEY = "instruction";
    String TIME_KEY = "time";
    String SESSIONID_KEY = "sessionID";
    String CATEGORY_KEY = "category";
    String IMAGE_KEY = "image";
    String INGREDIENT_KEY = "ingredients";
    String OPTIONAL_KEY = "isOptional";
    String AMOUNT_KEY = "amount";
    String AMOUNTTYPE_KEY = "amountType";
    String TOOLS_KEY = "tools";
    String SUC_KEY = "success";
    private boolean post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_recipe);

        /*
        TODO:
        Define the variables and make the methods that we will need.
        In this case we will use a button, and some TextViews.
        The declaration of textview will be like
        TextView ST = (TextView) findViewById(R.id.ST)
         */

        NameTxt = (TextView) findViewById(R.id.RecipeNameTxt);
        InstructionsTxt = (TextView) findViewById(R.id.RecipeIngredientsTxt);
        TimeTxt = (TextView) findViewById(R.id.editRecipeTimeTxt);

        final Button addIngredients = (Button) findViewById(R.id.RecipeIngredientsBtn);
        Button addTools = (Button) findViewById(R.id.RecipeToolsBtn);
        //On clicking these buttons new pages should be opened.
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

                            //TODO:
                            //Create the method forc it.
                            postRecipe(recipeName,recipeInstructions,recipeTime);
                            //should also pass ingredients and tools..
                            Intent intent = new Intent(getApplicationContext(), Main.class);
                            startActivity(intent);
                            finish();
                        } else Log.i("ERROR", "No time");
                    } else Log.i("ERROR", "NO instructions");
                } else Log.i("ERROR", "No name");

            }
        });

        addIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), addIngredients.class);
                intent.putExtra("List", ingredients);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void postRecipe(String recipeName, String recipeInstructions, int recipeTime){
    /*
    TODO:
    Request for post the recipe.
    need to get sessionID from login.
    category and images also need to defined in the layout.
    ingredients and tools should be passed to this function.

     */
        post = false;
        String ingredientsarray = "";
        String tools = "";
        String URL = urlJsonObject + "?" + SESSIONID_KEY + "=" + sessionID + "&" + TITLE_KEY + "=" + recipeName + "&" + INSTRUCTION_KEY + "=" + recipeInstructions + "&" + TIME_KEY + "=" + recipeTime + "&" + CATEGORY_KEY + "=" + category + "&" + IMAGE_KEY + "=" + image + "&" + INGREDIENT_KEY + "=" + ingredientsarray + "&" + TOOLS_KEY + "=" tools;
        JsonObjectRequest jObjReq = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try{
                    if(jsonObject.getBoolean(SUC_KEY)){
                        post = jsonObject.getBoolean(SUC_KEY);

                    }
                }catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }
        },new Response.ErrorListener() {
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
