package com.ringaapp.ringauser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jetradar.desertplaceholder.DesertPlaceholder;
import com.ringaapp.ringauser.dbhandlers.SQLiteHandler;
import com.ringaapp.ringauser.dbhandlers.SessionManager;
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
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;
import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

public class ServiceProviderDetails extends AppCompatActivity {
    public ListView listview_service,review_list;
    FlipperLayout flipperLayout_service;
     GifImageView serprov_bookbut;
    String  userid_book,servdetcategoryname_user,categid_book,subcateg_book,alladdress_book,
            alllatitude_book,alllongitude_book,sel_subcategnameuser,s_partner_lastseen;
    RatingBar rating_servp_ratingbar;
    Float dbrate;
    TextView tv_servp_name,tv_servp_subcatname,tv_servp_catname,tv_servp_locality,tv_servp_rating,
            tv_servp_totaljobsdone,tv_servp_totalreview,tv_servp_lastupdated,tv_servp_desc;
    Button but_servp_visitingcharge;
    String alladredlocali,stotal_count;

    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
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


        if (isConnectedToNetwork()) {
            final HashMap<String, String> user = db.getUserDetails();
            userid_book = user.get("uid");
            alladdress_book = user.get("user_address");
            alllatitude_book = user.get("user_latitude");
            alllongitude_book = user.get("user_longitude");
            alladredlocali=user.get("user_city");

            tv_servp_name=findViewById(R.id.servpdet_name);
            tv_servp_subcatname=findViewById(R.id.servpdet_subcatname);
            tv_servp_catname=findViewById(R.id.servpdet_catname);
            tv_servp_locality=findViewById(R.id.servpdet_localityname);
            tv_servp_rating=findViewById(R.id.servpdet_rating);
            tv_servp_totaljobsdone=findViewById(R.id.servpdet_totaljobs);
            tv_servp_totalreview=findViewById(R.id.servpdet_totalreview);
            tv_servp_lastupdated=findViewById(R.id.servpdet_lastupdated);
            tv_servp_desc=findViewById(R.id.servpdet_desc);
            but_servp_visitingcharge=findViewById(R.id.servpdet_visitingcharge);
            rating_servp_ratingbar=findViewById(R.id.servpdet_ratingbar);


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            categid_book = preferences.getString("sel_categid", "");
            subcateg_book = preferences.getString("sel_subcategid", "");
            sel_subcategnameuser=preferences.getString("sel_subcategnameuser","");
            servdetcategoryname_user=preferences.getString("categoryname_user","");
            final Intent intent = getIntent();
            final String sel_servicepid = intent.getStringExtra("selservice_providerid");
            String sel_servicepname = intent.getStringExtra("selservice_providername");
            String partner_Budget=intent.getStringExtra("visitingcahrgeofpartner");
            String servdet_totalreviews=intent.getStringExtra("totalrating");
            s_partner_lastseen=intent.getStringExtra("partner_lastseendate");
            stotal_count=intent.getStringExtra("partner_ratingcount");
            toolbar.setTitle(sel_servicepname);
            getservdet(sel_servicepid);




            tv_servp_name.setText(sel_servicepname);
            tv_servp_catname.setText(" "+servdetcategoryname_user);
            tv_servp_subcatname.setText(sel_subcategnameuser+" -");
            tv_servp_locality.setText(alladredlocali);
            String rupeesymbo="\u20B9."+partner_Budget;
            String review_total=servdet_totalreviews+"/5";
            but_servp_visitingcharge.setText(rupeesymbo);
            tv_servp_rating.setText("Based on number of Reviews:"+stotal_count);
            tv_servp_totalreview.setText(review_total);
            Float ratemap= Float.parseFloat(servdet_totalreviews);
            rating_servp_ratingbar.setRating(ratemap);
            tv_servp_lastupdated.setText(s_partner_lastseen);

            flipperLayout_service = findViewById(R.id.flipper_layout_service);
            listview_service = findViewById(R.id.s_service);
            serprov_bookbut = findViewById(R.id.serprov_bookbut);
            review_list=findViewById(R.id.review_part_list);

            String URLL = "http://quaticstech.in/projecti1andro/android_partner_servprovimagesslider.php?partner_uid=" + sel_servicepid;
            new kilomilo().execute(URLL);
            String review_partner = "http://quaticstech.in/projecti1andro/android_users_partner_review.php?partner_uid=" + sel_servicepid;
            new Reviewlistpart().execute(review_partner);
            serprov_bookbut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(ServiceProviderDetails.this, ServBookingConfirmation.class);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ServiceProviderDetails.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("confirm_bookingid", sel_servicepid);
                    editor.apply();
                    insertmes(userid_book, sel_servicepid, categid_book, subcateg_book, alladdress_book, alllatitude_book, alllongitude_book);

                    startActivity(intent1);
                }
            });
        }
        else
        {
            setContentView(R.layout.content_ifnointernet);
            DesertPlaceholder desertPlaceholder = (DesertPlaceholder) findViewById(R.id.placeholder_fornointernet);
            desertPlaceholder.setOnButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ServiceProviderDetails.this,Categories.class);
                    startActivity(intent);
                }
            });
        }

    }
    public class MovieAdap extends ArrayAdapter {
        private List<BannerlistPartner> movieModelList;
        private int resource;
        Context context;
        private LayoutInflater inflater;

        MovieAdap(Context context, int resource, List<BannerlistPartner> objects) {
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
            final BannerlistPartner ccitacc = movieModelList.get(position);
            holder.textone.setText(ccitacc.getPartner_uid());

            FlipperView view = new FlipperView(getBaseContext());
       // Picasso.with(context).load(ccitacc.getPromotion_fullimage()).fit().error(R.drawable.load).fit().into(flipperLayout);
            view.setImageUrl(ccitacc.getPartner_images());
            flipperLayout_service.addFlipperView(view);
//            view.setOnFlipperClickListener(new FlipperView.OnFlipperClickListener() {
//                @Override
//                public void onFlipperClick(FlipperView flipperView) {
//                    Toast.makeText(ServiceProviderDetails.this
//                            , "my service"
//                            , Toast.LENGTH_SHORT).show();
//                }
//            });

            return convertView;
        }

        class ViewHolder {
            public TextView textone;


        }
    }

    public class kilomilo extends AsyncTask<String, String, List<BannerlistPartner>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<BannerlistPartner> doInBackground(String... params) {
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
                List<BannerlistPartner> milokilo = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    BannerlistPartner catego = gson.fromJson(finalObject.toString(), BannerlistPartner.class);
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
        protected void onPostExecute(final List<BannerlistPartner> movieMode) {
            super.onPostExecute(movieMode);
            if (movieMode== null)
            {
                Toast.makeText(getApplicationContext(),"No Services available for your selection", Toast.LENGTH_SHORT).show();

            }
            else
            {
            MovieAdap adapter = new MovieAdap(getApplicationContext(), R.layout.categorytwo, movieMode);
                listview_service.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }

        public class ReviewPartner extends ArrayAdapter {
            private List<Reviewlistpartner> ReviewListpar;
            private int resource;
            Context context;
            private LayoutInflater inflater;

            ReviewPartner(Context context, int resource, List<Reviewlistpartner> objects) {
                super(context, resource, objects);
                ReviewListpar = objects;
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
                final ReviewPartner.ViewHolder holder;
                if (convertView == null) {
                    convertView = inflater.inflate(resource, null);
                    holder = new ReviewPartner.ViewHolder();

                    holder.reviwer_name =  convertView.findViewById(R.id.review_name);
                    holder.reviwer_place =  convertView.findViewById(R.id.review_place);
                    holder.reviwer_feedback =  convertView.findViewById(R.id.review_feedback);
                     holder.reviwer_profile_image = convertView.findViewById(R.id.review_profile_image);
                    convertView.setTag(holder);
                }//ino
                else {
                    holder = (ReviewPartner.ViewHolder) convertView.getTag();
                }
                final Reviewlistpartner reviewpartlist = ReviewListpar.get(position);
                holder.reviwer_name.setText(reviewpartlist.getUser_name());
                holder.reviwer_place.setText(reviewpartlist.getUser_address_cityname());
                holder.reviwer_feedback.setText(reviewpartlist.getUser_feedback());
          Picasso.with(context).load(reviewpartlist.getUser_profile_image()).fit().error(R.drawable.load).fit().into(holder.reviwer_profile_image);
                return convertView;
            }

            class ViewHolder {
                public TextView reviwer_name,reviwer_place,reviwer_feedback;
                public CircleImageView reviwer_profile_image;


            }
        }

    public class Reviewlistpart extends AsyncTask<String, String, List<Reviewlistpartner>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Reviewlistpartner> doInBackground(String... params) {
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
                List<Reviewlistpartner> fda = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    Reviewlistpartner reviwl = gson.fromJson(finalObject.toString(), Reviewlistpartner.class);
                    fda.add(reviwl);
                }
                return fda;
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
        protected void onPostExecute(final List<Reviewlistpartner> rplist) {
            super.onPostExecute(rplist);
            if (rplist!= null)
            {

                ReviewPartner adapter = new ReviewPartner(getApplicationContext(), R.layout.review_card_list, rplist);
               // review_list.setAdapter(adapter);

                review_list.setAdapter(adapter);
                Helper.getListViewSize(review_list);


                adapter.notifyDataSetChanged();


            }
            else
            {
                Toast.makeText(getApplicationContext(),"No Services available for your selection", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
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
                        String user_bookingid = users.getString("booking_uid");

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ServiceProviderDetails.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userbookidentire",user_bookingid);

                        editor.apply();

                        finish();

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please check number and Password",Toast.LENGTH_SHORT).show();

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_about_scroll, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_ringa) {

            startActivity(new Intent(ServiceProviderDetails.this,Categories.class));
        }


        return true;
    }
    public void getservdet(final String sphone1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.user_servicepfiledet, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");

                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("users_detail");
                        String partname=users.getString("partner_name");
                        String partner_address=users.getString("partner_address");
                        String partner_about=users.getString("partner_about");
                        JSONObject userss = users.getJSONObject("rating");
                        JSONObject userssm = users.getJSONObject("count");
                        String uname1 = userss.getString("AVG(user_ratings)");
                        if(uname1==null)
                        {

//                            textservname.setText(partname);
//                            textservaddress.setText(partner_address);
                            tv_servp_desc.setText(partner_about);
//                            ratingBarserv.setRating(0);
//                            textservcount.setText("0");
//                            textcount_serv.setText("0");
                        }
                        else
                        {
                            String uname2 = userssm.getString("COUNT(user_ratings)");
                            //Toast.makeText(ServiceProviderDetails.this, partname, Toast.LENGTH_SHORT).show();

//                            textservname.setText(partname);
//                            textservaddress.setText(partner_address);
                            tv_servp_desc.setText(partner_about);
//                            textservcount.setText(uname1);
//                            dbrate = Float.parseFloat(uname1);
//                            ratingBarserv.setRating(dbrate);
//                            textcount_serv.setText(uname2);


                        }







                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"No one has given review",Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> insert = new HashMap<String, String>();
                insert.put("partner_uid", sphone1);

                return insert;

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
