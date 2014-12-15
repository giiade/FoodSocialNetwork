package se.FSN.foodsocialnetwork;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class addIngredients extends Activity {

    private TextView addThings;
    private Button addBtn;
    private ListView thingsList;
    private ArrayList<String> things = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);
        addThings = (TextView) findViewById(R.id.ingredInput);
        addBtn = (Button) findViewById(R.id.addBtn);
        thingsList = (ListView) findViewById(R.id.addList);
        Bundle extras = getIntent().getExtras();
        things = extras.getStringArrayList("List");
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                things);
        thingsList.setAdapter(adapter);

        //Intent intent = getIntent();
        //things = (ArrayList<String>) intent.getStringArrayListExtra("List").clone();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addThings.getText().toString() != "") {
                    things.add(addThings.getText().toString());
                    addThings.setText("");
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
