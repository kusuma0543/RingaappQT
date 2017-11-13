package com.getinstaapp.instaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {
private EditText edforgotpswd;
    private Button butforgotpswd_otp;
    private String sforgot_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        edforgotpswd=(EditText) findViewById(R.id.edforgotpswd);
        butforgotpswd_otp=(Button) findViewById(R.id.butforgorpswd_otp);
        edforgotpswd.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    edforgotpswd.setTextColor(Color.RED);

                    edforgotpswd.setBackgroundResource( R.drawable.edittext_afterseslect);
                }
            }
        });
        butforgotpswd_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sforgot_mobile=edforgotpswd.getText().toString();
                forgot_updateotp(sforgot_mobile);
                Intent intent=new Intent(ForgotPassword.this,OTPVerify.class);
                String fromforgot="fromforgot";
                intent.putExtra("fromforgot",fromforgot);
               intent.putExtra("mobile_number",sforgot_mobile);

                startActivity(intent);
            }
        });
    }



    public void forgot_updateotp(final String s1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.user_forgot, new Response.Listener<String>() {
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
                params.put("mobile_number", s1);



                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

}
