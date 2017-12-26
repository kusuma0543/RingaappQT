package com.ringaapp.ringauser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

public class Categories extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemSelectedListener {

    private GridView home_gridview;
    private TextView home_tspinner,home_navusername;
    private ProgressDialog dialog;
    private Button homebut_search,homebut_buy;
    public  String sharedhomeloc,hcityName,firstuid,locationuser,user_profilepic,categuid;
    private ImageView nav_head_image;
    RequestQueue rq;
    List<Sliderlist> sliderimg;
    Context context;
    private ListView second_listview;
    FlipperLayout flipperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("INSTA");
        dialog = new ProgressDialog(this);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");

        home_tspinner=(TextView) findViewById(R.id.home_tspinner);
        home_gridview=(GridView) findViewById(R.id.home_gridview);
        homebut_search=(Button) findViewById(R.id.homebut_search);
        homebut_buy=(Button) findViewById(R.id.homebut_buy);
        flipperLayout = (FlipperLayout) findViewById(R.id.flipper_layout);
        second_listview = (ListView) findViewById(R.id.s);
        autocompletesearch();



        String URLL = "http://quaticstech.in/projecti1andro/android_users_slider.php";
        new kilomilo().execute(URLL);
        Intent intent=getIntent();
        firstuid=intent.getStringExtra("oneuid");
        String user_uname=intent.getStringExtra("user_unamehome");
        user_profilepic=intent.getStringExtra("updtaedimage");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedhomeloc= preferences.getString("user_city", "");
        categuid=preferences.getString("useruidentire","");
//        user_profilepic=preferences.getString("sharedprofileimages","");
//        shareduid_home=preferences.getString("shareduidd","");
      sharedhomeloc=intent.getStringExtra("user_city");
        home_tspinner.setText(sharedhomeloc);
        String users_updatedloc_servm="http://quaticstech.in/projecti1andro/android_users_spiner.php?district_place="+sharedhomeloc;
        new JSONTask().execute(users_updatedloc_servm);
        updatelastseen(firstuid);
        Toast.makeText(getApplicationContext(),firstuid,Toast.LENGTH_SHORT).show();

        Toast.makeText(getApplicationContext(),"my id was"+categuid,Toast.LENGTH_SHORT).show();



        rq=Volley.newRequestQueue(this);
        sliderimg=new ArrayList<>();

        homebut_search.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
                Intent intent1=new Intent(Categories.this,AllCatSearch.class);
                intent1.putExtra("sharedhomelocm",sharedhomeloc);
                 startActivity(intent1);

                }
            });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(Categories.this);
        View header=navigationView.getHeaderView(0);

        home_navusername = (TextView)header.findViewById(R.id.nav_username);
        nav_head_image=(ImageView) header.findViewById(R.id.nav_head_image);
        home_navusername.setText(user_uname);

        if(user_profilepic==null)
        {
            Picasso.with(this).load(R.drawable.ic_person_black_24dp).fit().error(R.drawable.ic_person_black_24dp).fit().into(nav_head_image);

        }
        else
        {
           // Picasso.with(this).load(user_profilepic).fit().error(R.drawable.ic_person_black_24dp).fit().into(nav_head_image);

        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String selected = parent.getItemAtPosition(position).toString();
        Toast.makeText(getApplicationContext(), "you selected location is "+selected, Toast.LENGTH_SHORT).show();
        String users_updatedloc_servm="http://quaticstech.in/projecti1andro/android_users_spiner.php?district_place="+locationuser;
        new JSONTask().execute(users_updatedloc_servm);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
          startActivity(new Intent(Categories.this,Categories.class));

        } else if (id == R.id.nav_search) {
            //startActivity(new Intent(Categories.this,AllCategories.class));

        } else if (id == R.id.nav_services) {

        } else if (id == R.id.nav_notifications) {

        } else if (id == R.id.nav_favourites) {

        } else if (id == R.id.nav_call) {


        } else if (id == R.id.nav_chat) {

        } else if (id == R.id.nav_account) {
            Intent intent=new Intent(Categories.this,ProfileActivity.class);
            intent.putExtra("profile_uid",firstuid);
            intent.putExtra("prof_address",sharedhomeloc);
            intent.putExtra("user_uimageprofile",user_profilepic);
            startActivity(intent);

        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","appchitvish@gmail.com", null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            intent.putExtra(Intent.EXTRA_TEXT, "send your feedback here... ");
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));

        }else if (id == R.id.nav_rateus) {

        }else if (id == R.id.nav_share) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "I am happy with this app.Please click the link to download \n https://play.google.com/store/apps/details?id=com.askchitvish.activity.prem&hl=en";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"ASKCHITVISH");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, " This is about service"));


        } else if (id == R.id.nav_whorwe) {
//            startActivity(new Intent(Categories.this,AboutUs.class));

        }else if (id == R.id.nav_logout) {
            Intent intent=new Intent(Categories.this,LoginActivity.class);
            logmeout(firstuid);
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).
                    edit().clear().apply();
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {

            new SweetAlertDialog(Categories.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Are u sure to exit?").setContentText("you wont able to recover ")
                    .setConfirmText("Yes exit").setCancelText("No Dont").showCancelButton(true).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    new SweetAlertDialog(Categories.this,SweetAlertDialog.SUCCESS_TYPE).setContentText("cancled").show();
                    Intent intent=new Intent(Categories.this,Categories.class);
                    startActivity(intent);
                    String users_updatedloc_servm="http://quaticstech.in/projecti1andro/android_users_spiner.php?district_place="+sharedhomeloc;
                    new JSONTask().execute(users_updatedloc_servm);
                }
            }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    Intent intent=new Intent(Categories.this,LoginActivity.class);
                    startActivity(intent);
                    // new SweetAlertDialog(MainActivity.this).setContentText("Exited").show();

                }
            }).show();

        }
    }



    public class JSONTask extends AsyncTask<String,String, List<Categorieslist>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }
        @Override
        protected List<Categorieslist> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("result");
                List<Categorieslist> movieModelList = new ArrayList<>();
                Gson gson = new Gson();
                for(int i=0; i<parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    Categorieslist categorieslist = gson.fromJson(finalObject.toString(), Categorieslist.class);
                    movieModelList.add(categorieslist);

                }
                return movieModelList;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  null;
        }
        @Override
        protected void onPostExecute(final List<Categorieslist> movieModelList) {
            super.onPostExecute(movieModelList);
            dialog.dismiss();
            if(movieModelList != null) {
                MovieAdapter adapter = new MovieAdapter(getApplicationContext(), R.layout.list_categorie, movieModelList);
                home_gridview.setAdapter(adapter);
                home_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Categorieslist categorieslist = movieModelList.get(position);
                      //  SharedPreferences preferences = getSharedPreferences("Preferences", 0);
                     //   SharedPreferences.Editor editor = preferences.edit();
                        Intent intent = new Intent(Categories.this, Second.class);

                        intent.putExtra("categoryname",categorieslist.getService_categ_name());
                         intent.putExtra("categorysid", categorieslist.getService_categ_uid());
                         String tvloc=home_tspinner.getText().toString();
                         intent.putExtra("homeloc",tvloc);

                        startActivity(intent);
                    }
                });

                adapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class MovieAdapter extends ArrayAdapter {
        private List<Categorieslist> movieModelList;
        private int resource;
        Context context;
        private LayoutInflater inflater;
        MovieAdapter(Context context, int resource, List<Categorieslist> objects) {
            super(context, resource, objects);
            movieModelList = objects;
            this.context =context;
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
            final ViewHolder holder  ;
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
                holder = new ViewHolder();
                holder.menuimage = (ImageView)convertView.findViewById(R.id.homeiv_catimg);
                holder.menuname=(TextView) convertView.findViewById(R.id.hometv_catname);



                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            Categorieslist categorieslist= movieModelList.get(position);
            Picasso.with(context).load(categorieslist.getService_categ_fullimage()).fit().error(R.drawable.load).fit().into(holder.menuimage);
            holder.menuname.setText(categorieslist.getService_categ_name());



            return convertView;
        }
        class ViewHolder{
            private ImageView menuimage;
            private TextView menuname;
        }
    }



    public void logmeout(final String s1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.user_logoutmode, new Response.Listener<String>() {
            public void onResponse(String response) {

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            { }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_uid",s1);


                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    public void updatelastseen(final String s1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.user_updatelastseen, new Response.Listener<String>() {
            public void onResponse(String response) {

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            { }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_uid",s1);


                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

public void autocompletesearch()
{
    PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
            getFragmentManager().findFragmentById(R.id.place_autocomplete_fragments);
    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
            .setCountry("IN")
            .build();

    autocompleteFragment.setFilter(typeFilter);
    autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
        @Override
        public void onPlaceSelected(Place place) {
           LatLng latilongi=place.getLatLng();
           Double lath=latilongi.latitude;
           Double longh=latilongi.longitude;
        Toast.makeText(getApplicationContext(), "Place: " + place.getLatLng(), Toast.LENGTH_SHORT).show();
            Geocoder geocoder = new Geocoder(Categories.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lath, longh, 1);

                hcityName = addresses.get(0).getLocality();
                String users_updatedloc_serv="http://quaticstech.in/projecti1andro/android_users_spiner.php?district_place="+hcityName;
                new JSONTask().execute(users_updatedloc_serv);



            } catch (IOException e) {
                e.printStackTrace();
            }

            home_tspinner.setText(hcityName);


        }

        @Override
        public void onError(Status status) {

            Toast.makeText(getApplicationContext(), "An error occurred: " + status, Toast.LENGTH_SHORT).show();

        }
    });
}
    public class MovieAdap extends ArrayAdapter {
        private List<Bannerlist> movieModelList;
        private int resource;
        Context context;
        private LayoutInflater inflater;

        MovieAdap(Context context, int resource, List<Bannerlist> objects) {
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
              // holder.nmn = (ImageView)convertView.findViewById(R.id.nmn);
                convertView.setTag(holder);
            }//ino
            else {
                holder = (MovieAdap.ViewHolder) convertView.getTag();
            }
            final Bannerlist ccitacc = movieModelList.get(position);
            holder.textone.setText(ccitacc.getPromotion_title());

            FlipperView view = new FlipperView(getBaseContext());
         //Picasso.with(context).load(ccitacc.getPromotion_fullimage()).fit().error(R.drawable.load).fit().into(flipperLayout);
            view.setImageUrl(ccitacc.getPromotion_thumbnail());
            flipperLayout.addFlipperView(view);
                    view.setOnFlipperClickListener(new FlipperView.OnFlipperClickListener() {
                        @Override
                        public void onFlipperClick(FlipperView flipperView) {
                            Toast.makeText(Categories.this
                                    , ccitacc.getPromotion_title()
                                    , Toast.LENGTH_SHORT).show();
                        }
                    });

            return convertView;
        }

        class ViewHolder {
            public TextView textone;


        }
    }

    public class kilomilo extends AsyncTask<String, String, List<Bannerlist>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Bannerlist> doInBackground(String... params) {
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
                List<Bannerlist> milokilo = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    Bannerlist catego = gson.fromJson(finalObject.toString(), Bannerlist.class);
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
        protected void onPostExecute(final List<Bannerlist> movieMode) {
            super.onPostExecute(movieMode);
            if (movieMode== null)
            {
                Toast.makeText(getApplicationContext(),"No Services available for your selection", Toast.LENGTH_SHORT).show();

            }
            else
            {
                MovieAdap adapter = new MovieAdap(getApplicationContext(), R.layout.categorytwo, movieMode);
                second_listview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }
}