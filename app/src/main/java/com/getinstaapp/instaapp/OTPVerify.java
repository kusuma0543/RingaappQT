package com.getinstaapp.instaapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import swarajsaaj.smscodereader.interfaces.OTPListener;
import swarajsaaj.smscodereader.receivers.OtpReader;

public class OTPVerify extends AppCompatActivity implements View.OnFocusChangeListener,View.OnClickListener,View.OnKeyListener,TextWatcher, OTPListener {
    private EditText mPinFirstDigitEditText;
    private EditText mPinSecondDigitEditText;
    private EditText mPinThirdDigitEditText;
    private EditText mPinForthDigitEditText;
    private EditText mPinHiddenEditText;
    private Button butotp_verify;
    private TextView tvotp_mobile;
    String value;
    CountDownTimer bb;
    String fromforgot,last_number;
    private static final String FORMAT = "%02d:%02d";
    int seconds , minutes;

    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);
        Intent intent=getIntent();
       // pref=getApplication().getSharedPreferences("Options", MODE_PRIVATE);

        last_number=intent.getStringExtra("mobile_number");
        fromforgot=intent.getStringExtra("fromforgot");

       // int_mobile=pref.getString("mobile_number", "");


        mPinFirstDigitEditText = (EditText) findViewById(R.id.pinone);
        mPinSecondDigitEditText = (EditText) findViewById(R.id.pintwo);
        mPinThirdDigitEditText = (EditText) findViewById(R.id.pinthree);
        mPinForthDigitEditText = (EditText) findViewById(R.id.pinfour);
        mPinHiddenEditText = (EditText) findViewById(R.id.pin_hidden_edittext);
        tvotp_mobile=(TextView) findViewById(R.id.tvotp_mobile);
       // int_mobile=intent.getStringExtra("mobile_number");
        tvotp_mobile.setText(last_number);


        final TextView countdown = (TextView) findViewById(R.id.countdown);
        butotp_verify=(Button) findViewById(R.id.butotp_verify);



      bb= new CountDownTimer(50000,1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                countdown.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }


            public void onFinish() {
                bb.cancel();
                Toast.makeText(OTPVerify.this, "Please click Resend to get OTP", Toast.LENGTH_SHORT).show();
              //  Intent intent=new Intent(OTPVerify.this,LoginActivity.class);

               // startActivity(intent);
                finish();


            }

        }.start();




        setPINListeners();

        butotp_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

String s1=mPinFirstDigitEditText.getText().toString().trim();
                String s2=mPinSecondDigitEditText.getText().toString().trim();
                String s3=mPinThirdDigitEditText.getText().toString().trim();
                String s4=mPinForthDigitEditText.getText().toString().trim();
                String s=s1+s2+s3+s4;
                otp_check(last_number,s);
//                if(s.equals(otp) || s.equals(value))
//                {
//                    Intent intent=new Intent(OTPVerify.this,Categories.class);
//                    startActivity(intent);
//                finish();
//
//                }
//                else
//                {
//                    Toast.makeText(OTPVerify.this,"Please enter valid OTP",Toast.LENGTH_LONG).show();
//                }

            }
        });
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        final int id = v.getId();
        switch (id) {
            case R.id.pinone:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                 showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pintwo:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                   showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pinthree:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pinfour:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;


            default:
                break;
        }
    }
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case R.id.pin_hidden_edittext:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {

                        if (mPinHiddenEditText.getText().length() == 4)
                            mPinForthDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 3)
                            mPinThirdDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 2)
                            mPinSecondDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 1)
                            mPinFirstDigitEditText.setText("");

                        if (mPinHiddenEditText.length() > 0) {
                            mPinHiddenEditText.setText(mPinHiddenEditText.getText().subSequence(0, mPinHiddenEditText.length() - 1));
                            mPinHiddenEditText.post(new Runnable() {
                                @Override
                                public void run() {
                                    mPinHiddenEditText.setSelection(mPinHiddenEditText.getText().toString().length());
                                }
                            });
                        }

                        break;
                    }

                default:
                    return false;
            }
        }

        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setDefaultPinBackground(mPinFirstDigitEditText);
        setDefaultPinBackground(mPinSecondDigitEditText);
        setDefaultPinBackground(mPinThirdDigitEditText);
        setDefaultPinBackground(mPinForthDigitEditText);

        if (s.length() == 0) {
            setFocusedPinBackground(mPinFirstDigitEditText);
            mPinFirstDigitEditText.setText("");
        } else if (s.length() == 1) {
            setFocusedPinBackground(mPinSecondDigitEditText);
            mPinFirstDigitEditText.setText(s.charAt(0) + "");
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");

        } else if (s.length() == 2) {
            setFocusedPinBackground(mPinThirdDigitEditText);
            mPinSecondDigitEditText.setText(s.charAt(1) + "");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");

        } else if (s.length() == 3) {
            setFocusedPinBackground(mPinForthDigitEditText);
            mPinThirdDigitEditText.setText(s.charAt(2) + "");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 4) {
            mPinForthDigitEditText.setText(s.charAt(3) + "");
        }
    }
    private void setDefaultPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(R.drawable.ggg));
    }

    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }
    private void setFocusedPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(R.drawable.ggg));
    }

    private void setPINListeners() {
        mPinHiddenEditText.addTextChangedListener(this);

        mPinFirstDigitEditText.setOnFocusChangeListener(this);
        mPinSecondDigitEditText.setOnFocusChangeListener(this);
        mPinThirdDigitEditText.setOnFocusChangeListener(this);
        mPinForthDigitEditText.setOnFocusChangeListener(this);

        mPinFirstDigitEditText.setOnKeyListener(this);
        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText.setOnKeyListener(this);
        mPinHiddenEditText.setOnKeyListener(this);
    }
    @SuppressWarnings("deprecation")
    public void setViewBackground(View view, Drawable background) {
        if (view == null || background == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }
    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    @Override
    public void otpReceived(String messageText) {

    }


    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.butotp_verify:
                break;
        }

    }
    public void otp_check(final String sphone1,final String sphone2) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.user_otpreverify, new Response.Listener<String>() {
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

                       String uname2 = users.getString("otp_identification");

    Intent intent=new Intent(OTPVerify.this,Categories.class);
                        intent.putExtra("mobile_number",uname1);
    intent.putExtra("otp_identification",uname2);
    if (fromforgot.equals("fromforgot"))
    {
        Intent intent1=new Intent(OTPVerify.this,ResetPassword.class);
        intent.putExtra("mobile_number",uname1);
        startActivity(intent1);
    }
    else
    {
        startActivity(intent);
        finish();
    }




//                        intent.putExtra("jijii",uname4);
//                        intent.putExtra("gkkk",uname5);
//
////                        SharedPreferences pref = PreferenceManager
////                                .getDefaultSharedPreferences(LoginActivity.this);
////                        SharedPreferences.Editor editor = pref.edit();
////                        editor.putString ("uid", uname3);
////                        editor.putString ("uname", uname5);
////                        editor.commit();
//


                        //   Toast.makeText(getApplicationContext(),mobile_number,Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please enter correct otp",Toast.LENGTH_SHORT).show();

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
                insert.put("mobile_number",sphone1);
                insert.put("otp_identification", sphone2);

                return insert;

            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }


}
