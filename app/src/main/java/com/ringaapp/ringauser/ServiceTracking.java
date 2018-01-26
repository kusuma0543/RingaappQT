package com.ringaapp.ringauser;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jetradar.desertplaceholder.DesertPlaceholder;
import com.ringaapp.ringauser.dbhandlers.SQLiteHandler;
import com.ringaapp.ringauser.dbhandlers.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ServiceTracking extends AppCompatActivity {
    private RatingBar ratingBar;
    private ImageView imageongoing,imagestart,imagedone;
    private String textpartneruid,textuseruid,texsubcateg,textstatusjob,gettrackrating,gettrackfeedback,users_uid;
    String fav_rid;
    private SessionManager session;
    private SQLiteHandler db;
    private Button submitbut;
    private EditText trackfeedback;
    private TextView textfeed,textrating;
    Float dbrate;
    ImageView click_tofacv;
    String favon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_tracking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Service Tracking");

        imageongoing = findViewById(R.id.tracking_ongoing);
        imagestart = findViewById(R.id.tracking_start);
        submitbut = findViewById(R.id.submitbut);
        ratingBar = findViewById(R.id.ratingBar1);
        trackfeedback = findViewById(R.id.track_feedback);
        textfeed = findViewById(R.id.textfeed);
        textrating = findViewById(R.id.textrating);
        click_tofacv=findViewById(R.id.click_tofav);
        try
        {
            getfav(fav_rid);
        }catch (Exception e)
        {

        }


        if (isConnectedToNetwork()) {
            session = new SessionManager(getApplicationContext());
            db = new SQLiteHandler(getApplicationContext());
            final HashMap<String, String> user = db.getUserDetails();

            users_uid = user.get("uid");
            imagedone = findViewById(R.id.tracking_done);
            Intent intent1 = getIntent();
            textpartneruid = intent1.getStringExtra("partnerhome_partneruid");
            textuseruid = intent1.getStringExtra("partnerhome_useruid");
            texsubcateg = intent1.getStringExtra("partnerhome_subcateguid");
            textstatusjob = intent1.getStringExtra("partnerhome_statusjob");


            final boolean[] showingF = {true};
try
{if (favon.equals("Y"))
{
    click_tofacv.setImageResource(R.drawable.ic_favorite_black_24dp);
    showingF[0]=false;
}
else
{
    click_tofacv.setImageResource(R.drawable.ic_favorite_border_black_24dp);
    showingF[0]=true;
}

}catch (Exception e)
{

}

            if (textstatusjob.matches("In Progress")) {
                imageongoing.setVisibility(View.INVISIBLE);
                imagedone.setVisibility(View.INVISIBLE);

                imagestart.setVisibility(View.VISIBLE);


            } else if (textstatusjob.matches("Done")) {
                ratinggiven(textpartneruid, textuseruid);
                click_tofacv.setVisibility(View.VISIBLE);


            }
            click_tofacv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(showingF[0] == true)
                    {
                        insertuserfav(users_uid,textuseruid);
                        click_tofacv.setImageResource(R.drawable.ic_favorite_black_24dp);
                        showingF[0] = false;

                    }
                    else{
                      delfav(fav_rid);
                        showingF[0] = true;
                        click_tofacv.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                    }

                }
            });

            submitbut.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (trackfeedback.getText().toString().equals("")) {
                        Toast.makeText(ServiceTracking.this, "Please enter Feedback", Toast.LENGTH_SHORT).show();
                    } else {
                        String feedback = trackfeedback.getText().toString();
                        String rating = String.valueOf(ratingBar.getRating());
                        insertratingdet(textpartneruid, textuseruid, texsubcateg, rating, feedback, users_uid);
                        onBackPressed();
                    }
                }

            });
        }
        else
        {
            setContentView(R.layout.content_ifnointernet);
            DesertPlaceholder desertPlaceholder = (DesertPlaceholder) findViewById(R.id.placeholder_fornointernet);
            desertPlaceholder.setOnButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ServiceTracking.this,MySelService.class);
                    startActivity(intent);
                }
            });
        }

    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.categories, menu);
//        if (textstatusjob.matches("In Progress")) {
//
//
//
//        } else if (textstatusjob.matches("Done")) {
//
//            MenuItem item = menu.findItem(R.id.action_fav);
//            item.setVisible(true);

//
//            final boolean[] showingF = {true};
//
//            if (favon.equals("Y"))
//            {
//                favori.setImageResource(R.drawable.like);
//                showingF[0]=false;
//            }
//            else
//            {
//                favori.setImageResource(R.drawable.unlike);
//                showingF[0]=true;
//            }


//        }
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.action_fav) {
//
//
//
//            if(showingF[0] == true)
//            {
//                insertuserfav(users_uid,textuseruid);
//                favori.setImageResource(R.drawable.like);
//                showingF[0] = false;
//
//            }
//            else{
//                favdel(uid,sidin);
//                showingF[0] = true;
//                favori.setImageResource(R.drawable.unlike);
//
//            }
//
//
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    public void insertratingdet(final String sphone1,final String sphone2,final String sphone3,final String sphone4,final String sphone5,final String sphone6) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.user_rating, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> insert = new HashMap<String, String>();
                insert.put("partner_uid", sphone1);
                insert.put("user_booking_uid", sphone2);
                insert.put("service_subcateg_uid", sphone3);
                insert.put("user_ratings", sphone4);
                insert.put("user_feedback", sphone5);
                insert.put("user_uid", sphone6);
                return insert;

            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
    public void ratinggiven(final String sphone1,final String sphone2) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.user_givenrating, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");

                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("users_detail");
                        gettrackrating= users.getString("user_ratings");
                        gettrackfeedback=users.getString("user_feedback");


                        visibilitymethod();

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please check number and Password",Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> insert = new HashMap<String, String>();
                insert.put("partner_uid", sphone1);
                insert.put("user_booking_uid", sphone2);

                return insert;

            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
    public void visibilitymethod()
    {
        if(gettrackfeedback.matches("no"))
        {
            imageongoing.setVisibility(View.INVISIBLE);
            imagestart.setVisibility(View.INVISIBLE);
            imagedone.setVisibility(View.VISIBLE);
            textfeed.setVisibility(View.VISIBLE);
            trackfeedback.setVisibility(View.VISIBLE);
            textrating.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);
            submitbut.setVisibility(View.VISIBLE);
        }
        else
        {
            dbrate = Float.parseFloat(gettrackrating);

            imageongoing.setVisibility(View.INVISIBLE);
            imagestart.setVisibility(View.INVISIBLE);
            imagedone.setVisibility(View.VISIBLE);
            textfeed.setVisibility(View.VISIBLE);
            trackfeedback.setVisibility(View.VISIBLE);
            textrating.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);

            ratingBar.setClickable(false);
            ratingBar.setFocusable(false);
            ratingBar.setFocusableInTouchMode(false);

            trackfeedback.setFocusable(false);
            trackfeedback.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            trackfeedback.setClickable(false);

            trackfeedback.setText(gettrackfeedback);
            ratingBar.setRating(dbrate);

        }
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

    }
    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    public void insertuserfav(final String sphone1,final String sphone2) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.user_favadd, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");

                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("users_detail");
                       fav_rid= users.getString("fid");
                       favon=users.getString("favon");
                        if (favon==null|| favon.isEmpty())
                        {
                            favon="N";
                        }



                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please check number and Password",Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> insert = new HashMap<String, String>();
                insert.put("user_uid", sphone1);
                insert.put("user_booking_uid", sphone2);

                return insert;

            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
    public void delfav(final String sphone1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.user_favdel, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> insert = new HashMap<String, String>();
                insert.put("fid", sphone1);


                return insert;

            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
    public void getfav(final String sphone1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.user_favdel, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");

                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("users_detail");
                        fav_rid= users.getString("fid");
                        favon=users.getString("favon");
                        if (favon==null|| favon.isEmpty())
                        {
                            favon="N";
                        }



                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please check number and Password",Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> insert = new HashMap<String, String>();
                insert.put("fid", sphone1);


                return insert;

            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
}
