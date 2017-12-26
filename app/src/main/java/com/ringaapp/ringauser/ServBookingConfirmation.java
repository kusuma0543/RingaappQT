package com.ringaapp.ringauser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.concurrent.TimeUnit;

public class ServBookingConfirmation extends AppCompatActivity {
    private ImageView test_image;
    public TextView booking_countdown;
    private Button alldirecttohome_but;
    CountDownTimer booking_c;
    private static final String FORMAT = "%02d:%02d";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serv_booking_confirmation);
        Toolbar toolbar =  findViewById(R.id.toolbar);
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
       final String  confirmcity= preferences.getString("user_city", "");
       final String  confirmuid=preferences.getString("useruidentire","");
        test_image=findViewById(R.id.test_image);
        booking_countdown=findViewById(R.id.booking_countdown);
        alldirecttohome_but=findViewById(R.id.directto_home);
        booking_c= new CountDownTimer(50000,1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                booking_countdown.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }


            public void onFinish() {
                booking_c.cancel();
                Toast.makeText(ServBookingConfirmation.this, "Sorry Selected Service Provided did'nt accepted ur service", Toast.LENGTH_SHORT).show();
                booking_c.cancel();
               Intent intent1=new Intent(ServBookingConfirmation.this,Categories.class);

                intent1.putExtra("oneuid",confirmuid);

                intent1.putExtra("user_city", confirmcity);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ServBookingConfirmation.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("useruidentire",confirmuid);
                editor.putString("user_city", confirmcity);

                startActivity(intent1);
            }

        }.start();

        Glide.with(this).load(R.drawable.booking_gif).
               into(test_image);
        alldirecttohome_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(ServBookingConfirmation.this,Categories.class);

                intent2.putExtra("oneuid",confirmuid);

                intent2.putExtra("user_city", confirmcity);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ServBookingConfirmation.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("useruidentire",confirmuid);
                editor.putString("user_city", confirmcity);

                startActivity(intent2);
            }
        });
    }
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
