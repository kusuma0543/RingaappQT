package com.getinstaapp.instaapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity {
private Button home_butsignin,home_butsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        home_butsignin=(Button) findViewById(R.id.butsingin);
        home_butsignup=(Button) findViewById(R.id.butsignup);
        home_butsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeScreen.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        home_butsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeScreen.this,SignupActivity.class);
                startActivity(intent);
            }
        });

    }
}
