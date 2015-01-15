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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import se.FSN.foodsocialnetwork.utils.AppController;
import se.FSN.foodsocialnetwork.utils.UsefulFunctions;


public class PostRecipe extends Activity {

    /*
    TODO:
    Add Image
    */

    private String recipeName, recipeInstructions, Time;
    private int recipeTime;
    TextView NameTxt, InstructionsTxt, TimeTxt;
    private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
    private ArrayList<String> tools = new ArrayList<String>();
    private HashMap<String, String> Values = new HashMap<String, String>();


    private String urlJsonObject = UsefulFunctions.CRECIPE_URL;
    private boolean post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_recipe);

        getActionBar().hide();


        NameTxt = (TextView) findViewById(R.id.RecipeNameTxt);
        InstructionsTxt = (TextView) findViewById(R.id.rInstrucTxt);
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
                intent.putExtra(UsefulFunctions.INTENTLIST_KEY, ingredients);
                intent.putExtra("Type", 1);
                startActivityForResult(intent, 2);
            }
        });

        //ADD TOOLS FUNCTION
        addTools.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), addIngredients.class);
                intent.putExtra(UsefulFunctions.INTENTLIST_KEY, tools);
                intent.putExtra("Type", 0);
                startActivityForResult(intent, 1);
            }
        }));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle extra = data.getExtras();
                tools = (ArrayList<String>) extra.get(UsefulFunctions.INTENTLIST_KEY);
            }
        } else {
            if (resultCode == RESULT_OK) {
                Bundle extra = data.getExtras();
                ingredients = (ArrayList<Ingredient>) extra.get(UsefulFunctions.INTENTLIST_KEY);
            }
        }

    }

    private void postRecipe(String recipeName, String recipeInstructions, int recipeTime) {
    /*
    TODO:
    Request for post the recipe.
    need to get sessionID from login.
    category and images also need to defined in the layout.
    ingredients and tools should be passed to this function.

     */

        SharedPreferences preferences = getSharedPreferences(UsefulFunctions.PREFERENCES_KEY, Context.MODE_PRIVATE);
        String toolToSend = "";
        try {
            recipeName = URLEncoder.encode(recipeName, "UTF-8");
            recipeInstructions = URLEncoder.encode(recipeInstructions, "UTF-8");
            toolToSend = URLEncoder.encode(printTools(tools), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        String url = urlJsonObject + "?" + UsefulFunctions.SESSIONID_KEY + "=" + preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000") + "&"
                + UsefulFunctions.TITLE_KEY + "=" + recipeName + "&" + UsefulFunctions.INSTRUCTION_KEY + "="
                + recipeInstructions + "&" + UsefulFunctions.TIME_KEY + "=" + recipeTime + "&"
                + UsefulFunctions.CATEGORY_KEY + "=" + "2" + "&" + UsefulFunctions.IMAGE_KEY + "=" + "blank" + "&"
                + UsefulFunctions.INGREDIENT_KEY + "=" + ingredients.toString()
                + "&" + UsefulFunctions.TOOLS_KEY + "=" + toolToSend;
        Log.d("URL", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("URL", response.toString());

                try {
                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        Toast.makeText(getApplicationContext(), "Recipe Created", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Main.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*
                TODO:
                Do something with the result. GO to main
                Add Image
                Add Categories.Further release, We need to have data to make the categories.
                 */

                // http://83.254.221.239:9000/createRecepie?sessionID=7afdb348-559f-4ac4-b486-ad7cac9ad04e&title=hola+caraco&instruction=gt+gg+hy&time=2&category=Food&image=blank&ingredient=[{name: "ttt", isOptional: true, amount: 52, amountType: "grams"}, {name: "xft", isOptional: false, amount: 23, amountType: "grams"}]&tools=teefjk
                //Toast.makeText(getApplicationContext(),
                //      "Account created succesfully", Toast.LENGTH_SHORT).show();


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

    private String printTools(ArrayList<String> tools) {
        StringBuilder result = new StringBuilder();
        for (String tool : tools) {
            result.append(tool);
            result.append(",");
        }
        if (result.toString().contains(","))
            result.deleteCharAt(result.lastIndexOf(","));

        return result.toString();
    }
}
