package com.getinstaapp.instaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by sweety on 10/12/2017.
 */

public class CustomPagerAdapter extends PagerAdapter {
    int[] mResources = {
            R.drawable.home,
            R.drawable.search,
            R.drawable.refer,
            R.drawable.call,
            R.drawable.comments,
            R.drawable.logout
    };
    Context mContext;
    LayoutInflater mLayoutInflater;

    public CustomPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imagesView);
        imageView.setImageResource(mResources[position]);
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
