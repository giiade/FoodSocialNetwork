package se.FSN.foodsocialnetwork;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class Login extends Activity {

    /*TODO:
    Add variables necessary to login. TextViews and Buttons as well as Strings.
    Make the request to the server
    Save the sessionID.
     */

    //Variables
    private String username, password;
    TextView userText, passText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //From here Start Coding
        userText = (TextView) findViewById(R.id.emailInput);
        passText = (TextView) findViewById(R.id.passInput);

        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = userText.getText().toString();
                if (temp != "") {
                    username = temp;
                    temp = passText.getText().toString();
                    if (temp != "") {
                        password = temp;
                        requestLogin(username, password);
                    } else Log.i("ERROR", "NO PASS");
                } else Log.i("ERROR", "No User");
            }
        });

    }

    private void requestLogin(String user, String pass) {
        JsonObjectRequest jObjReq = new JsonObjectRequest(Request.Method.GET, "URL", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
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
