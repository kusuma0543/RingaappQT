package com.getinstaapp.instaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.AdapterView.OnItemSelectedListener;


import cn.pedant.SweetAlert.SweetAlertDialog;
import me.relex.circleindicator.CircleIndicator;

public class Categories extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemSelectedListener {
    private CustomPagerAdapter mCustomPagerAdapter;
    private ViewPager mViewPager;
    CircleIndicator indicator;
    private GridView home_gridview;
    private Spinner home_tspinner;
    private ArrayList<String> students;
    private ProgressDialog dialog;
    private JSONArray result;

    private Button edcat_search;

private String firstuid;
    RequestQueue rq;
    List<Sliderlist> sliderimg;
    String request_url=GlobalUrl.user_sliderdataret;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dialog = new ProgressDialog(this);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");

       //sendRequest();
        setTitle("INSTA");
        Intent intent=getIntent();
        firstuid=intent.getStringExtra("oneuid");
        Toast.makeText(getApplicationContext(),firstuid,Toast.LENGTH_SHORT).show();
updatelastseen(firstuid);

        home_gridview=(GridView) findViewById(R.id.home_gridview);
        edcat_search=(Button) findViewById(R.id.edcat_search);

        rq=Volley.newRequestQueue(this);
        sliderimg=new ArrayList<>();
        mViewPager = (ViewPager) findViewById(R.id.pager);
       indicator = (CircleIndicator) findViewById(R.id.indicator);


        students = new ArrayList<String>();
        home_tspinner=(Spinner) findViewById(R.id.home_tspinner);
        home_tspinner.setOnItemSelectedListener(this);

getData();

        edcat_search.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
//        String sed_home=edcat_search.getText().toString();
//        Intent intent=new Intent(Categories.this,AllSearch.class);
//        intent.putExtra("edkeyword",sed_home);
//       intent.putExtra("edkeywords","words");
//       // intent.putExtra("edkeyword", Categories.class.toString());
//        startActivity(intent);

    }
});
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        new JSONTask().execute(GlobalUrl.user_categoriesret);

    }



    private void getData(){
        StringRequest stringRequest = new StringRequest(GlobalUrl.user_spinnerdataret,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("result");
                            getStudents(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getStudents(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                students.add(json.getString("place_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        home_tspinner.setAdapter(new ArrayAdapter<String>(Categories.this, android.R.layout.simple_spinner_dropdown_item, students));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String selected = parent.getItemAtPosition(position).toString();
        Toast.makeText(getApplicationContext(), "you selected location is "+selected, Toast.LENGTH_SHORT).show();
        String users_updatedloc_serv="http://quaticstech.in/projecti1andro/android_users_spiner.php?place_id="+selected;
        new JSONTask().execute(users_updatedloc_serv);

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
            intent.putExtra("twouid",firstuid);
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
                        intent.putExtra("categoryname",categorieslist.getCategory_name());
                         intent.putExtra("categorysid", categorieslist.getCid());
                      //  editor.commit();
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
            Picasso.with(context).load(categorieslist.getCategory_thumbnail_image()).fit().error(R.drawable.load).fit().into(holder.menuimage);
            holder.menuname.setText(categorieslist.getCategory_name());



            return convertView;
        }
        class ViewHolder{
            private ImageView menuimage;
            private TextView menuname;
        }
    }

public  void sendRequest()
{
//    JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, request_url,null, new Response.Listener<JSONArray>() {
//        @Override
//        public void onResponse(JSONArray response) {
//            for(int i=0;i<response.length();i++)
//            {
//                Sliderlist sliderlist=new Sliderlist();
//
//                try {
//                    JSONObject jsonObject=response.getJSONObject(i);
//                    sliderlist.setBanner_images(jsonObject.getString("banner_images"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//sliderimg.add(sliderlist);
//            }
//            mCustomPagerAdapter = new CustomPagerAdapter(sliderimg,Categories.this);
//
//            mViewPager.setAdapter(mCustomPagerAdapter);
//            indicator.setViewPager(mViewPager);
//
//        }
//    }, new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//
//        }
//    });
// rq.add(jsonArrayRequest);
//   // AppController.getInstance().addToRequestQueue(jsonArrayRequest);
//
//}
    StringRequest stringRequest = new StringRequest(Request.Method.GET, request_url, new Response.Listener<String>() {
        public void onResponse(String response) {
            for(int i=0;i<response.length();i++)
            {
                Sliderlist sliderlist=new Sliderlist();

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    sliderlist.setBanner_images(jsonObject.getString("banner_images"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

sliderimg.add(sliderlist);
            }
            mCustomPagerAdapter = new CustomPagerAdapter(sliderimg,Categories.this);

            mViewPager.setAdapter(mCustomPagerAdapter);
            indicator.setViewPager(mViewPager);

        }


    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error)
        { }
    }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            return params;
        }
    };
    //rq.add(stringRequest);
    AppController.getInstance().addToRequestQueue(stringRequest);
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


}
