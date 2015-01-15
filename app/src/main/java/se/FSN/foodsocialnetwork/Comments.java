package se.FSN.foodsocialnetwork;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import se.FSN.foodsocialnetwork.utils.AppController;
import se.FSN.foodsocialnetwork.utils.UsefulFunctions;

/**
 * Created by JulioLopez on 15/1/15.
 */
public class Comments extends Activity {

    private EditText commentBody;
    private TextView ratingCompl;
    private EditText commentTitle;
    private RatingBar ratingBarDialog;
    private Button sendBtn;

    private SharedPreferences preferences;

    private String recipeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentlayout);

        Bundle extras = getIntent().getExtras();
        recipeID = extras.getString(UsefulFunctions.ID_KEY);
        Float rate = extras.getFloat(UsefulFunctions.RATE_KEY);

        preferences = getSharedPreferences(UsefulFunctions.PREFERENCES_KEY, MODE_PRIVATE);

        ratingBarDialog = (RatingBar) findViewById(R.id.ratingComent);
        commentTitle = (EditText) findViewById(R.id.commentTitle);
        ratingCompl = (TextView) findViewById(R.id.ratingComplement);
        commentBody = (EditText) findViewById(R.id.commentTxt);
        sendBtn = (Button) findViewById(R.id.sendBtn);

        ratingBarDialog.setRating(rate);
        ratingBarDialog.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                switch ((int) rating) {
                    case 1:
                        ratingCompl.setText("I hate it, Horrible!");
                        break;
                    case 2:
                        ratingCompl.setText("I didn't like it");
                        break;
                    case 3:
                        ratingCompl.setText("Meh.. its okay");
                        break;
                    case 4:
                        ratingCompl.setText("I like it");
                        break;
                    case 5:
                        ratingCompl.setText("Love it. Perfect!");
                        break;
                }
            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = (commentTitle.getText().length() > 0) ? commentTitle.getText().toString() : "";
                String body = (commentBody.getText().length() > 0) ? commentBody.getText().toString() : "Empty comment";
                int score = (int) ratingBarDialog.getRating();

                try {
                    body = URLEncoder.encode(body, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                SendComment(preferences.getString(UsefulFunctions.SESSIONID_KEY, "0000"), recipeID, body, score);


            }
        });


    }

    private void SendComment(String sessionID, String recipeID, String comment, int rate) {
        String url = UsefulFunctions.COMMENT_URL + "?" + UsefulFunctions.SESSIONID_KEY + "=" + sessionID
                + "&" + UsefulFunctions.ID_KEY + "=" + recipeID
                + "&" + UsefulFunctions.COMMENT_KEY + "=" + comment
                + "&" + UsefulFunctions.RATE_KEY + "=" + rate;

        Log.d("URL_SENDCOMMENT", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean(UsefulFunctions.SUC_KEY)) {
                        Toast.makeText(getApplicationContext(),
                                "Comment Sended", Toast.LENGTH_SHORT).show();
                        finish();


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
}



