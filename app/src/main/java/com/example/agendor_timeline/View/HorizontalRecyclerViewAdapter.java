package com.example.agendor_timeline.View;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendor_timeline.R;
import com.example.agendor_timeline.TimeLineConfig;
import com.example.agendor_timeline.interfaces.TimelineObject;

import java.util.ArrayList;


public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerViewAdapter.HorizontalRecyclerViewHolder> {


    ArrayList<TimelineObject> list = new ArrayList<>();

    public HorizontalRecyclerViewAdapter(ArrayList<TimelineObject> list) {
        this.list = list;
    }

    @Override
    public HorizontalRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_timeline_layout, parent, false);

        return new HorizontalRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HorizontalRecyclerViewHolder holder, final int position) {
        holder.textView.setText(""+list.get(position).getTitle());

        TimeLineConfig.getImageLoadEngine().onLoadImage(holder.imageView, list.get(position).getImageUrl());

        if (TimeLineConfig.getListener() != null) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimeLineConfig.getListener().onTimelineObjectClicked(list.get(position));
                }
            });

            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TimeLineConfig.getListener().onTimelineObjectLongClicked(list.get(position));
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }


    public class HorizontalRecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;
        public CardView cardView;


        public HorizontalRecyclerViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.tv_timeline_horizontal_card_name);
            cardView =  view.findViewById(R.id.timeline_obj_cardview);
            imageView =  view.findViewById(R.id.iv_horizontal_card_image);

            textView.setTextSize(TimeLineConfig.getTimelineCardTextSize());
            textView.setTextColor(Color.parseColor(TimeLineConfig.getTimelineCardTextColour()));
            textView.setBackgroundColor(Color.parseColor(TimeLineConfig.getTimelineCardTextBackgroundColour()));
        }
    }
}