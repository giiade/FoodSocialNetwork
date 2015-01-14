package se.FSN.foodsocialnetwork;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import se.FSN.foodsocialnetwork.utils.AppController;
import se.FSN.foodsocialnetwork.utils.UsefulFunctions;

/**
 * Created by JulioLopez on 14/1/15.
 */
public class ShowUser extends Activity {


    private SharedPreferences preferences;

    private NetworkImageView imageView;
    private ImageButton imgButton;
    private Button editBtn;

    private Friend friend;

    private ArrayList<String> myFriendIDS = new ArrayList<>();

    private TextView nameTxt, mailTxt, countryTxt;

    String id;

    MenuItem itemFavo, itemMyRec, itemMyAcc, itemMyFri, itemShowAll;


    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        id = extras.getString(UsefulFunctions.MAIL_KEY);
        boolean isMyAcc = false;
        isMyAcc = extras.getBoolean(UsefulFunctions.MYACCOUNT_KEY);


        if (!isMyAcc)
            setContentView(R.layout.showuserprofile_layout);
        else
            setContentView(R.layout.myprofile_layout);


        preferences = getSharedPreferences(UsefulFunctions.PREFERENCES_KEY, MODE_PRIVATE);


        imageView = (NetworkImageView) findViewById(R.id.singleUImage);

        nameTxt = (TextView) findViewById(R.id.singleUName);
        mailTxt = (TextView) findViewById(R.id.showUseremail);
        countryTxt = (TextView) findViewById(R.id.showUserCountry);

        imgButton = (ImageButton) findViewById(R.id.UserFavoriteButton);
        editBtn = (Button) findViewById(R.id.EditMyAccountBtn);


        friend = new Friend();
        Resources res = getResources();


        myFriendIDS = UsefulFunctions.convertToArray(preferences.getString(UsefulFunctions.MYFRIENDS_KEY, null));


        if (!isMyAcc) {
            if (myFriendIDS.contains(id)) {
                imgButton.setImageDrawable(res.getDrawable(R.drawable.star_on));
            } else {
                imgButton.setImageDrawable(res.getDrawable(R.drawable.star_off));
            }
        }


        requestSearch(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), id);


        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActionBar actionBar = getActionBar();
                actionBar.setTitle(friend.getUsername());
            }
        }, 500);


        if (!isMyAcc) {
            imgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resources res = getResources();

                    if (myFriendIDS != null) {
                        if (myFriendIDS.contains(id)) {
                            unfollowRequest(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), id);
                            imgButton.setImageDrawable(res.getDrawable(R.drawable.star_off));
                        } else {
                            followRequest(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), id);
                            imgButton.setImageDrawable(res.getDrawable(R.drawable.star_on));
                        }
                    } else {

                        myFriendIDS = new ArrayList<String>();
                        followRequest(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), id);
                        imgButton.setImageDrawable(res.getDrawable(R.drawable.star_on));
                    }

                }
            });
        } else {
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), EditAccount.class);
                    startActivity(i);
                }
            });

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestSearch(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), id);
    }

    private void followRequest(String sessionID, String id) {

        String url = UsefulFunctions.ADDFRIEND_URL + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID
                + "&" + UsefulFunctions.FOLLOWER_KEY + "=" + id;

        Log.d("URL_ADDFRIEND", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        Toast.makeText(getApplicationContext(),
                                friend.getUsername() + " Followed", Toast.LENGTH_SHORT).show();
                        myFriendIDS.add(friend.getEmail());

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

    private void unfollowRequest(String sessionID, String id) {

        String url = UsefulFunctions.DELETEFRIEND_URL + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID
                + "&" + UsefulFunctions.FOLLOWER_KEY + "=" + id;
        Log.d("URL_UNFOLLOW", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    Log.d("URL_UNFOLLOW", response.toString());

                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        Toast.makeText(getApplicationContext(),
                                friend.getUsername() + " Unfollowed", Toast.LENGTH_SHORT).show();
                        myFriendIDS.remove(friend.getEmail());

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

                 /*
                TODO: Do something with the result.
                 */

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

    private void requestSearch(final String sessionID, String tag) {

        String url = UsefulFunctions.SEARCHUSER_URL + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID
                + "&" + UsefulFunctions.SEARCHTAG_KEY + "=" + tag;
        Log.d("URL_Friends", url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("URL_SEARCH", response.toString());

                try {
                    //If the response is correct
                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        JSONArray jsonFriends = null;
                        jsonFriends = response.getJSONArray(UsefulFunctions.FRIENDARRAY_KEY);


                        for (int i = 0; i < jsonFriends.length(); i++) {
                            //We create a Friend Object
                            //We create a JsonObject that will contain the data of this friend
                            JSONObject jFriend = (JSONObject) jsonFriends.get(i);
                            //We pass the data to the recipe object
                            friend.setID(Integer.toString(i));
                            friend.setUsername(jFriend.getString(UsefulFunctions.USERNAME_KEY));
                            friend.setEmail(jFriend.getString(UsefulFunctions.MAIL_KEY));
                            friend.setCountry(jFriend.getString(UsefulFunctions.COUNTRY_KEY));
                            friend.setImgUrl(UsefulFunctions.createUserImageURL(sessionID, friend.getEmail()));

                            //Show Friend in the Layout

                            nameTxt.setText(friend.getUsername());
                            mailTxt.setText(friend.getEmail());
                            countryTxt.setText(friend.getCountry());
                            imageView.setImageUrl(friend.getImgUrl(), imageLoader);
                        }

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

        itemShowAll.setVisible(true);
        itemFavo.setVisible(false);
        itemMyRec.setVisible(false);

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
