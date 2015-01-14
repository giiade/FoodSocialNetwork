package se.FSN.foodsocialnetwork;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import se.FSN.foodsocialnetwork.utils.AppController;
import se.FSN.foodsocialnetwork.utils.UsefulFunctions;

public class MyFriends extends Activity {

    private SharedPreferences preferences;
    private FriendAdapter adapter;
    private ListView friendsList;

    private List<Friend> friends = new ArrayList<Friend>();

    private EditText searchString;
    MenuItem itemFavo, itemMyRec, itemMyAcc, itemMyFri, itemShowAll;

    private ArrayList<String> myFriendsIds = new ArrayList<>();


    //TODO: RELLENAR LA LISTA, SI ESTÁ VACIA DEJARLA CON UN MENSAJE DE BUSCAR AMIGOS NUEVOS.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_friends_layout);

        ActionBar actionBar = getActionBar();
        actionBar.setTitle("My friends");

        preferences = getSharedPreferences(UsefulFunctions.PREFERENCES_KEY, MODE_PRIVATE);

        searchString = (EditText) findViewById(R.id.searchText_friend);

        //For the list
        friendsList = (ListView) findViewById(R.id.friendList);
        adapter = new FriendAdapter(this, friends);
        friendsList.setAdapter(adapter);

        //Request For Show all possible friends
        requestSearch(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), "");


        //Button for searching
        ImageButton searchBTN = (ImageButton) findViewById(R.id.searchBtn_friend);
        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagToSearch = "";
                if (searchString.length() > 0) {
                    try {
                        tagToSearch = URLEncoder.encode(searchString.getText().toString(), "UTF8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                } else {
                    tagToSearch = "";
                }
                requestSearch(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), tagToSearch);
            }


        });


        //Button for show My Friends
        Button myFriBtn = (Button) findViewById(R.id.showMyFriendsBtn);
        myFriBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMyFriends(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"));
            }
        });


        //
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void requestSearch(String sessionID, String tag) {

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

                        List<Friend> auxList = new ArrayList<>();

                        for (int i = 0; i < jsonFriends.length(); i++) {
                            //We create a Friend Object
                            Friend friend = new Friend();
                            //We create a JsonObject that will contain the data of this friend
                            JSONObject jFriend = (JSONObject) jsonFriends.get(i);
                            //We pass the data to the recipe object
                            friend.setID(Integer.toString(i));
                            friend.setUsername(jFriend.getString(UsefulFunctions.USERNAME_KEY));
                            friend.setEmail(jFriend.getString(UsefulFunctions.MAIL_KEY));
                            friend.setCountry(jFriend.getString(UsefulFunctions.COUNTRY_KEY));
                            //friend.setImgUrl(UsefulFunctions.createImageURL(sessionID, recipe.getID()));
                            //Add the recipe object to the array
                            auxList.add(friend);
                            adapter.swapItems(auxList);
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

    private void requestMyFriends(String sessionID) {
        String url = UsefulFunctions.SEARCHUSER_URL + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID;
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
                        jsonFriends = response.getJSONArray(UsefulFunctions.USERARRAY_KEY);

                        List<Friend> auxList = new ArrayList<>();

                        for (int i = 0; i < jsonFriends.length(); i++) {
                            //We create a Friend Object
                            Friend friend = new Friend();
                            //We create a JsonObject that will contain the data of this friend
                            JSONObject jFriend = (JSONObject) jsonFriends.get(i);
                            //We pass the data to the recipe object
                            String mail = jFriend.getString(UsefulFunctions.MAIL_KEY);
                            myFriendsIds.add(mail);
                            friend = UsefulFunctions.HasItem(friends, mail);
                            //friend.setImgUrl(UsefulFunctions.createImageURL(sessionID, recipe.getID()));
                            //Add the recipe object to the array
                            auxList.add(friend);
                            adapter.swapItems(auxList);
                        }
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(UsefulFunctions.MYFRIENDS_KEY, UsefulFunctions.convertToString(myFriendsIds));
                        editor.commit();

                    } else {
                        List<Friend> auxList = new ArrayList<>();
                        Friend f = new Friend();
                        f.setUsername("You don't have any friends yet");
                        auxList.add(f);
                        adapter.swapItems(auxList);
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

    private void addFriend(String sessionID, String followerName) {

    }

    private void deleteFriend(String sessionID, String followerName) {

    }


}
