package com.example.nikhil.youtubeplayback.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.nikhil.youtubeplayback.R;

public class CustomViewYoutube extends RelativeLayout {

    View view;

    public CustomViewYoutube(Context context) {
        super(context);
        view=LayoutInflater.from(getContext()).inflate(R.layout.cutsom_view_youtube,null,false);
    }


    public CustomViewYoutube(Context context, AttributeSet attrs) {
        super(context, attrs);
       view= LayoutInflater.from(getContext()).inflate(R.layout.cutsom_view_youtube,null,false);

    }

    public CustomViewYoutube(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       view= LayoutInflater.from(getContext()).inflate(R.layout.cutsom_view_youtube,null,false);
    }

    public CustomViewYoutube(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        view=LayoutInflater.from(getContext()).inflate(R.layout.cutsom_view_youtube,null,false);
    }

    @Override
    public boolean performClick() {
        Log.v("tag1","hi");
        return super.performClick();

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
         super.onInterceptTouchEvent(ev);



         return true;
    }

    public View getView()
    {
        this.addView(view);
        return view;
    }




}
