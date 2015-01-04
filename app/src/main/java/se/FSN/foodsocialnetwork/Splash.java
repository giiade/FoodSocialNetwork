package se.FSN.foodsocialnetwork;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import se.FSN.foodsocialnetwork.utils.AppController;
import se.FSN.foodsocialnetwork.utils.UsefulFunctions;

public class Splash extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
    Animation fadeOut;
    RelativeLayout splashLay;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferences = getSharedPreferences(UsefulFunctions.PREFERENCES_KEY, Context.MODE_PRIVATE);


        splashLay = (RelativeLayout) findViewById(R.id.splashRlt);

        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fadeout);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {


                Intent i;
                if (preferences.getBoolean(UsefulFunctions.LOGED_KEY, false)) {
                    i = new Intent(getApplicationContext(), Main.class);
                    requestLogin();
                } else {
                    i = new Intent(Splash.this, Login.class);

                }

                startActivity(i);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //Nothing
            }
        });


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                splashLay.startAnimation(fadeOut);

            }
        }, SPLASH_TIME_OUT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
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

    private void requestLogin() {
        String username = preferences.getString(UsefulFunctions.MAIL_KEY, "Null");
        String password = preferences.getString(UsefulFunctions.PASS_KEY, "1234");

        //?email=myEmail@email.com&password=pass
        String URL = UsefulFunctions.LOGIN_URL + "?" + UsefulFunctions.MAIL_KEY + "=" + username + "&" + UsefulFunctions.PASS_KEY + "=" + password;
        JsonObjectRequest jObjReq = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.d("URL " + Splash.class.toString(), jsonObject.toString());
                    String sessionID;
                    SharedPreferences.Editor editor = preferences.edit();
                    if (jsonObject.getBoolean(UsefulFunctions.SUC_KEY)) {
                        sessionID = jsonObject.getString(UsefulFunctions.SESSIONID_KEY);
                        //Save the SessionId for further requests.
                        editor.putString(UsefulFunctions.SESSIONID_KEY, sessionID);
                        editor.commit();
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


}
