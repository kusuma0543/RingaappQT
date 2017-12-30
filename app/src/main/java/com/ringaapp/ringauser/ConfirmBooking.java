package com.ringaapp.ringauser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmBooking extends AppCompatActivity {
    private TextView tvconfirm_partnername,tvconfirm_partneraddress,tvconfirm_partnerbudgetfeatures;
    String sconfirm_partnerid,sconfirm_partnername,sconfirm_partneraddress,sconfirm_partnerbudget,sconfirm_partnerfeatures;
    private Button serprov_confirmbookbut;
    private EditText description_book;
    String sdescription_book;
    String  userid_book,partnerid_book,categid_book,subcateg_book,alladdress_book,alllatitude_book,alllongitude_book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Confirm Booking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setOnClickListener( new View.OnClickListener() {
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        description_book=findViewById(R.id.confirm_partnerdescription);
        serprov_confirmbookbut=findViewById(R.id.serprov_confirmbookbut);
        tvconfirm_partnername=findViewById(R.id.confirm_partnername);
        tvconfirm_partneraddress=findViewById(R.id.confirm_partneraddress);
        tvconfirm_partnerbudgetfeatures=findViewById(R.id.confirm_partnerbudgetfeatures);


        Intent intent1=getIntent();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userid_book=preferences.getString("useruidentire","");
        partnerid_book =preferences.getString("confirm_bookingid","");
        insertmybbokingdetails(partnerid_book);
        categid_book=preferences.getString("sel_categid","");
         subcateg_book=preferences.getString("sel_subcategid","");
         alladdress_book=preferences.getString("usersseladdressfull","");
         alllatitude_book=preferences.getString("usersellatitude","");
          alllongitude_book=preferences.getString("usersellongitude","");
        Toast.makeText(getApplicationContext(),"my address was"+partnerid_book,Toast.LENGTH_SHORT).show();



        serprov_confirmbookbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(description_book.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"please enter description",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    sdescription_book=description_book.getText().toString();
                    Intent intent1=new Intent(ConfirmBooking.this,ServBookingConfirmation.class);
                   insertmes(userid_book,partnerid_book,categid_book,subcateg_book,sdescription_book,alladdress_book,alllatitude_book,alllongitude_book);

                    startActivity(intent1);
                }

            }
        });
    }
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
    public void insertmybbokingdetails(final String s1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.user_bookingdetret, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");


                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("users_detail");
                     sconfirm_partnername = users.getString("partner_name");

                        sconfirm_partneraddress= users.getString("partner_address");
                        sconfirm_partnerbudget= users.getString("partner_budget");
                        sconfirm_partnerfeatures= users.getString("partner_features");
                        tvconfirm_partnername.setText(sconfirm_partnername);
                       tvconfirm_partneraddress.setText(sconfirm_partneraddress);
                       tvconfirm_partnerbudgetfeatures.setText(sconfirm_partnerbudget+sconfirm_partnerfeatures);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Account already exists!",Toast.LENGTH_SHORT).show();

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
                params.put("partner_uid", s1);



                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    public void insertmes(final String ss1, final String ss2,final String ss3,final String ss4,final String ss5, final String ss6,final String ss7,final String ss8) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.user_booking, new Response.Listener<String>() {
            public void onResponse(String response) {

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            { }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_uid", ss1);
                params.put("partner_uid", ss2);
                params.put("service_categ_uid",ss3);
                params.put("service_subcateg_uid",ss4);
                params.put("service_booking_description", ss5);
                params.put("service_booking_address", ss6);
                params.put("service_latitude",ss7);
                params.put("service_longitude",ss8);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

}
