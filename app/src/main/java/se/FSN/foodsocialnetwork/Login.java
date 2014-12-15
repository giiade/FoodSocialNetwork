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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends Activity {

    /*TODO:
    Add variables necessary to login. TextViews and Buttons as well as Strings.
    Make the request to the server
    Save the USER AND PASS
     */

    //Variables
    private String urlJsonObj = " http://83.254.221.239:9000/login";
    String MAIL_KEY = "email";
    String PASS_KEY = "password";
    String SUC_KEY = "success";
    String SESSIONID_KEY = "sessionID";
    private String username, password;
    private boolean login;
    TextView mailText, passText;
    String sessionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //From here Start Coding
        mailText = (TextView) findViewById(R.id.emailInput);
        passText = (TextView) findViewById(R.id.passInput);

        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = mailText.getText().toString();
                if (temp != "") {
                    username = temp;
                    temp = passText.getText().toString();
                    if (temp != "") {
                        password = temp;
                        if (requestLogin(username, password) == true) {
                            Intent intent = new Intent(getApplicationContext(), Main.class);
                            startActivity(intent);
                        }

                    } else Log.i("ERROR", "NO PASS");
                } else Log.i("ERROR", "No User");
            }
        });

    }

    private boolean requestLogin(String user, String pass) {
        login = false;
        //?email=myEmail@email.com&password=pass
        String URL = urlJsonObj + "?" + MAIL_KEY + "=" + username + "&" + PASS_KEY + "=" + password;
        JsonObjectRequest jObjReq = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getBoolean(SUC_KEY)) {
                        login = jsonObject.getBoolean(SUC_KEY);
                        sessionID = jsonObject.getString(SESSIONID_KEY);
                        //TODO:
                        /*
                        Save the sessionID for everytime we open the app if we are logged we don't need to relog.
                         */
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

        return login;
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
