package se.FSN.foodsocialnetwork;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class createAccount extends Activity {

    private String urlJsonObj = "http://api.androidhive.info/volley/person_object.json";
    /*Variables*/
    String username, countryCode, mail, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        TextView userTxt = (TextView) findViewById(R.id.userTxt);
        TextView passTxt = (TextView) findViewById(R.id.passTxt);
        TextView mailTxt = (TextView) findViewById(R.id.mailTxt);
        Spinner countrySpn = (Spinner) findViewById(R.id.spinnerCountry);
        Button createAccBtn = (Button) findViewById(R.id.createAccountBtn);


        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:
                //COMPROBAR QUE LOS EDITTEXT NO ESTEN VACIOS
                //AÑADIR EL CONTENIDO A LAS VARIABLES
                MakeCreateAccountRequest();
            }
        });


        final Locale[] countryL = Locale.getAvailableLocales();
        final ArrayList<String> country = new ArrayList<String>();

        for (int i = 0; i < countryL.length; i++)
            if (countryL[i].getDisplayCountry() != "")
                country.add(countryL[i].getDisplayCountry());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, country);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        countrySpn.setAdapter(adapter);

        countrySpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryCode = countryL[position].getCountry();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void MakeCreateAccountRequest() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("URL", response.toString());


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
        getMenuInflater().inflate(R.menu.create_account, menu);
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


