package se.FSN.foodsocialnetwork;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import se.FSN.foodsocialnetwork.utils.AppController;
import se.FSN.foodsocialnetwork.utils.UsefulFunctions;


public class EditAccount extends Activity {

    //Username
    TextView usernameTxt;

    //Password
    TextView passTxt, rePassTxt;

    //Country
    Spinner countrySpn;

    //Button
    Button saveBtn;

    private String countryCode;

    SharedPreferences preferences;

    MenuItem itemFavo, itemMyRec, itemMyAcc, itemMyFri, itemShowAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        preferences = getSharedPreferences(UsefulFunctions.PREFERENCES_KEY, MODE_PRIVATE);

        getActionBar().hide();


        //Set up the Country Spinner
        countrySpn = (Spinner) findViewById(R.id.editspinnerCountry);
        final String[] countryL = Locale.getISOCountries();
        final ArrayList<String> country = new ArrayList<String>();

        for (int i = 0; i < countryL.length; i++)
            if (countryL[i] != "") {
                Locale local = new Locale("", countryL[i]);
                country.add(local.getDisplayCountry());
            }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, country);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        countrySpn.setAdapter(adapter);
        countrySpn.setPrompt("Select a country");

        countrySpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryCode = countryL[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Nothing
            }
        });

        //Set up the username
        usernameTxt = (TextView) findViewById(R.id.editUsernameTxt);

        //Set up password
        passTxt = (TextView) findViewById(R.id.editPasswordTxt);
        rePassTxt = (TextView) findViewById(R.id.confirmPasswordTxt);

        //Set up the button
        saveBtn = (Button) findViewById(R.id.editAccountBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameTxt.getText().length() > 0) {
                    ChangeUsername(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), usernameTxt.getText().toString());

                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
                }

                if (passTxt.getText().length() > 0) {
                    if (passTxt.getText() == rePassTxt.getText()) {
                        ChangePassword(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), rePassTxt.getText().toString());
                        requestLogout(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"));
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        startActivity(i);
                    }
                }

                if (countryCode != null) {
                    ChangeCountry(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), countryCode);
                }
            }
        });


    }

    private void ChangeUsername(String sessionID, String username) {
        String url = UsefulFunctions.CHANGEUSER_URL + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID
                + "&" + UsefulFunctions.USERNAME_KEY + "=" + username;

        Log.d("URL_ChangeUser", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        Toast.makeText(getApplicationContext(),
                                "Username changed.", Toast.LENGTH_SHORT).show();

                        //requestLogout(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"));


                    } else {
                        Toast.makeText(getApplicationContext(), response.getString(UsefulFunctions.ERROR_KEY), Toast.LENGTH_SHORT).show();
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

    private void ChangePassword(String sessionID, String password) {

        String url = UsefulFunctions.CHANGEPASS_URL + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID
                + "&" + UsefulFunctions.PASS_KEY + "=" + password;

        Log.d("URL_ChangePass", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        Toast.makeText(getApplicationContext(),
                                "password changed.", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(getApplicationContext(), response.getString(UsefulFunctions.ERROR_KEY), Toast.LENGTH_SHORT).show();
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

    private void ChangeCountry(String sessionID, String countryCode) {

        String url = UsefulFunctions.CHANGECOUNTRY_URL + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID
                + "&" + UsefulFunctions.COUNTRY_KEY + "=" + countryCode;

        Log.d("URL_ChangeCountry", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        Toast.makeText(getApplicationContext(),
                                "Country changed.", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(getApplicationContext(), response.getString(UsefulFunctions.ERROR_KEY), Toast.LENGTH_SHORT).show();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        itemFavo = menu.findItem(R.id.mnuFavourites);
        itemMyAcc = menu.findItem(R.id.mnuMyAccount);
        itemMyFri = menu.findItem(R.id.mnuMyFriends);
        itemShowAll = menu.findItem(R.id.mnuShowAll);
        itemMyRec = menu.findItem(R.id.mnuMyRecipes);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //TODO: Finish the menu and actions of it.
        //TODO: AÑADIR FAVORITOS Y MIS RECETAS EN LOS MENUS.

        switch (item.getItemId()) {
            case R.id.mnuLogOut:
                Log.i("MENU" + Main.class.toString(), "LOGOUT");
                //LOGOUT
                requestLogout(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"));
                return true;
            case R.id.mnuMyAccount:
                Log.i("MENU" + Main.class.toString(), "MY ACCOUNT");
                //We go to our friends Class
                Intent My = new Intent(getApplicationContext(), ShowUser.class);
                My.putExtra(UsefulFunctions.MAIL_KEY, preferences.getString(UsefulFunctions.MAIL_KEY, "Null"));
                My.putExtra(UsefulFunctions.MYACCOUNT_KEY, true);
                startActivity(My);
                return true;

            case R.id.mnuShowAll:
                Log.i("MENU" + Main.class.toString(), "Show All");
                Intent all = new Intent(getApplicationContext(), Main.class);
                startActivity(all);
                return true;
            case R.id.mnuMyFriends:
                Log.i("MENU" + Main.class.toString(), "My Friends");
                Intent i = new Intent(getApplicationContext(), MyFriends.class);
                startActivity(i);
                return true;


        }
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
