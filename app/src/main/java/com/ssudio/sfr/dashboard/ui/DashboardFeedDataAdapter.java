package com.ssudio.sfr.dashboard.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ssudio.sfr.R;
import com.ssudio.sfr.dashboard.model.DashboardFeedModel;
import com.ssudio.sfr.utility.DateUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class DashboardFeedDataAdapter extends ArrayAdapter<DashboardFeedModel> {

    public DashboardFeedDataAdapter(Context context, ArrayList<DashboardFeedModel> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DashboardFeedModel feed = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_feed, parent, false);
        }

        // Lookup view for data population
        TextView txtFeedDate = (TextView) convertView.findViewById(R.id.txtFeedDate);
        TextView txtFeedDescription = (TextView) convertView.findViewById(R.id.txtFeedDescription);

        // Populate the data into the template view using the data object
        txtFeedDate.setText(DateUtility.parseToApplicationDateFormat(feed.getFeedDate()));
        txtFeedDescription.setText(feed.getFeed());

        // Return the completed view to render on screen
        return convertView;
    }
}
