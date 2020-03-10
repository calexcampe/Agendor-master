package com.example.agendor_timeline.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.agendor_timeline.R;
import com.example.agendor_timeline.TimeLineConfig;
import com.example.agendor_timeline.interfaces.ImageLoadingEngine;
import com.example.agendor_timeline.View.VerticalRecyclerViewAdapter;
import com.example.agendor_timeline.interfaces.TimelineObject;
import com.example.agendor_timeline.interfaces.TimelineObjectClickListener;
import com.example.agendor_timeline.model.TimelineGroupType;

import java.util.ArrayList;

public class TimelineFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager recyclerViewLayoutManager;
    private LinearLayoutManager verticalLinearLayoutManager;
    private VerticalRecyclerViewAdapter verticalRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swipe_timeline_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_vertical_timeline);

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        // Adding items to RecyclerView.
        //addItemsToHorizontalRecyclerViewArrayList();

        verticalRecyclerViewAdapter = new VerticalRecyclerViewAdapter(getContext(), null);
        verticalLinearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLinearLayoutManager);
        recyclerView.setAdapter(verticalRecyclerViewAdapter);
    }

    public void addOnClickListener(TimelineObjectClickListener listener){
        TimeLineConfig.addOnClickListener(listener);
    }

    public void setImageLoadEngine(ImageLoadingEngine engine){
        TimeLineConfig.setImageLoadEngine(engine);
    }

    public void setData(ArrayList<TimelineObject> dataList, TimelineGroupType type ){
        TimeLineConfig.setData(dataList, type);

        if (verticalRecyclerViewAdapter!=null) {
            verticalRecyclerViewAdapter.notifyDataSetChanged();
            verticalRecyclerViewAdapter.notifyDataSetChangedToHorizontalView();
        }
    }

    public void addSingleObject(TimelineObject data, TimelineGroupType type){
        TimeLineConfig.addObject(data,type);
        if (verticalRecyclerViewAdapter!=null) {
            verticalRecyclerViewAdapter.notifyDataSetChanged();
            verticalRecyclerViewAdapter.notifyDataSetChangedToHorizontalView();
        }
    }

    public void clearData(){
        TimeLineConfig.clearData();
    }

    /**
     * Timeline header text size
     * */
    public void setTimelineHeaderSize(int size){
        TimeLineConfig.setTimelineHeaderSize(size);
    }
    public int getTimelineHeaderSize(){
        return TimeLineConfig.getTimelineHeaderSize();
    }

    /**
     * Timeline header text colour
     * */
    public void setTimelineHeaderTextColour(String textColour){
        TimeLineConfig.setTimelineHeaderTextColour(textColour);
    }
    public String getTimelineHeaderTextColour(){
        return TimeLineConfig.getTimelineHeaderTextColour();
    }

    /**
     * Timeline header background colour
     * */
    public void setTimelineHeaderBackgroundColour(String colour){
        TimeLineConfig.setTimelineHeaderBackgroundColour(colour);
    }
    public String getTimelineHeaderBackgroundColour(){
        return TimeLineConfig.getTimelineHeaderBackgroundColour();
    }

    /**
     * Timeline indicator background colour
     * */
    public void setTimelineIndicatorBackgroundColour(String colour){
        TimeLineConfig.setTimelineIndicatorBackgroundColour(colour);
    }
    public String getTimelineIndicatorBackgroundColour(){
        return TimeLineConfig.getTimelineIndicatorBackgroundColour();
    }

    /**
     * Timeline indicator line colour
     * */
    public void setTimelineIndicatorLineColour(String colour){
        TimeLineConfig.setTimelineIndicatorLineColour(colour);
    }
    public String getTimelineIndicatorLineColour(){
        return TimeLineConfig.getTimelineIndicatorLineColour();
    }

    /**
     * Timeline card text size
     * */
    public void setTimelineCardTextSize(int size){
        TimeLineConfig.setTimelineCardTextSize(size);
    }
    public int getTimelineCardTextSize(){
        return TimeLineConfig.getTimelineCardTextSize();
    }

    /**
     * Timeline card text colour
     * */
    public void setTimelineCardTextColour(String colour){
        TimeLineConfig.setTimelineCardTextColour(colour);
    }
    public String getTimelineCardTextColour(){
        return TimeLineConfig.getTimelineCardTextColour();
    }

    /**
     * Timeline card text background colour
     * */
    public void setTimelineCardTextBackgroundColour(String colour){
        TimeLineConfig.setTimelineCardTextBackgroundColour(colour);
    }
    public String getTimelineCardTextBackgroundColour(){
        return TimeLineConfig.getTimelineCardTextBackgroundColour();
    }

}
