package com.ringaapp.ringauser;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;

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

import pl.droidsonroids.gif.GifImageView;

public class AllCatSearch extends AppCompatActivity  {
   ProgressDialog dialog;
    private GridView second_listview;
    SharedPreferences preferences;
    private EditText second_edittext;
String homeloca;

TextView normal_text;
TextView normal_texts;
GifImageView underser_gif;

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

        normal_text=findViewById(R.id.textview_subcat);
        underser_gif=findViewById(R.id.gif_view);

        if (isConnectedToNetwork()) {
            Intent intent = getIntent();
            homeloca = intent.getStringExtra("sharedhomelocm");


            dialog = new ProgressDialog(this);
            dialog = new ProgressDialog(this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage("Loading. Please wait...");
            dialog.show();
            second_listview = (GridView) findViewById(R.id.second_listview);
            second_edittext = (EditText) findViewById(R.id.edsubcat_search);
            second_edittext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(second_edittext.getWindowToken(), 0);
                        return true;
                    }
                    return false;
                }
            });

            second_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        String getsearch = second_edittext.getText().toString();
                        String URLLL = GlobalUrl.users_allcatsearchtwo + "?district_place=" + homeloca + "&searchitem=" + getsearch;

                        new kilomilo().execute(URLLL);
                    }
                    return false;
                }
            });
            second_edittext.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void afterTextChanged(Editable mEdit)
                {
                    String getsearch = mEdit.toString();
                    String URLLL = GlobalUrl.users_allcatsearchtwo + "?district_place=" + homeloca + "&searchitem=" + getsearch;

                    new kilomilo().execute(URLLL);
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after){}

                public void onTextChanged(CharSequence s, int start, int before, int count){


                }

            });
           String URLL = GlobalUrl.users_allcatsearch + "?district_place=" + homeloca;
            //String URLL = GlobalUrl.users_allcatsearchtwo + "?district_place=" + homeloca + "&searchitem=" + "ssasdssaadsa";
            new kilomilo().execute(URLL);
        }
        else {

            Intent i = new Intent(AllCatSearch.this, Categories.class);
            startActivity(i);
            finish();
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

            startActivity(new Intent(AllCatSearch.this,Categories.class));
        }


        return true;
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

                holder.textone =  convertView.findViewById(R.id.second_texttitle);
                holder.menuimage =  convertView.findViewById(R.id.second_imageview);
                convertView.setTag(holder);
            }//ino
            else {
                holder = (MovieAdap.ViewHolder) convertView.getTag();
            }
            subcatelist ccitacc = movieModelList.get(position);
            holder.textone.setText(ccitacc.getService_subcateg_name());

           Picasso.with(context).load(ccitacc.getService_subcateg_fullimage()).fit().into(holder.menuimage);

            return convertView;
        }

        class ViewHolder {
            public TextView textone;
            private ImageView menuimage;
        }
    }

    @SuppressLint("StaticFieldLeak")
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
            dialog.dismiss();
            if((movieMode != null) && (movieMode.size()>0) ){

                MovieAdap adapter= new MovieAdap(getApplicationContext(), R.layout.categorytwo, movieMode);
                second_listview.setAdapter(adapter);
                second_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        subcatelist item = movieMode.get(position);
                        Intent intent = new Intent(AllCatSearch.this, MapsActivity.class);
                        intent.putExtra("categid",item.getService_categ_uid());
                        intent.putExtra("subcategid",item.getService_subcateg_uid());
                        intent.putExtra("subcategname",item.getService_subcateg_name());


                        startActivity(intent);
                    }
                });
                adapter.notifyDataSetChanged();

            }

            else {

                second_listview.setVisibility(View.INVISIBLE);
                underser_gif.setVisibility(View.VISIBLE);
                normal_text.setVisibility(View.VISIBLE);

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}