package com.ringaapp.ringauser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.ringaapp.ringauser.dbhandlers.SQLiteHandler;
import com.ringaapp.ringauser.dbhandlers.SessionManager;
import com.squareup.picasso.Picasso;
import com.thefinestartist.finestwebview.FinestWebView;

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

import pl.droidsonroids.gif.GifImageView;
import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;



public class Categories extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Double lath_cat, lng_cat;
    private GridView home_gridview;
    private TextView home_tspinner, home_navusername;
    private ProgressDialog dialog;
    private CardView homebut_search;
         FloatingActionButton homebut_buy;
    public String sharedhomeloc, hcityName, firstuid, categuid, name_nav;
    private ImageView search_image;
    RequestQueue rq;
    List<Sliderlist> sliderimg;
    Context context;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private ListView second_listview;
    FlipperLayout flipperLayout;
    private SessionManager session;
    private SQLiteHandler db;
    public String shrotadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialog = new ProgressDialog(this);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");
        dialog.show();

        if (isConnectedToNetwork()) {

            home_tspinner = (TextView) findViewById(R.id.home_tspinner);
            home_gridview = (GridView) findViewById(R.id.home_gridview);
            homebut_search = findViewById(R.id.hello_card);
            homebut_buy = findViewById(R.id.postad_partner);
            search_image = findViewById(R.id.search_image_map);
            flipperLayout = (FlipperLayout) findViewById(R.id.flipper_layout);
            second_listview = (ListView) findViewById(R.id.s);
            RelativeLayout relativeLayout = findViewById(R.id.home_screen_conti);
            session = new SessionManager(getApplicationContext());
            db = new SQLiteHandler(getApplicationContext());

            final HashMap<String, String> user = db.getUserDetails();
            sharedhomeloc = user.get("user_city");
            categuid = user.get("uid");
            name_nav = user.get("name");
            home_tspinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                                .setCountry("IN")
                                .build();

                        Intent intent =
                                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter)
                                        .build(Categories.this);
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    } catch (GooglePlayServicesRepairableException e) {
                        // TODO: Handle the error.
                    } catch (GooglePlayServicesNotAvailableException e) {
                        // TODO: Handle the error.
                    }
                }
            });
            homebut_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callmetopostadd();
                    //pdovpod

                }
            });
            search_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                                .setCountry("IN")
                                .build();

                        Intent intent =
                                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter)
                                        .build(Categories.this);
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    } catch (GooglePlayServicesRepairableException e) {
                        // TODO: Handle the error.
                    } catch (GooglePlayServicesNotAvailableException e) {
                        // TODO: Handle the error.
                    }
                }
            });


            home_tspinner.setText(sharedhomeloc);
            String slider_image = "http://quaticstech.in/projecti1andro/android_users_slider.php";
            new kilomi().execute(slider_image);
            String users_updatedloc_servm = "http://quaticstech.in/projecti1andro/android_users_spiner.php";
            new JSONTask().execute(users_updatedloc_servm);
            updatelastseen(categuid);
            rq = Volley.newRequestQueue(this);
            sliderimg = new ArrayList<>();
            homebut_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Categories.this, AllCatSearch.class);
                    intent1.putExtra("sharedhomelocm", sharedhomeloc);
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
            View header = navigationView.getHeaderView(0);

            home_navusername = (TextView) header.findViewById(R.id.nav_username);

            home_navusername.setText(name_nav);
//
//        if(user_profilepic==null)
//        {
//           // Picasso.with(this).load(R.drawable.ic_person_black_24dp).fit().error(R.drawable.ic_person_black_24dp).fit().into(nav_head_image);
//            Glide.with(context)
//                    .load(R.drawable.ic_person_black_24dp)
//                    .error(R.drawable.ic_person_black_24dp)
//                    .fitCenter()
//                    .into(nav_head_image);
//
//        }
//        else
//        {
//           // Picasso.with(this).load(user_profilepic).fit().error(R.drawable.ic_person_black_24dp).fit().into(nav_head_image);
//
//        }

        } else {

            Intent i = new Intent(Categories.this, Categories.class);
            startActivity(i);
            finish();
        }
    }


    public void callmetopostadd()
    {
        new SweetAlertDialog(Categories.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Hurray! Be a Partner").setContentText("To Post an AD and Service you need to be an RingaApp Partner")
                .setConfirmText("Yes.Let Me in!").setCancelText("Cancel").showCancelButton(true).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();

            }
        }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.whatsapp"));
                startActivity(intent);
            }
        }).show();

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if (id == R.id.nav_search) {
            Intent intent1=new Intent(Categories.this,AllCatSearch.class);
            intent1.putExtra("sharedhomelocm",sharedhomeloc);
            startActivity(intent1);

        } else if (id == R.id.nav_services) {
            startActivity(new Intent(Categories.this,MySelService.class));

        }  else if (id == R.id.nav_favourites) {
           startActivity(new Intent(Categories.this,MyFavs.class));

        } else if (id == R.id.nav_support) {
           Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                   "mailto","ringaapp@gmail.com", null));
           intent.putExtra(Intent.EXTRA_SUBJECT, "Support");
           intent.putExtra(Intent.EXTRA_TEXT, "How can we help you... ");
           startActivity(Intent.createChooser(intent, "Choose an Email client :"));

        }  else if (id == R.id.nav_account) {
            Intent intent=new Intent(Categories.this,ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","ringaapp@gmail.com", null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            intent.putExtra(Intent.EXTRA_TEXT, "send your feedback here... ");
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));

        }
        else if (id == R.id.nav_rateus) {

        }
       else if (id == R.id.nav_postad) {
        callmetopostadd();
       }
        else if (id == R.id.nav_share) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "I am happy with this app.Please click the link to download \n https://play.google.com/store/apps/details?id=com.askchitvish.activity.prem&hl=en";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"RINGAAPP");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, " This is about service"));


        }

        else if (id == R.id.nav_whorwe) {
//            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
//                    "mailto","ringaapp@gmail.com", null));
//            intent.putExtra(Intent.EXTRA_SUBJECT, "Support");
//            intent.putExtra(Intent.EXTRA_TEXT, "How can we help you... ");
//            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
startActivity( new Intent(Categories.this,AboutScroll.class));

        }
        else if (id == R.id.nav_logout) {
            Intent intent=new Intent(Categories.this,LoginActivity.class);
            logmeout(firstuid);
            session.setLogin(false);
            db.deleteUsers();
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

            new SweetAlertDialog(Categories.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Are you sure to Exit?").setContentText("You wont be able to recover ")
                    .setConfirmText("Yes exit").setCancelText("No Dont").showCancelButton(true).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();

                }
            }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }).show();

        }
    }



    public class JSONTask extends AsyncTask<String,String, List<Categorieslist>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                LatLng latilongi=place.getLatLng();
                lath_cat=latilongi.latitude;
                lng_cat=latilongi.longitude;
                Toast.makeText(getApplicationContext(), "Place: " + place.getLatLng(), Toast.LENGTH_SHORT).show();
                Geocoder geocoder = new Geocoder(Categories.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(lath_cat, lng_cat, 1);
                    hcityName = addresses.get(0).getLocality();
                     shrotadd=addresses.get(0).getSubLocality();
                    String users_updatedloc_serv="http://quaticstech.in/projecti1andro/android_users_spiner.php";
                    new JSONTask().execute(users_updatedloc_serv);

                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                home_tspinner.setText(shrotadd+","+hcityName);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.


            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
    public class kilomi extends AsyncTask<String, String, List<Bannerlist>> {
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
                JSONArray parentArray = parentObject.getJSONArray("resul");
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
            if (movieMode!= null) {
                MovieAdap imageslider_adapter = new MovieAdap(getApplicationContext(), R.layout.slider_layout, movieMode);
                second_listview.setAdapter(imageslider_adapter);
                imageslider_adapter.notifyDataSetChanged();
            }
            else
            {
                Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();

            }


        }
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
                holder.promo_res= convertView.findViewById(R.id.res_promo);
              // holder.nmn = (ImageView)convertView.findViewById(R.id.nmn);
                convertView.setTag(holder);
            }//ino
            else {
                holder = (MovieAdap.ViewHolder) convertView.getTag();
            }
            final Bannerlist ccitacc = movieModelList.get(position);
            holder.textone.setText(ccitacc.getPromotion_title());
holder.promo_res.setText(ccitacc.getPromotion_res());
       // Picasso.with(context).load(ccitacc.getPromotion_fullimage()).fit().error(R.drawable.load).fit().into((Target) flipperLayout);
            holder.glpview.setImageUrl(ccitacc.getPromotion_thumbnail());


            holder.glpview.setOnFlipperClickListener(new FlipperView.OnFlipperClickListener() {
                        @Override
                        public void onFlipperClick(FlipperView flipperView) {
if (ccitacc.getPromotion_res().equals("web"))
{
    new FinestWebView.Builder(Categories.this)
            .titleDefault("RingaApp")
            .showUrl(false)
            .dividerHeight(0)
            .toolbarScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS)
            .gradientDivider(false)
            .titleColorRes(R.color.finestWhite)
            .urlColorRes(R.color.finestWhite)
            .statusBarColorRes(R.color.colorPrimaryDark)
            .toolbarColorRes(R.color.colorPrimary)
            .iconPressedColorRes(R.color.colorPrimary)
            .progressBarColorRes(R.color.colorPrimary)
            .backPressToClose(false)
            .gradientDivider(false)
            .setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit, R.anim.activity_close_enter, R.anim.activity_close_exit)
            .show(ccitacc.getPromotion_title());
}

else if(ccitacc.getPromotion_res().equals("youtube"))
{
    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ccitacc.getPromotion_title())));

}
else
{
    Intent intent1=new Intent(Categories.this,AllCatSearch.class);
    intent1.putExtra("sharedhomelocm",sharedhomeloc);
    startActivity(intent1);

}

                         //   new FinestWebView.Builder(Categories.this).show(ccitacc.getPromotion_title());
                        }
                    });

            flipperLayout.addFlipperView(holder.glpview);

            return convertView;
        }

        class ViewHolder {
            public TextView textone;
public  TextView promo_res;
            FlipperView glpview =new FlipperView(getBaseContext());;

        }
    }


    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
