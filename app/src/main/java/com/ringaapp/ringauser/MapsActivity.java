package com.ringaapp.ringauser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.jetradar.desertplaceholder.DesertPlaceholder;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.ringaapp.ringauser.dbhandlers.SQLiteHandler;
import com.ringaapp.ringauser.dbhandlers.SessionManager;
import com.roger.catloadinglibrary.CatLoadingView;

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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener {
String mappagelat,mappagelng,mappageloc;
    private GoogleMap mMap;
    private TrackGps gps;
    int PERMISSION_ALL = 1;
    Double lat,lng;
    ListView list29;
    String sel_subcategid,selcategid;
    String user_bookingid;
    String  userid_book,alladdress_book,alllatitude_book,alllongitude_book,sforpartnerid,sforpartnername;
    SharedPreferences preferences;
    private SessionManager session;
    private SQLiteHandler db;
    String sel_subcategname;
    CatLoadingView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (isConnectedToNetwork()) {

            Intent intent=getIntent();
             sel_subcategname=intent.getStringExtra("subcategname");
            selcategid=intent.getStringExtra("categid");
            sel_subcategid=intent.getStringExtra("subcategid");
            preferences = PreferenceManager.getDefaultSharedPreferences(MapsActivity.this);

            mView = new CatLoadingView();
            mView.show(getSupportFragmentManager(), "");
            list29=findViewById(R.id.map_listviewone);


        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);


        String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        }
        else
        {
            setContentView(R.layout.content_ifnointernet);
            DesertPlaceholder desertPlaceholder = (DesertPlaceholder) findViewById(R.id.placeholder_fornointernet);
            desertPlaceholder.setOnButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MapsActivity.this,Categories.class);
                    startActivity(intent);
                }
            });
        }


    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public class MovieAdap extends ArrayAdapter {

        private List<GeoLocate> movieModelList;
        private int resource;
        Context context;
        private LayoutInflater inflater;
        MovieAdap(Context context, int resource, List<GeoLocate> objects) {
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
        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if(view == null){
                view = inflater.inflate(resource,null);
                holder = new ViewHolder();
                holder.textid= view.findViewById(R.id.mappage_sername);
                holder.textname= view.findViewById(R.id.mappage_seradd);
              holder.textvisiting=view.findViewById(R.id.mappage_sericename);
                holder.butviewpart=view.findViewById(R.id.butviewpart);
                holder.butbookpart=view.findViewById(R.id.butbookpart);
                holder.forpartnerid=view.findViewById(R.id.forpartnerid);
                holder.forpartnername=view.findViewById(R.id.forpartnername);
                holder.text_substype=view.findViewById(R.id.textsubs_type);
               // holder.textcount=view.findViewById(R.id.mappage_count);
                holder.ratingBar=view.findViewById(R.id.mappage_rating);
                view.setTag(holder);
            }
            else {
                holder = (ViewHolder) view.getTag();
            }
            GeoLocate ccitac=movieModelList.get(position);
            holder.textid.setText(ccitac.getPartner_name());
            holder.textname.setText(ccitac.getPartner_locality());
           holder.textvisiting.setText(sel_subcategname);
            holder.forpartnerid.setText(ccitac.getPartner_uid());
            holder.forpartnername.setText(ccitac.getPartner_name());
            String checksubtype=ccitac.getPartner_budget();
//            if(checksubtype.matches("Gold"))
//            {
//                    holder.text_substype.setBackground(getDrawable(R.drawable.goldbut));
//                    holder.text_substype.setTextColor(getColor(R.color.colorAccent));
//            }
//            else if(checksubtype.matches("Silver"))
//            {
//                holder.text_substype.setBackground(getDrawable(R.drawable.silverbut));
//                holder.text_substype.setTextColor(Color.WHITE);
//            }
//            else if(checksubtype.matches("Diamond"))
//            {
//                holder.text_substype.setBackground(getDrawable(R.drawable.diamondbut));
//                holder.text_substype.setTextColor(Color.WHITE);
//            }
//            else if(checksubtype.matches("Free"))
//            {
//                holder.text_substype.setBackground(getDrawable(R.drawable.edittext_afterseslect));
//                holder.text_substype.setTextColor(getColor(R.color.colorAccent));
//            }
            if(checksubtype.matches(".*\\d+.*"))
            {
                holder.text_substype.setBackground(getDrawable(R.drawable.edittext_afterseslect));
                holder.text_substype.setText("Rs "+ccitac.getPartner_budget());
                holder.text_substype.setTextColor(getColor(R.color.colorAccent));
            }
            else
            {
                holder.text_substype.setText(ccitac.getPartner_budget());

            }
            Float ratemap= Float.parseFloat(ccitac.getUser_ratings());
            holder.ratingBar.setRating(ratemap);
           // holder.textcount.setText(ccitac.getUser_ratings());
            holder.butviewpart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sforpartnerid=holder.forpartnerid.getText().toString();
                    String sforpartnername=holder.forpartnername.getText().toString();
                    Intent intentb=new Intent(MapsActivity.this,ServiceProviderDetails.class);
                    intentb.putExtra("selservice_providerid",sforpartnerid);
                    intentb.putExtra("selservice_providername",sforpartnername);


                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MapsActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("sel_subcategid",sel_subcategid);
                    editor.putString("sel_categid",selcategid);
                    editor.apply();
                    startActivity(intentb);

                }
            });
            holder.butbookpart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


//                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MapsActivity.this);
//                     userid_book=preferences.getString("useruidentire","");
//                     alladdress_book=preferences.getString("usersseladdressfull","");
//                     alllatitude_book=preferences.getString("usersellatitude","");
//                    alllongitude_book=preferences.getString("usersellongitude","");


                    new SweetAlertDialog(MapsActivity.this, SweetAlertDialog.NORMAL_TYPE).setTitleText("Confirm Your Booking?")

                            .setConfirmText("Confirm").setCancelText("Back").
                            showCancelButton(true).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();

                        }
                    }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sforpartnerid=holder.forpartnerid.getText().toString();
                            sforpartnername=holder.forpartnername.getText().toString();
                            final HashMap<String, String> user = db.getUserDetails();
                            userid_book=user.get("uid");
                            alladdress_book=user.get("user_address");
                            alllatitude_book=user.get("user_latitude");
                            alllongitude_book=user.get("user_longitude");

                            insertmes(userid_book,sforpartnerid,selcategid,sel_subcategid,alladdress_book,alllatitude_book,alllongitude_book);
                        }
                    }).show();


                }
            });



            lat=Double.parseDouble(ccitac.getPartner_latitude());
            lng=Double.parseDouble(ccitac.getPartner_longitude());

            mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng))
                    .title(ccitac.getPartner_name())
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.destloc)));



            PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                    getFragmentManager().findFragmentById(R.id.place_autocomplete_fragmentsk);
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setCountry("IN")
                    .build();

            autocompleteFragment.setFilter(typeFilter);
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {

                    // Toast.makeText(getApplicationContext(), "Place: " + place.getName(), Toast.LENGTH_SHORT).show();

                    // Toast.makeText(getApplicationContext(), "Place: " + place.getLatLng(), Toast.LENGTH_SHORT).show();
                    mMap.clear();
                    LatLng sydneys = place.getLatLng();
                    Double dlat=sydneys.latitude;
                    Double dlng=sydneys.longitude;
                    new kilomilo().execute(GlobalUrl.user_mapdetails+"?partner_latitude="+dlat+"&partner_longitude="+dlng+"&subcategid="+sel_subcategid);

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydneys, 6.5f));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(12.5f), 2000, null);
                    mMap.addMarker(new MarkerOptions()
                            .position(sydneys).icon(BitmapDescriptorFactory.fromResource(R.drawable.ourloc
                            )));
                    mMap.setMaxZoomPreference(15.5f);
                    mMap.setMinZoomPreference(6.5f);



                }

                @Override
                public void onError(Status status) {

                    Toast.makeText(getApplicationContext(), "An error occurred: " + status, Toast.LENGTH_SHORT).show();

                }
            });


            //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),6.5f));

            mMap.animateCamera(CameraUpdateFactory.zoomTo(12.5f), 2000, null);
            mMap.setMaxZoomPreference(14.5f);
            mMap.setMinZoomPreference(6.5f);



            // TrackGPS location;

            mMap.getUiSettings().setZoomControlsEnabled(true);
            return view;
        }
        class ViewHolder{
            public TextView textid,textname,textvisiting,butbookpart,forpartnerid,forpartnername,text_substype,textcount;
            public Button butviewpart;
            public RatingBar ratingBar;
        }
    }
    public class kilomilo extends AsyncTask<String,String, List<GeoLocate>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected List<GeoLocate> doInBackground(String... params) {
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
                List<GeoLocate> milokilo = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    GeoLocate catego = gson.fromJson(finalObject.toString(), GeoLocate.class);
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
        protected void onPostExecute(final List<GeoLocate> movieMode) {
            super.onPostExecute(movieMode);
        mView.dismiss();
            if (movieMode == null) {
                Toast.makeText(getApplicationContext(), "No Services available for your selection", Toast.LENGTH_SHORT).show();

            } else {
                MovieAdap adapter = new MovieAdap(getApplicationContext(), R.layout.list_mapdetails, movieMode);
                list29.setAdapter(adapter);
                list29.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        GeoLocate item = movieMode.get(position);
                        Intent intentb = new Intent(MapsActivity.this, ServiceProviderDetails.class);
                        intentb.putExtra("selservice_providerid",item.getPartner_uid());
                        intentb.putExtra("selservice_providername",item.getPartner_name());


                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("sel_subcategid",sel_subcategid);
                        editor.putString("sel_categid",selcategid);
                        editor.apply();


                        startActivity(intentb);
                    }
                });
                adapter.notifyDataSetChanged();
            }
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        gps = new TrackGps(MapsActivity.this);

        Double lat = gps.getLatitude();
        Double lng=gps.getLongitude();
        LatLng sydney = new LatLng(lat,lng);
        float zoomLevel =10;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        final HashMap<String, String> user = db.getUserDetails();
        mappageloc=user.get("user_city");
        mappagelat=user.get("user_latitude");
        mappagelng=user.get("user_longitude");

//        mappageloc= preferences.getString("presentlocationstore", "");
//        mappagelat= preferences.getString("presentlocationlat", "");
//        mappagelng= preferences.getString("presentlocationlng", "");
        Double maplat=Double.parseDouble(mappagelat);
        Double maplng=Double.parseDouble(mappagelng);

        new kilomilo().execute(GlobalUrl.user_mapdetails+"?partner_latitude="+mappagelat+"&partner_longitude="+mappagelng+"&subcategid="+sel_subcategid);

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        List<Address> addresses  = null;
        try {
            addresses = geocoder.getFromLocation(lat,lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String state = addresses.get(0).getAdminArea();



   // Toast.makeText(getApplicationContext(),state,Toast.LENGTH_SHORT).show();



        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(maplat,maplng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ourloc
                )));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(maplat,maplng) ,6.5f));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12.5f), 2000, null);
        mMap.setMaxZoomPreference(15.5f);
        mMap.setMinZoomPreference(6.5f);





    }



    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public void insertmes(final String ss1, final String ss2,final String ss3,final String ss4, final String ss6,final String ss7,final String ss8) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.user_booking, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");

                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("users_detail");
                         user_bookingid = users.getString("booking_uid");
                        Intent intentb=new Intent(MapsActivity.this,ServBookingConfirmation.class);
                        intentb.putExtra("selservice_providerid",sforpartnerid);
                        intentb.putExtra("selservice_providername",sforpartnername);


                        // SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MapsActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("sel_subcategid",sel_subcategid);
                        editor.putString("sel_categid",selcategid);
                        editor.apply();

                        editor.putString("userbookidentire",user_bookingid);

                        editor.apply();
                        intentb.putExtra("bookidid",user_bookingid);
                        startActivity(intentb);
                        finish();

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Your booking is already under process for this service",Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            { }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_uid", ss1);
                params.put("partner_uid", ss2);
                params.put("service_categ_uid",ss3);
                params.put("service_subcateg_uid",ss4);

                params.put("service_booking_address", ss6);
                params.put("service_latitude",ss7);
                params.put("service_longitude",ss8);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}