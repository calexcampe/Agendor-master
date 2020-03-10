package com.example.agendor_timeline.View;

import android.widget.ImageView;

import com.example.agendor_timeline.R;
import com.example.agendor_timeline.interfaces.ImageLoadingEngine;
import com.squareup.picasso.Picasso;


public class PicassoEngine implements ImageLoadingEngine {
    @Override
    public void onLoadImage(ImageView imageView, String uri) {


        Picasso.with(imageView.getContext())
                .load(uri)
                .resize(250, 250)
                .placeholder(R.drawable.timeline_card_placeholder)
                .centerCrop()
                .into(imageView);

    }
}
