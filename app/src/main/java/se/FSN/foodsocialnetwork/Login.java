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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import se.FSN.foodsocialnetwork.utils.AppController;
import se.FSN.foodsocialnetwork.utils.UsefulFunctions;

public class Login extends Activity {


    SharedPreferences preferences;

    //Variables
    private String urlJsonObj = " http://83.254.221.239:9000/login";
    private String username, password;
    private boolean login;
    TextView mailText, passText;
    CheckBox saveData;
    private String sessionID, error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //From here Start Coding
        //Access to the shared preference file.
        preferences = getSharedPreferences(UsefulFunctions.PREFERENCES_KEY, Context.MODE_PRIVATE);

        mailText = (EditText) findViewById(R.id.emailInput);
        passText = (TextView) findViewById(R.id.passInput);
        saveData = (CheckBox) findViewById(R.id.saveLoginCheck);

        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        Button registerBtn = (Button) findViewById(R.id.registerBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = mailText.getText().toString();
                if (mailText.getText().length() > 0) {
                    username = temp;
                    temp = passText.getText().toString();
                    if (passText.getText().length() > 0) {
                        password = temp;
                        requestLogin(username, password);
                    } else {
                        //Toast.makeText(getApplicationContext(),"No password", Toast.LENGTH_SHORT);
                        Log.d("ERROR", "NO PASS");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No user or Pass", Toast.LENGTH_SHORT).show();
                    Log.d("ERROR", "No User");
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), createAccount.class);
                startActivity(i);

            }
        });

    }

    private void requestLogin(String user, final String pass) {


        //?email=myEmail@email.com&password=pass
        String URL = urlJsonObj + "?" + UsefulFunctions.MAIL_KEY + "=" + username + "&" + UsefulFunctions.PASS_KEY + "=" + password;
        JsonObjectRequest jObjReq = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.d("URL " + Login.class.toString(), jsonObject.toString());
                    error = jsonObject.getString(UsefulFunctions.ERROR_KEY);
                    SharedPreferences.Editor editor = preferences.edit();
                    if (jsonObject.getBoolean(UsefulFunctions.SUC_KEY)) {
                        login = jsonObject.getBoolean(UsefulFunctions.SUC_KEY);
                        sessionID = jsonObject.getString(UsefulFunctions.SESSIONID_KEY);


                        //Save the SessionId for further requests.
                        editor.putString(UsefulFunctions.SESSIONID_KEY, sessionID);

                        //Checkbox
                        if (saveData.isChecked()) {
                            // If the user want to save the login.
                            //it will test the boolean on the splash screen.
                            editor.putString(UsefulFunctions.MAIL_KEY, username);
                            editor.putString(UsefulFunctions.PASS_KEY, password);
                            editor.putBoolean(UsefulFunctions.LOGED_KEY, true);
                        }

                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), Main.class);
                        startActivity(intent);
                        finish();

                    } else {

                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

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
        AppController.getInstance().addToRequestQueue(jObjReq);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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
