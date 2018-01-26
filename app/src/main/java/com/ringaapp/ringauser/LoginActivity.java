package com.ringaapp.ringauser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ringaapp.ringauser.dbhandlers.SQLiteHandler;
import com.ringaapp.ringauser.dbhandlers.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button butlogin_login;
    private EditText edlogin_mobile,edlogin_pswd;
    private TextView tvlogin_forgot,tvlogin_singnup;
private String sphone,spassword;
    private Boolean isClicked = false;
    private  TextView show_pass;

    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        butlogin_login=(Button) findViewById(R.id.butlogin_login);
        edlogin_mobile=(EditText)findViewById(R.id.edlogin_num);
        edlogin_pswd=(EditText) findViewById(R.id.edlogin_pswd);
        tvlogin_forgot=(TextView) findViewById(R.id.tvlogin_forgot);
        tvlogin_singnup=(TextView) findViewById(R.id.tvlogin_signup);
        show_pass= findViewById(R.id.show_password);
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, Categories.class);
            startActivity(intent);
        }
        show_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClicked = isClicked ? false : true;
                if (isClicked) {
                    show_pass.setText("Hide");
                    edlogin_pswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else {
                    show_pass.setText("Show");
                    edlogin_pswd.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });
            tvlogin_forgot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
                }
            });
        edlogin_mobile.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    edlogin_mobile.setTextColor(Color.RED);

                    edlogin_mobile.setBackgroundResource( R.drawable.edittext_afterseslect);
                    edlogin_pswd.setBackgroundResource(R.drawable.rounded_edittextred);

                    edlogin_pswd.setTextColor(Color.BLACK);

                }

            }
        });
        edlogin_pswd.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    edlogin_pswd.setTextColor(Color.RED);

                    edlogin_pswd.setBackgroundResource( R.drawable.edittext_afterseslect);
                    edlogin_mobile.setBackgroundResource(R.drawable.rounded_edittextred);

                    edlogin_mobile.setTextColor(Color.BLACK);
                }
            }
        });


        butlogin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (edlogin_mobile.getText().toString().equals("") && edlogin_pswd.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter all fields", Toast.LENGTH_LONG).show();

                }
                else
                {

                    if (isConnectedToNetwork()) {
                        sphone = edlogin_mobile.getText().toString();
                        spassword = edlogin_pswd.getText().toString();

                        logininto(sphone,spassword);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please check your Internet", Toast.LENGTH_LONG).show();

                    }

 
                }
            }
        });
        tvlogin_singnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

    }
    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onClick(View view) {


    }



    public void logininto(final String sphone1,final String sphone2) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.user_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");

                    if (abc)
                    {

                        JSONObject users = jObj.getJSONObject("user_det");
                        String uname1 = users.getString("user_mobile_number");

                        Intent intent=new Intent(LoginActivity.this,OTPVerify.class);
                         intent.putExtra("mobile_number",uname1);


                        startActivity(intent);

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
                insert.put("user_mobile_number", sphone1);
                insert.put("user_password", sphone2);
                return insert;

            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
}
