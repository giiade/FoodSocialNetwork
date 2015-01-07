package se.FSN.foodsocialnetwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import se.FSN.foodsocialnetwork.utils.UsefulFunctions;

public class addIngredients extends Activity {

    private TextView addThings, quantityTxt;
    private Spinner unitTypeSpn;
    private Button addBtn, saveBtn;
    private ListView thingsList;
    private AddIngredientsAdapter adapter;
    private ArrayAdapter<String> toolAdapter;
    private List<Ingredient> ingredientsList = new ArrayList<Ingredient>();
    private List<String> toolsList = new ArrayList<String>();
    private String title, quantity, inputType;
    private CheckBox isOptionalCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);

        addThings = (TextView) findViewById(R.id.ingredInput);
        quantityTxt = (TextView) findViewById(R.id.quantityInput);
        addBtn = (Button) findViewById(R.id.addBtn);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        thingsList = (ListView) findViewById(R.id.addList);
        isOptionalCheck = (CheckBox) findViewById(R.id.isOptionalCheck);
        unitTypeSpn = (Spinner) findViewById(R.id.inputTypeSpn);
        final int type;

        //ADAPTER FOR SPINNER
        final ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);

        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitTypeSpn.setAdapter(spnAdapter);

        Bundle extras = getIntent().getExtras();

        type = extras.getInt("Type");

        /*adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                things);
                */
        ingredientsList = (ArrayList<Ingredient>) extras.get(UsefulFunctions.INTENTLIST_KEY);
        adapter = new AddIngredientsAdapter(this, ingredientsList);

        if (type == 0) {
            toolsList = (ArrayList<String>) extras.get(UsefulFunctions.INTENTLIST_KEY);
            //We are going to add tools
            isOptionalCheck.setVisibility(View.GONE);
            unitTypeSpn.setVisibility(View.GONE);
            quantityTxt.setVisibility(View.GONE);

            toolAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, toolsList);
            thingsList.setAdapter(toolAdapter);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (addThings.getText().toString() != "") {

                        title = addThings.getText().toString();

                        toolsList.add(title);
                        addThings.setText("");
                        toolAdapter.notifyDataSetChanged();

                    }
                }
            });


        } else {
            //We are going to add Ingredients


            isOptionalCheck.setVisibility(View.VISIBLE);
            unitTypeSpn.setVisibility(View.VISIBLE);
            quantityTxt.setVisibility(View.VISIBLE);

            thingsList.setAdapter(adapter);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (addThings.getText().toString() != "") {

                        inputType = unitTypeSpn.getSelectedItem().toString();
                        title = addThings.getText().toString();
                        quantity = quantityTxt.getText().toString();

                        Ingredient ingredient = new Ingredient();
                        ingredient.setTitle(title);
                        ingredient.setQuantity(quantity);
                        ingredient.setInputType(inputType);
                        ingredient.setOptional(isOptionalCheck.isChecked());
                        ingredientsList.add(ingredient);
                        addThings.setText("");
                        quantityTxt.setText("");
                        isOptionalCheck.setChecked(false);
                        adapter.notifyDataSetChanged();

                    }
                }
            });
        }


        //Intent intent = getIntent();
        //things = (ArrayList<String>) intent.getStringArrayListExtra("List").clone();


        //Save data Function.
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = addIngredients.this.getIntent();
                if (type == 0) {
                    //We return the Tool array
                    returnIntent.putStringArrayListExtra(UsefulFunctions.INTENTLIST_KEY, (ArrayList<String>) toolsList);
                    Log.e("HOLO", toolsList.toString());
                } else {
                    //We return the ingredient array
                    returnIntent.putExtra(UsefulFunctions.INTENTLIST_KEY, (ArrayList<Ingredient>) ingredientsList);
                    Log.e("HOLO", ingredientsList.toString());
                }
                addIngredients.this.setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        //OnItemClick Function
        thingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Long Click for delete", Toast.LENGTH_SHORT).show();
            }
        });


        //OnLongItemClick Function
        thingsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (type == 0) {
                    //Tools
                    toolsList.remove(position);
                    toolAdapter.notifyDataSetChanged();
                } else {
                    ingredientsList.remove(position);
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });

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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
