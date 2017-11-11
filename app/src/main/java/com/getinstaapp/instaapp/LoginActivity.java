package com.getinstaapp.instaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ObbInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{
    private Button butlogin_login;
    private EditText edlogin_mobile,edlogin_pswd;
    private TextView tvlogin_forgot,tvlogin_singnup;
private String sphone,spassword;
    private LinearLayout prof_section;
    private SignInButton signin;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE=9001;
    CatLoadingView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        prof_section=(LinearLayout) findViewById(R.id.prof_section);
        signin=(SignInButton) findViewById(R.id.butlogin_glogin);

        butlogin_login=(Button) findViewById(R.id.butlogin_login);
        edlogin_mobile=(EditText)findViewById(R.id.edlogin_num);
        edlogin_pswd=(EditText) findViewById(R.id.edlogin_pswd);
        tvlogin_forgot=(TextView) findViewById(R.id.tvlogin_forgot);
        tvlogin_singnup=(TextView) findViewById(R.id.tvlogin_signup);
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
                    edlogin_pswd.setTextColor(Color.BLACK);

                }

            }
        });
        edlogin_pswd.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    edlogin_pswd.setTextColor(Color.RED);

                    edlogin_pswd.setBackgroundResource( R.drawable.edittext_afterseslect);
                    edlogin_mobile.setTextColor(Color.BLACK);
                }
            }
        });

        GoogleSignInOptions signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();
        signin.setOnClickListener(this);
        butlogin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edlogin_mobile.getText().toString().equals("") && edlogin_pswd.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter fields", Toast.LENGTH_LONG).show();

                }
                else
                {

                    if (isConnectedToNetwork()) {
                        sphone = edlogin_mobile.getText().toString();
                        spassword = edlogin_pswd.getText().toString();
                        mView = new CatLoadingView();

                        mView.show(getSupportFragmentManager(), "");

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
        switch (view.getId())
        {
            case R.id.butlogin_glogin:
                signin();
                break;



        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void signin()
    {
        Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);

    }
    private void handleResult(GoogleSignInResult result)
    {

        if(result.isSuccess())
        {
            GoogleSignInAccount account=result. getSignInAccount();
            String name=account.getDisplayName();
            String email=account.getEmail();
            setContentView(R.layout.google_data);
            TextView one=(TextView) findViewById(R.id.one);
            TextView second=(TextView) findViewById(R.id.seon);

            one.setText(name);//setting name and email to textviews
            second.setText(email);
            updateUI(true);

        }
        else {
            updateUI(false);
        }
    }
    private void updateUI(boolean isLogin)
    {
        if(isLogin)
        {
            prof_section.setVisibility(View.VISIBLE);
            signin.setVisibility(View.GONE);
        }
        else
        {
            prof_section.setVisibility(View.GONE);
            signin.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        if(requestCode==REQ_CODE)
        {
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
    public void logininto(final String sphone1,final String sphone2) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.user_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");
                    System.out.print(abc);
                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("user_det");
                        String uname1 = users.getString("mobile_number");

                        Intent intent=new Intent(LoginActivity.this,OTPVerify.class);
                   intent.putExtra("mobile_number",uname1);


                        startActivity(intent);

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
                insert.put("mobile_number", sphone1);
                insert.put("password", sphone2);
                return insert;

            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
}
