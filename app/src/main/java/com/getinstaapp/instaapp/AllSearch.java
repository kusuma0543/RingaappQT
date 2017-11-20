package com.getinstaapp.instaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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
import java.util.List;

public class AllSearch extends AppCompatActivity {

    private ProgressDialog dialog;
    private ListView second_listview;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
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
        dialog = new ProgressDialog(this);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");

        second_listview = (ListView) findViewById(R.id.second_listview);
        Intent intent = getIntent();
//        String name = intent.getStringExtra("categoryname");
//        String catiids = intent.getStringExtra("categorysid");
        String sedkeyword = intent.getStringExtra("edkeyword");
       // String mykeyword = intent.getStringExtra("edkeywords");
//String b="words";
//        if (mykeyword==b) {
//            Toast.makeText(getApplicationContext(), "No Services available for your selection", Toast.LENGTH_SHORT).show();
//
            setTitle(sedkeyword);
            String URLLL = "http://quaticstech.in/projecti1andro/android_users_homesearch.php?subcategory_name=" + sedkeyword;
            new kilomilo().execute(URLLL);
//        }
//else{
//        setTitle(name);
//        String URLL = "http://quaticstech.in/projecti1andro/android_users_subcayret.php?cid_categ_sub=" + catiids;
//        new kilomilo().execute(URLL);
//    }



    }
    public class MovieAdap extends ArrayAdapter {
        private List<subcatelist> movieModelList;
        private int resource;
        Context context;
        private LayoutInflater inflater;

        MovieAdap(Context context, int resource, List<subcatelist> objects) {
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

                holder.textone = (TextView) convertView.findViewById(R.id.second_texttitle);
                holder.menuimage = (ImageView)convertView.findViewById(R.id.second_imageview);
                convertView.setTag(holder);
            }//ino
            else {
                holder = (MovieAdap.ViewHolder) convertView.getTag();
            }
            subcatelist ccitacc = movieModelList.get(position);
            holder.textone.setText(ccitacc.getSubcategory_name());

            Picasso.with(context).load(ccitacc.getSubcategory_thumbnail()).fit().error(R.drawable.load).fit().into(holder.menuimage);

            return convertView;
        }

        class ViewHolder {
            public TextView textone;
            private ImageView menuimage;
        }
    }

    public class kilomilo extends AsyncTask<String, String, List<subcatelist>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<subcatelist> doInBackground(String... params) {
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
                List<subcatelist> milokilo = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    subcatelist catego = gson.fromJson(finalObject.toString(), subcatelist.class);
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
        protected void onPostExecute(final List<subcatelist> movieMode) {
            super.onPostExecute(movieMode);
            if (movieMode== null)
            {
                Toast.makeText(getApplicationContext(),"No Services available for your selection", Toast.LENGTH_SHORT).show();

            }
            else
            {
              MovieAdap adapter = new MovieAdap(getApplicationContext(), R.layout.categorytwo, movieMode);
                second_listview.setAdapter(adapter);
                second_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        subcatelist item = movieMode.get(position);
                        Intent intent = new Intent(AllSearch.this,Categories.class);
//                        intent.putExtra("code",item.getScode());




                        startActivity(intent);
                    }
                });
                adapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}

