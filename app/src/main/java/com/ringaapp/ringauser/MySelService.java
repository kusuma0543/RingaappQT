package com.ringaapp.ringauser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class MySelService extends AppCompatActivity {
    String myseruseruid;
    private ListView userserv_listview;
    private ProgressDialog dialog;
    private SessionManager session;
    private SQLiteHandler db;
    TextView normal_text;
    GifImageView underser_gif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sel_service);

        if (isConnectedToNetwork()) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if(getSupportActionBar()!=null)
            {
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
            normal_text=findViewById(R.id.textview_subcat);
            underser_gif=findViewById(R.id.gif_view);
            final HashMap<String, String> user = db.getUserDetails();
            myseruseruid = user.get("uid");

            dialog = new ProgressDialog(this);
            dialog = new ProgressDialog(this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage("Loading. Please wait...");
            userserv_listview = findViewById(R.id.user_selservices);
            String URLL = GlobalUrl.user_myservices + "?user_uid=" + myseruseruid;
            new kilomilo().execute(URLL);
        }
        else
        {
            setContentView(R.layout.content_ifnointernet);
            DesertPlaceholder desertPlaceholder = (DesertPlaceholder) findViewById(R.id.placeholder_fornointernet);
            desertPlaceholder.setOnButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MySelService.this,MySelService.class);
                    startActivity(intent);
                }
            });
        }
    }
    public class MovieAdap extends ArrayAdapter {
        private List<myservices> movieModelList;
        private int resource;
        Context context;
        private LayoutInflater inflater;

        MovieAdap(Context context, int resource, List<myservices> objects) {
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
                holder.textthree = (TextView)convertView.findViewById(R.id.userservice_subcateg);
                holder.textfour = (TextView)convertView.findViewById(R.id.userservice_address);
                holder.textstatus = (TextView)convertView.findViewById(R.id.tvstatus);

                convertView.setTag(holder);
            }//ino
            else {
                holder = (MovieAdap.ViewHolder) convertView.getTag();
            }
            myservices ccitacc = movieModelList.get(position);
            holder.textthree.setText(ccitacc.getService_subcateg_name());
            holder.textone.setText(ccitacc.getPartner_name());
            holder.textstatus.setText(ccitacc.getService_booking_status());
            holder.textstatus.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            String n=ccitacc.getPartner_locality();
            String nn=ccitacc.getPartner_cityname();
            holder.textfour.setText(n+","+nn);
            return convertView;
        }

        class ViewHolder {
            public TextView textone,textthree,textfour,textstatus;

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

            startActivity(new Intent(MySelService.this,Categories.class));
        }


        return true;
    }
    public class kilomilo extends AsyncTask<String, String, List<myservices>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected List<myservices> doInBackground(String... params) {
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
                List<myservices> milokilo = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    myservices catego = gson.fromJson(finalObject.toString(), myservices.class);
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
        protected void onPostExecute(final List<myservices> movieMode) {
            super.onPostExecute(movieMode);
            dialog.dismiss();
            if((movieMode != null) && (movieMode.size()>0) )
            {
                MovieAdap adapter = new MovieAdap(getApplicationContext(), R.layout.myservices, movieMode);
                userserv_listview.setAdapter(adapter);
                userserv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        myservices item = movieMode.get(position);
                        Intent intent = new Intent(MySelService.this,ServiceTracking.class);
                        intent.putExtra("partnerhome_partneruid",item.getPartner_uid());
                        intent.putExtra("partnerhome_useruid",item.getBooking_uid());
                        intent.putExtra("partnerhome_subcateguid",item.getService_subcateg_uid());
                        intent.putExtra("partnerhome_statusjob",item.getService_booking_status());
                        startActivity(intent);
                    }
                });
                adapter.notifyDataSetChanged();


            }
            else
            {
                userserv_listview.setVisibility(View.INVISIBLE);
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
