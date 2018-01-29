package com.ringaapp.ringauser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jetradar.desertplaceholder.DesertPlaceholder;
import com.ringaapp.ringauser.dbhandlers.SQLiteHandler;
import com.ringaapp.ringauser.dbhandlers.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class MyFavs extends AppCompatActivity {
    String myseruseruid;
    private ListView user_myfavslist;
    private ProgressDialog dialog;
    private SessionManager session;
    private SQLiteHandler db;
    TextView normal_text;
    GifImageView underser_gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favs);

        if (isConnectedToNetwork()) {
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
            session = new SessionManager(getApplicationContext());
            db = new SQLiteHandler(getApplicationContext());
            normal_text = findViewById(R.id.textview_subcat);
            underser_gif = findViewById(R.id.gif_view);
            final HashMap<String, String> user = db.getUserDetails();
            myseruseruid = user.get("uid");

            dialog = new ProgressDialog(this);
            dialog = new ProgressDialog(this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage("Loading. Please wait...");
            user_myfavslist = findViewById(R.id.user_myfavlist);
            String URLL = GlobalUrl.user_showfav + "?user_uid=" + myseruseruid;
            new kilomilo().execute(URLL);
        }
        else {
            setContentView(R.layout.content_ifnointernet);
            DesertPlaceholder desertPlaceholder = (DesertPlaceholder) findViewById(R.id.placeholder_fornointernet);
            desertPlaceholder.setOnButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyFavs.this, MyFavs.class);
                    startActivity(intent);
                }
            });
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_about_scroll, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_ringa) {

            startActivity(new Intent(MyFavs.this,Categories.class));
        }


        return true;
    }
    public class MovieAdap extends ArrayAdapter {
        private List<showfav> movieModelList;
        private int resource;
        Context context;
        private LayoutInflater inflater;

        MovieAdap(Context context, int resource, List<showfav> objects) {
            super(context, resource, objects);
            movieModelList = objects;
            this.context = context;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final MovieAdap.ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(resource, null);
                holder = new MovieAdap.ViewHolder();

                holder.textone = (TextView) convertView.findViewById(R.id.userservice_name);
                holder.textthree = (TextView) convertView.findViewById(R.id.userservice_subcateg);
                holder.textfour = (TextView) convertView.findViewById(R.id.userservice_address);
                holder.textstatus = (TextView) convertView.findViewById(R.id.tvstatus);
                holder.text_acceptedbefore = (TextView) convertView.findViewById(R.id.myservices_acceptedbefore);
                holder.text_getmobile_number = (TextView) convertView.findViewById(R.id.getmobilenumber);
                holder.but_call = convertView.findViewById(R.id.partneraccrej_callbut);

                convertView.setTag(holder);
            }//ino
            else {
                holder = (MovieAdap.ViewHolder) convertView.getTag();
            }
            final showfav ccitacc = movieModelList.get(position);
            holder.textthree.setText(ccitacc.getService_subcateg_name());
            holder.textone.setText(ccitacc.getPartner_name());
            holder.textstatus.setText(ccitacc.getService_booking_status());
            holder.textstatus.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

          //  holder.textfour.setText(n + "," + nn);
            holder.text_getmobile_number.setText(ccitacc.getPartner_mobilenumber());

            long days;
            String strThatDay = ccitacc.getService_booking_createddate();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date d = null;
            try {
                d = formatter.parse(strThatDay);//catch exception
            } catch (ParseException e) {

                e.printStackTrace();
            }
            Calendar thatDay = Calendar.getInstance();
            thatDay.setTime(d);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = df.format(c.getTime());
            long diff = c.getTimeInMillis() - thatDay.getTimeInMillis(); //result in millis
            days = diff / (24 * 60 * 60 * 1000);
            holder.text_acceptedbefore.setText("accepted before " + days + " days");
            holder.but_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String take_user_number = holder.text_getmobile_number.getText().toString();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + take_user_number));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (ActivityCompat.checkSelfPermission(MyFavs.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    startActivity(callIntent);
                }
            });


            return convertView;
        }

        class ViewHolder {
            public TextView textone,textthree,textfour,textstatus,text_acceptedbefore,text_getmobile_number;
            public Button but_call;

        }
    }
    public class kilomilo extends AsyncTask<String, String, List<showfav>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected List<showfav> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("result");
                List<showfav> milokilo = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    showfav catego = gson.fromJson(finalObject.toString(), showfav.class);
                    milokilo.add(catego);
                }
                return milokilo;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<showfav> movieMode) {
            super.onPostExecute(movieMode);
            dialog.dismiss();
            if((movieMode != null) && (movieMode.size()>0) )
            {
                MovieAdap adapter = new MovieAdap(getApplicationContext(), R.layout.myservices, movieMode);
                user_myfavslist.setAdapter(adapter);
                user_myfavslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        showfav item = movieMode.get(position);
//                        Intent intent = new Intent(MyFavs.this,MapsActivity.class);
//                        intent.putExtra("categid",item.getService_categ_uid());
//                        intent.putExtra("subcategid",item.getService_subcateg_uid());
//                        intent.putExtra("subcategname",item.getService_subcateg_name());
//                        intent.putExtra("categoryname_user",item.getService_categ_name());
//                        startActivity(intent);



                        Intent intentb=new Intent(MyFavs.this,ServiceProviderDetails.class);
                        intentb.putExtra("selservice_providerid",item.getPartner_uid());
                        intentb.putExtra("selservice_providername",item.getPartner_name());
                        intentb.putExtra("visitingcahrgeofpartner",item.getPartner_budget());
                        intentb.putExtra("totalrating",item.getUser_ratings());
                        intentb.putExtra("partner_lastseendate",item.getService_booking_createddate());
                        intentb.putExtra("partner_ratingcount",item.getUser_rating_count());

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyFavs.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("sel_subcategid",item.getService_subcateg_uid());
                        editor.putString("sel_categid",item.getService_categ_uid());
                        editor.putString("sel_subcategnameuser",item.getService_subcateg_name());
                        editor.putString("categoryname_user",item.getService_categ_name());
                        editor.apply();
                        startActivity(intentb);

                    }
                });

                adapter.notifyDataSetChanged();


            }
            else
            {
                user_myfavslist.setVisibility(View.INVISIBLE);
                underser_gif.setVisibility(View.VISIBLE);
                normal_text.setVisibility(View.VISIBLE);

            }
        }
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
