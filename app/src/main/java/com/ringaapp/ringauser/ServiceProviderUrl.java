package com.ringaapp.ringauser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ServiceProviderUrl extends AppCompatActivity {
    String partner_uid,subcateg_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_url);
     onNewIntent(getIntent());

    }
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            Uri uri = Uri.parse(data);
            partner_uid = uri.getQueryParameter("partner");
            subcateg_uid = uri.getQueryParameter("subcateg");

            partnerdetailscollector(partner_uid,subcateg_uid);










        }

    }
    public  void partnerdetailscollector(final String partner_uid, final String subcateg_uid)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.service_provider_url_handle, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");

                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("users_detail");
String sforpartnerid=users.getString("partner_uid");
                        String sforpartnername=users.getString("partner_name");
                        String visitingcharge=users.getString("partner_budget");
                        String rating_average=users.getString("user_ratings");
                        String lastseenstatus=users.getString("partner_createddate");
                        String getting_rating_countl=users.getString("user_rating_count");
                        String sel_subcategid=users.getString("service_subcateg_uid");
                        String selcategid=users.getString("service_categ_uid");
                        String sel_subcategname=users.getString("service_subcateg_name");
                        String mapcategoryname_user=users.getString("service_categ_name");

                        Intent intentb=new Intent(ServiceProviderUrl.this,ServiceProviderDetails.class);
                        intentb.putExtra("selservice_providerid",sforpartnerid);
                        intentb.putExtra("selservice_providername",sforpartnername);
                        intentb.putExtra("visitingcahrgeofpartner",visitingcharge);
                        intentb.putExtra("totalrating",rating_average);
                        intentb.putExtra("partner_lastseendate",lastseenstatus);
                        intentb.putExtra("partner_ratingcount",getting_rating_countl);

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ServiceProviderUrl.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("sel_subcategid",sel_subcategid);
                        editor.putString("sel_categid",selcategid);
                        editor.putString("sel_subcategnameuser",sel_subcategname);
                        editor.putString("categoryname_user",mapcategoryname_user);
                        editor.apply();
                        startActivity(intentb);
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
                params.put("partner", partner_uid);
                params.put("subcateg", subcateg_uid);


                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);


    }
}
