package com.ringaapp.ringauser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
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

public class ProfileActivity extends AppCompatActivity {
private String profi_uid,profi_name,profi_email,profi_mobile,profi_address,profi_image;
private TextView profile_tvusername,profile_tvnumber,profile_tvemail,profile_tvaddress;
    String shareduids;
    ImageView imageuser;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        profile_tvusername=(TextView) findViewById(R.id.profile_tvusername);
        profile_tvnumber=(TextView) findViewById(R.id.profile_tvnumber);
        profile_tvemail=(TextView) findViewById(R.id.profile_tvemail);
        profile_tvaddress=(TextView) findViewById(R.id.profile_tvaddress);
        imageuser=(ImageView) findViewById(R.id.imageuser);

        final Intent intent=getIntent();
        profi_address=intent.getStringExtra("prof_address");
        shareduids= intent.getStringExtra("profile_uid");

        profi_image=intent.getStringExtra("user_uimageprofile");
        profile_tvaddress.setText(profi_address);
        logininto(shareduids);
        Toast.makeText(getApplicationContext(),shareduids,Toast.LENGTH_SHORT).show();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.profile_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent1=new Intent(ProfileActivity.this,ProfileEdit.class);
                intent1.putExtra("profileedit_name",profi_name);
                intent1.putExtra("profileedit_email",profi_email);
                intent1.putExtra("profileedit_mobile",profi_mobile);
                intent1.putExtra("profileedit_uid",profi_uid);
                intent1.putExtra("profileeditlocation",profi_address);

                startActivity(intent1);
            }
        });
    }
    public void logininto(final String sphone1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.users_profiledeth, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");
                   // Toast.makeText(getApplicationContext(),"ko",Toast.LENGTH_SHORT).show();

                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("users_detail");
                        profi_uid=users.getString("user_uid");
                        profi_name = users.getString("user_name");
                        profi_email = users.getString("user_email");
                        profi_mobile = users.getString("user_mobile_number");
                        profi_image=users.getString("user_profile_image");
                        if(profi_image.equals(""))
                        {
                            Picasso.with(context).load(R.drawable.ic_person_black_24dp).fit().error(R.drawable.ic_person_black_24dp).fit().into(imageuser);

                        }
                        else
                        {
                            Picasso.with(context).load(profi_image).fit().error(R.drawable.load).fit().into(imageuser);


                        }

                        profile_tvusername.setText(profi_name);
                        profile_tvemail.setText(profi_email);
                        profile_tvnumber.setText(profi_mobile);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please enter correct number",Toast.LENGTH_SHORT).show();

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

                return insert;

            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }@Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent1=new Intent(ProfileActivity.this,Categories.class);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        String sharedhomeloc= preferences.getString("user_city", "");
        intent1.putExtra("oneuid",profi_uid);
        intent1.putExtra("user_uname",profi_name);

        intent1.putExtra("user_city",sharedhomeloc);

        startActivity(intent1);
        finish();
    }

}
