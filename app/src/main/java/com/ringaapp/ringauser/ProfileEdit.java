package com.ringaapp.ringauser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jetradar.desertplaceholder.DesertPlaceholder;
import com.ringaapp.ringauser.dbhandlers.SQLiteHandler;
import com.ringaapp.ringauser.dbhandlers.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;


public class ProfileEdit extends AppCompatActivity {
    public static final String UPLOAD_KEY = "user_uid";
    public static final String UPLOAD_KEYONE="user_name";
    public static final String UPLOAD_KEYTWO="user_email";
    public static final String UPLOAD_KEYTHREE="user_profile_image";
    public static final String UPLOAD_KEYFOUR="user_gender";

    private String profie_uid,profie_name,profie_email,profie_mobile,profie_address;

    private TextView tv_profileeditmobile,tv_profileeditaddress;
    private EditText et_profileeditname,et_profileeditemail;
    private RadioGroup shome_groupone;
    private RadioButton radio_male;
    String  radiosel,snamesel,semailsel;
    private ImageView profileedit_image;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private Bitmap bitmap;
    String result;
    private SessionManager session;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
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

        if (isConnectedToNetwork()) {
            tv_profileeditmobile = (TextView) findViewById(R.id.profileedit_tvnumber);
            tv_profileeditaddress = (TextView) findViewById(R.id.profileedit_tvaddress);
            et_profileeditname = (EditText) findViewById(R.id.profileedit_tvname);
            et_profileeditemail = (EditText) findViewById(R.id.profileedit_tvemail);
            shome_groupone = (RadioGroup) findViewById(R.id.radiogroup_gender);
            profileedit_image = (ImageView) findViewById(R.id.profileedit_image);


            session = new SessionManager(getApplicationContext());
            db = new SQLiteHandler(getApplicationContext());

            final Intent intent = getIntent();
            profie_name = intent.getStringExtra("profileedit_name");
            profie_email = intent.getStringExtra("profileedit_email");
            profie_mobile = intent.getStringExtra("profileedit_mobile");
            profie_uid = intent.getStringExtra("profileedit_uid");
            profie_address = intent.getStringExtra("profileeditlocation");
            tv_profileeditmobile.setText(profie_mobile);
            tv_profileeditaddress.setText(profie_address);
            Toast.makeText(getApplicationContext(), profie_uid, Toast.LENGTH_SHORT).show();

            profileedit_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFileChooser();
                }
            });

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        else
        {
            setContentView(R.layout.content_ifnointernet);
            DesertPlaceholder desertPlaceholder = (DesertPlaceholder) findViewById(R.id.placeholder_fornointernet);
            desertPlaceholder.setOnButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ProfileEdit.this,Categories.class);
                    startActivity(intent);
                }
            });
        }

    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileedit_image.setImageBitmap(bitmap);





            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
         com.ringaapp.ringauser.RequestHandler rh=new com.ringaapp.ringauser.RequestHandler();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfileEdit.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
               // Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);
              //  Toast.makeText(ProfileEdit.this, radio_male.getText(), Toast.LENGTH_SHORT).show();
                if(uploadImage==null || uploadImage.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please sleect image", Toast.LENGTH_LONG).show();

                }
                else
                {
                    int selectedId = shome_groupone.getCheckedRadioButtonId();
                    radio_male = (RadioButton) findViewById(selectedId);
                    radiosel=radio_male.getText().toString();

                    HashMap<String,String> data = new HashMap<>();
                    data.put(UPLOAD_KEY,profie_uid);
                    data.put(UPLOAD_KEYONE,snamesel);
                    data.put(UPLOAD_KEYTWO,semailsel);
                    data.put(UPLOAD_KEYTHREE,uploadImage);
                    data.put(UPLOAD_KEYFOUR,radiosel);

                  result= rh.sendPostRequest(GlobalUrl.user_profile_edit,data);

                }

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent1=new Intent(ProfileEdit.this,Categories.class);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProfileEdit.this);
        String sharedhomeloc= preferences.getString("user_city", "");
        intent1.putExtra("oneuid",profie_uid);
        intent1.putExtra("user_uname",snamesel);
        intent1.putExtra("profileedit_email",semailsel);
//        intent1.putExtra("updtaedimage", getStringImage(bitmap).getBytes().toString());
        intent1.putExtra("user_city",sharedhomeloc);

        startActivity(intent1);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_check) {
            checkupdate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void checkupdate()
    {

        if (et_profileeditname.getText().toString().equals("") && et_profileeditemail.getText().toString().equals("")&&radio_male.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter fields", Toast.LENGTH_LONG).show();
        }
        else
        {
            snamesel=et_profileeditname.getText().toString();
            semailsel=et_profileeditemail.getText().toString();

            uploadImage();
            Intent intent1=new Intent(ProfileEdit.this,Categories.class);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProfileEdit.this);
            String sharedhomeloc= preferences.getString("user_city", "");
            intent1.putExtra("oneuid",profie_uid);
            intent1.putExtra("user_uname",snamesel);
            intent1.putExtra("profileedit_email",semailsel);
            intent1.putExtra("updtaedimage", getStringImage(bitmap).getBytes().toString());
            intent1.putExtra("user_city",sharedhomeloc);
            //  Toast.makeText(getApplicationContext(), "please relogin to save your changes", Toast.LENGTH_SHORT).show();
            startActivity(intent1);
        }
    }
    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
