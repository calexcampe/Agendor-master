package com.example.agendor_timeline.View;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendor_timeline.R;
import com.example.agendor_timeline.TimeLineConfig;

import java.util.ArrayList;

public class VerticalRecyclerViewAdapter extends RecyclerView.Adapter<VerticalRecyclerViewAdapter.VerticalRecyclerViewHolder> {


    ArrayList<String> list;
    Context context;

    HorizontalRecyclerViewAdapter horizontalRecyclerViewAdapter;

    public VerticalRecyclerViewAdapter(Context context, ArrayList<String> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public VerticalRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_timeline_layout, parent, false);

        return new VerticalRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VerticalRecyclerViewHolder holder, int position) {
        holder.time.setText(TimeLineConfig.headerList.get(position));

        horizontalRecyclerViewAdapter = new HorizontalRecyclerViewAdapter(TimeLineConfig.timelineObjMap.get(TimeLineConfig.headerList.get(position)));
        holder.recyclerView.setAdapter(horizontalRecyclerViewAdapter);

    }

    @Override
    public int getItemCount() {
        return TimeLineConfig.headerList.size();
    }

    public class VerticalRecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView time, header;
        public RecyclerView recyclerView;

        RelativeLayout timelineindicator_container;
        TextView timelineindicator_line;

        public VerticalRecyclerViewHolder(View view) {
            super(view);

            time = view.findViewById(R.id.tv_timeline_time);
            header =  view.findViewById(R.id.tv_timeline_header);
            header.setVisibility(View.INVISIBLE);

            timelineindicator_container =  view.findViewById(R.id.container_timeline_indicator);
            timelineindicator_line =  view.findViewById(R.id.tv_timeline_indicator_line);

            /*apply configs*/
            time.setTextColor(Color.parseColor(TimeLineConfig.getTimelineHeaderTextColour()));
            time.setTextSize(TimeLineConfig.getTimelineHeaderSize());
            timelineindicator_line.setBackgroundColor(Color.parseColor(TimeLineConfig.getTimelineIndicatorLineColour()));
            timelineindicator_container.setBackgroundColor(Color.parseColor(TimeLineConfig.getTimelineIndicatorBackgroundColour()));

            recyclerView = view.findViewById(R.id.rv_horizontal_timeline);

            LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(recyclerViewLayoutManager);
            LinearLayoutManager horizontalLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(horizontalLinearLayoutManager);
        }
    }

    public void notifyDataSetChangedToHorizontalView() {
        if (horizontalRecyclerViewAdapter == null){
            return;
        }
        horizontalRecyclerViewAdapter.notifyDataSetChanged();
    }
}
