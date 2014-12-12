package se.FSN.foodsocialnetwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class PostRecipe extends Activity {

    //TODO:
    //ADD VARIABLES HERE AS PRIVATE OR WHAT YOU WANT.
    //No define them here. just declare.
    private String recipeName, recipeInstructions, Time;
    private int recipeTime;
    TextView NameTxt, InstructionsTxt, TimeTxt;
    private ArrayList<String> ingredients;

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

        NameTxt = (TextView) findViewById(R.id.editRecipeNameTxt);
        InstructionsTxt = (TextView) findViewById(R.id.editRecipeInstructionsTxt);
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
                intent.putStringArrayListExtra("List", ingredients);
                startActivity(intent);
            }
        });
    }

    public void postRecipe(String recipeName, String recipeInstructions, int recipeTime){
    /*
    TODO:
    Request for post the recipe.
     */
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
