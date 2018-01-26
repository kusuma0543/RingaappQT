package com.ringaapp.ringauser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jetradar.desertplaceholder.DesertPlaceholder;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.ringaapp.ringauser.dbhandlers.SQLiteHandler;
import com.ringaapp.ringauser.dbhandlers.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ServBookingConfirmation extends AppCompatActivity {

    public TextView booking_countdown;
    private Button alldirecttohome_but;
    CountDownTimer booking_c;
    String userbookingid;
    private static final String FORMAT = "%02d:%02d";
    final Handler handler = new Handler();
     String  confirmcity,confirmuid;
    private final int FIVE_SECONDS = 5000;
    private SessionManager session;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serv_booking_confirmation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (isConnectedToNetwork()) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            confirmcity = preferences.getString("user_city", "");
            confirmuid = preferences.getString("useruidentire", "");
            userbookingid = preferences.getString("userbookidentire", "");
            //Toast.makeText(this, userbookingid, Toast.LENGTH_SHORT).show();

            booking_countdown = findViewById(R.id.booking_countdown);
            alldirecttohome_but = findViewById(R.id.directto_home);

            session = new SessionManager(getApplicationContext());
            db = new SQLiteHandler(getApplicationContext());

            scheduleSendLocation();

//        timer = new CountDownTimer(10000, 10) {
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//            }
//
//            @Override
//            public void onFinish() {
//                try{
//                    checkingbookstatus(userbookingid);
//                }catch(Exception e){
//                    Log.e("Error", "Error: " + e.toString());
//                }
//            }
//        }.start();


            booking_c = new CountDownTimer(250000, 1000) { // adjust the milli seconds here
                public void onTick(long millisUntilFinished) {
                    booking_countdown.setText("" + String.format(FORMAT,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }


                public void onFinish() {
                    booking_c.cancel();
                    handler.removeCallbacksAndMessages(null);
                    new SweetAlertDialog(ServBookingConfirmation.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Sorry! Unable to reach Partner").setContentText("Press OK to find other partners")
                            .setConfirmText("OK").showCancelButton(true).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Intent intent1 = new Intent(ServBookingConfirmation.this, Categories.class);

                            intent1.putExtra("oneuid", confirmuid);

                            intent1.putExtra("user_city", confirmcity);

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ServBookingConfirmation.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("useruidentire", confirmuid);
                            editor.putString("user_city", confirmcity);

                            startActivity(intent1);
                            finish();

                        }
                    }).show();
                }

            }.start();


            alldirecttohome_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(ServBookingConfirmation.this, Categories.class);

                    intent2.putExtra("oneuid", confirmuid);

                    intent2.putExtra("user_city", confirmcity);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ServBookingConfirmation.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("useruidentire", confirmuid);
                    editor.putString("user_city", confirmcity);

                    startActivity(intent2);
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
                    Intent intent=new Intent(ServBookingConfirmation.this,Categories.class);
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_about_scroll, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_ringa) {

            startActivity(new Intent(ServBookingConfirmation.this,Categories.class));
        }


        return true;
    }
    public void scheduleSendLocation() {

        handler.postDelayed(new Runnable() {
            public void run() {
                checkingbookstatus(userbookingid)  ;        // this method will contain your almost-finished HTTP calls
                handler.postDelayed(this, FIVE_SECONDS);
            }
        }, FIVE_SECONDS);
    }
    public void onBackPressed()
    {
        super.onBackPressed();

    }
    public void checkingbookstatus(final String ss1) {
        String CHECKURL= GlobalUrl.user_getbookingconfirmation+"?booking_uid="+userbookingid;
        //Toast.makeText(this, userbookingid, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CHECKURL, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");

                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("users_detail");
                        String bookingstatus = users.getString("service_partner_accepted");
                        if(bookingstatus.matches("0"))
                        {
                           // Toast.makeText(ServBookingConfirmation.this, "Waiting for Response", Toast.LENGTH_SHORT).show();
                           // handler.removeCallbacksAndMessages(null);

                        }
                      else if(bookingstatus.matches("1"))
                        {
                            Toast.makeText(ServBookingConfirmation.this, "Accepted", Toast.LENGTH_SHORT).show();
                            handler.removeCallbacksAndMessages(null);
                            booking_c.cancel();

                            new SweetAlertDialog(ServBookingConfirmation.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Hurry Your Service is Accepted").setContentText("You can able to track the Service Provider in My Services page")
                                    .setConfirmText("OK").showCancelButton(true).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent intent1=new Intent(ServBookingConfirmation.this,Categories.class);

                                    intent1.putExtra("oneuid",confirmuid);

                                    intent1.putExtra("user_city", confirmcity);

                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ServBookingConfirmation.this);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("useruidentire",confirmuid);
                                    editor.putString("user_city", confirmcity);

                                    startActivity(intent1);
                                    finish();

                                }
                            }).show();


                        }
                      else if(bookingstatus.matches("2"))
                        {
                            handler.removeCallbacksAndMessages(null);
                            booking_c.cancel();

                            new SweetAlertDialog(ServBookingConfirmation.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Sorry! Unable to reach Partner").setContentText("Press OK to find other partners")
                                    .setConfirmText("OK").showCancelButton(true).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent intent1=new Intent(ServBookingConfirmation.this,Categories.class);

                                    intent1.putExtra("oneuid",confirmuid);

                                    intent1.putExtra("user_city", confirmcity);

                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ServBookingConfirmation.this);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("useruidentire",confirmuid);
                                    editor.putString("user_city", confirmcity);

                                    startActivity(intent1);
                                    finish();

                                }
                            }).show();
                        }
                        else
                        {
                            handler.removeCallbacksAndMessages(null);
                            booking_c.cancel();

                            new SweetAlertDialog(ServBookingConfirmation.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Sorry! for Inconvenience").setContentText("Please try again later")
                                    .setConfirmText("OK").showCancelButton(true).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent intent1=new Intent(ServBookingConfirmation.this,Categories.class);

                                    intent1.putExtra("oneuid",confirmuid);

                                    intent1.putExtra("user_city", confirmcity);

                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ServBookingConfirmation.this);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("useruidentire",confirmuid);
                                    editor.putString("user_city", confirmcity);

                                    startActivity(intent1);
                                    finish();

                                }
                            }).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please wait for confirmation!",Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            { }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("booking_uid", ss1);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
