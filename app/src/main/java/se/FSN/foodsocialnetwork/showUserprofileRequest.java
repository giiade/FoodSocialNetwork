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
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import se.FSN.foodsocialnetwork.utils.AppController;
import se.FSN.foodsocialnetwork.utils.UsefulFunctions;



public class showUserprofileRequest extends Activity{

    /*
    First showuserprofilerequest should be called i think, so that it updates the values of username,email and country to display in the layout.
    */
    private String urlJsonObj = " http://83.254.221.239:9000/profile";


    String SESSION_KEY = "SessionID";
    String MAIL_KEY = "email";
    String USER_KEY = "username";
    String COUNTRY_KEY = "country";
    String username, mail, country;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showuserprofile_layout);

        TextView showusername = (TextView) findViewById(R.id.showUsername);
        TextView showmail = (TextView) findViewById(R.id.showemail);
        TextView showcountry = (TextView) findViewById(R.id.showCountry);

        showusername.setText(username);
        showmail.setText(mail);
        showcountry.setText(country);

    }


    public void showUserProfileRequest(String sessionID, final String email){
        Log.i("DATOS", sessionID + "," + email);
        String url = urlJsonObj + "?" + SESSION_KEY + "=" + sessionID + "&" + MAIL_KEY + "=" + email;
        Log.i("URL", url);

        final JsonObjectRequest jsnObjReq = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        username = response.getString(USER_KEY);
                        mail = response.getString(MAIL_KEY);
                        country = response.getString(COUNTRY_KEY);

                    }
                }catch (JSONException e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

}
