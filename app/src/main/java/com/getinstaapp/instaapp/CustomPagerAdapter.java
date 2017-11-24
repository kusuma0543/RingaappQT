package com.getinstaapp.instaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by sweety on 10/12/2017.
 */

public class CustomPagerAdapter extends PagerAdapter {

    private List<Sliderlist> sliderimg;
    private ImageLoader imageLoader;

//    int[] mResources = {
//            R.drawable.home, R.drawable.search,R.drawable.refer,R.drawable.call,R.drawable.comments,R.drawable.logout
//    };
    Context mContext;
    LayoutInflater mLayoutInflater;

    public CustomPagerAdapter(List<Sliderlist>sliderimg,Context context) {
        this.sliderimg=sliderimg;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return sliderimg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);


        Sliderlist utils=sliderimg.get(position);


        ImageView imageView = (ImageView) itemView.findViewById(R.id.imagesView);
        imageLoader=CustomVolleyRequest.getInstance(mContext).getImageLoader();
        imageLoader.get(utils.getBanner_images(),ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher,android.R.drawable.ic_menu_search));



        //imageView.setImageResource(mResources[position]);
        container.addView(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(position==0){
            Intent i1 = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(i1);
        }
        else if(position==1)
        {
            Toast.makeText(mContext,"image 2 clicked",Toast.LENGTH_SHORT).show();
        }

    }
});
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
