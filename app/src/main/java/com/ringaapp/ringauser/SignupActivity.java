package com.ringaapp.ringauser;

import android.app.ProgressDialog;
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
import android.widget.CheckBox;
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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText edsignup_name,edsignup_mobile,edsignup_mail,edsignup_pswd;
    private String sname,semail,smobile,spassword;
    private String emailInput,emailPattern;
    private TextView tvsignup_tc,tvsignup_signin;
    private Button butsingnup_signup;
    private CheckBox checkbox;
private  TextView show_pass;
    private SessionManager session;
    private SQLiteHandler db;
 ProgressDialog dialog;

    private Boolean isClicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        edsignup_name=(EditText) findViewById(R.id.edsignup_name);
        edsignup_mail=(EditText) findViewById(R.id.edsignup_mail);
        edsignup_mobile=(EditText) findViewById(R.id.edsignup_mobile);
        edsignup_pswd=(EditText) findViewById(R.id.edsignup_pswd);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        checkbox=(CheckBox) findViewById(R.id.checkBox);
        tvsignup_tc=(TextView) findViewById(R.id.tvsignup_tc);
        butsingnup_signup=(Button) findViewById(R.id.butsignup_signup);

        tvsignup_signin=(TextView) findViewById(R.id.tvsignup_signin);

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        butsingnup_signup.setOnClickListener(this);
        show_pass= findViewById(R.id.show_password);

        show_pass.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        isClicked = isClicked ? false : true;
        if (isClicked) {
            show_pass.setText("Hide");
            edsignup_pswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        } else {
            show_pass.setText("Show");
            edsignup_pswd.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }
});
        edsignup_name.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    edsignup_name.setTextColor(Color.RED);
                    edsignup_name.setBackgroundResource( R.drawable.edittext_afterseslect);
                    edsignup_mail.setBackgroundResource(R.drawable.rounded_edittextred);
                    edsignup_mobile.setBackgroundResource(R.drawable.rounded_edittextred);
                    edsignup_pswd.setBackgroundResource(R.drawable.rounded_edittextred);

                    edsignup_mail.setTextColor(Color.BLACK);
                    edsignup_mobile.setTextColor(Color.BLACK);
                    edsignup_pswd.setTextColor(Color.BLACK);
                }
            }
        });
        edsignup_mail.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    edsignup_mail.setTextColor(Color.RED);
                    edsignup_mail.setBackgroundResource( R.drawable.edittext_afterseslect);
                    edsignup_name.setBackgroundResource(R.drawable.rounded_edittextred);
                    edsignup_mobile.setBackgroundResource(R.drawable.rounded_edittextred);
                    edsignup_pswd.setBackgroundResource(R.drawable.rounded_edittextred);

                    edsignup_name.setTextColor(Color.BLACK);
                    edsignup_mobile.setTextColor(Color.BLACK);
                    edsignup_pswd.setTextColor(Color.BLACK);
                }
            }
        });
        edsignup_mobile.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    edsignup_mobile.setTextColor(Color.RED);
                    edsignup_mobile.setBackgroundResource( R.drawable.edittext_afterseslect);
                    edsignup_mail.setBackgroundResource(R.drawable.rounded_edittextred);
                    edsignup_name.setBackgroundResource(R.drawable.rounded_edittextred);
                    edsignup_pswd.setBackgroundResource(R.drawable.rounded_edittextred);

                    edsignup_mail.setTextColor(Color.BLACK);
                    edsignup_name.setTextColor(Color.BLACK);
                    edsignup_pswd.setTextColor(Color.BLACK);
                }
            }
        });
        edsignup_pswd.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    edsignup_pswd.setTextColor(Color.RED);
                    edsignup_pswd.setBackgroundResource( R.drawable.edittext_afterseslect);
                    edsignup_mail.setBackgroundResource(R.drawable.rounded_edittextred);
                    edsignup_mobile.setBackgroundResource(R.drawable.rounded_edittextred);
                    edsignup_name.setBackgroundResource(R.drawable.rounded_edittextred);

                    edsignup_mail.setTextColor(Color.BLACK);
                    edsignup_mobile.setTextColor(Color.BLACK);

                    edsignup_name.setTextColor(Color.BLACK);

                }
            }
        });
        tvsignup_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        tvsignup_tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, TermsAndConditions.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {

            case R.id.butsignup_signup:

                dialog = new ProgressDialog(this);
                dialog = new ProgressDialog(this);
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.setMessage("Loading. Please wait...");
                dialog.show();
                nsignin();

                break;



        }

    }

    private void nsignin() {
        emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        emailInput = edsignup_mail.getText().toString().trim();
        if (edsignup_mail.getText().toString().equals("") && edsignup_mobile.getText().toString().equals("")) {
            dialog.dismiss();

            Toast.makeText(getApplicationContext(), "Please Enter All fields", Toast.LENGTH_LONG).show();
        }
        else
        {
            dialog.dismiss();
            if (edsignup_name.getText().toString().equals(""))
            {
                dialog.dismiss();

                Toast.makeText(SignupActivity.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();

            }
            else {
                if (emailInput.matches(emailPattern))
                {



                    if (isConnectedToNetwork()) {
                        if (edsignup_mobile.length() == 10) {
                            if (checkbox.isChecked()) {
                                if (edsignup_pswd.getText().toString().equals(""))
                                {
                                    Toast.makeText(getApplicationContext(), "Please Enter Valid Password",
                                            Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                else
                                {

                                    sname = edsignup_name.getText().toString();
                                    semail = edsignup_mail.getText().toString();
                                    smobile = edsignup_mobile.getText().toString();
                                    spassword = edsignup_pswd.getText().toString();
                                    insertme(sname, semail, smobile, spassword);

                                    Intent intent = new Intent(SignupActivity.this, OTPVerify.class);
                                    intent.putExtra("mobile_number",smobile);


                                    startActivity(intent);
                                }

                            } else {
                                Toast.makeText(SignupActivity.this, "Please check the Terms & Comditions m", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Enter Valid Mobile Number",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    } else {
                        Toast.makeText(SignupActivity.this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Please Invalid email address",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        }


    }
    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    public void insertme(final String s1, final String s2,final String s3,final String s4) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.user_signup, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");


                    if (abc)
                    {
                        dialog.dismiss();
                        JSONObject users = jObj.getJSONObject("users_detail");
                       String uname1 = users.getString("user_mobile_number");



                        Intent intent=new Intent(SignupActivity.this,OTPVerify.class);
                        intent.putExtra("mobile_number",uname1);


                        startActivity(intent);


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
                params.put("user_name", s1);
                params.put("user_email", s2);
                params.put("user_mobile_number",s3);
                params.put("user_password",s4);


                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}

