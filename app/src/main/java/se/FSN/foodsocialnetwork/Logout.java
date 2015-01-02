package se.FSN.foodsocialnetwork;

import android.app.Activity;
import android.os.Bundle;



public class Logout extends Activity {

    private boolean logout;
    String sessionID;

    protected void onCreate(Bundle savedInstanceState) {

        /*
        TODO: We don't need a layout for doing this. it will be just a function.
         */
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_logout);
/*

        Button logoutYes = (Button) findViewById(R.id.logoutYesBtn);
        logoutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestLogout()) {
                    Intent intent = new Intent(getApplicationContext(), Main.class);
                    startActivity(intent);
                }
            }
        });

        Button logoutNo = (Button) findViewById(R.id.logoutNoBtn);
        logoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: go back to the previous activity.
            }
        });
*/


    }

    /*private boolean requestLogout(){
        logout = false;


            *//*TODO:delete sessionID

             *//*
            if (*//*deleted*//*)
            logout = true;


        return logout;
    }*/
}
