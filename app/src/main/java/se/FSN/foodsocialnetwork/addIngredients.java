package se.FSN.foodsocialnetwork;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class addIngredients extends Activity {

    private TextView addThings, quantityTxt;
    private Spinner unitTypeSpn;
    private Button addBtn;
    private ListView thingsList;
    private ArrayList<String> things = new ArrayList<String>();
    private AddIngredientsAdapter adapter;
    private List<Ingredient> ingredientsList = new ArrayList<Ingredient>();
    private String title, quantity, inputType;
    private String optional = "mandatory";
    private CheckBox isOptionalCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);

        addThings = (TextView) findViewById(R.id.ingredInput);
        quantityTxt = (TextView) findViewById(R.id.quantityInput);
        addBtn = (Button) findViewById(R.id.addBtn);
        thingsList = (ListView) findViewById(R.id.addList);
        isOptionalCheck = (CheckBox) findViewById(R.id.isOptionalCheck);

        unitTypeSpn = (Spinner) findViewById(R.id.inputTypeSpn);

        final ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitTypeSpn.setAdapter(spnAdapter);



        Bundle extras = getIntent().getExtras();
        things = extras.getStringArrayList("List");

        /*adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                things);
                */
        adapter = new AddIngredientsAdapter(this, ingredientsList);
        thingsList.setAdapter(adapter);

        //Intent intent = getIntent();
        //things = (ArrayList<String>) intent.getStringArrayListExtra("List").clone();


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                TODO:
                Test empty input
                 */

                if (addThings.getText().toString() != "") {

                    inputType = unitTypeSpn.getSelectedItem().toString();
                    title = addThings.getText().toString();
                    quantity = quantityTxt.getText().toString() + " " + inputType;

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
        /*
        TODO:
        Boton saveit debe devolver el resultado a la applicacion principal
         */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_ingredients, menu);
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
