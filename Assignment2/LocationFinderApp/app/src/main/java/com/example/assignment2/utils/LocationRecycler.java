package com.example.assignment2.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment2.R;
import java.util.ArrayList;

/* Note Recycler View Row Adapter */
public class LocationRecycler extends RecyclerView.Adapter<LocationRecycler.MyViewHolder> implements Filterable {

    private Context context;
    // Criteria for user search
    private CharSequence prev_search;
    // Display Locations
    private ArrayList<LocationWrapper> visible_locations;
    private ArrayList<LocationWrapper> location_list;
    private LocationClickListener clickListener;

    // Constructor to initialize main display arrays and full collection arrays
    public LocationRecycler(Context context, ArrayList<LocationWrapper> locations, LocationClickListener clickListener) {
        this.context = context;
        this.visible_locations = locations;
        this.location_list = locations;
        this.clickListener = clickListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView address_text;
        TextView lat_text;
        TextView long_text;
        LocationClickListener clickListener;

        public MyViewHolder(@NonNull View itemView, LocationClickListener listener) {
            super(itemView);
            address_text = itemView.findViewById(R.id.address_lbl);
            lat_text = itemView.findViewById(R.id.lat_lbl);
            long_text = itemView.findViewById(R.id.long_lbl);
            clickListener = listener;
        }

        @Override
        public void onClick(View v) {
            // fire clicklistener event to the selected card
            clickListener.locationClicked(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the view with the recycler row
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_row, parent, false);
        return new MyViewHolder(view, this.clickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LocationWrapper location = visible_locations.get(position);
        // Set values to the text views
        holder.address_text.setText(location.getAddress());
        holder.lat_text.setText("Latitude: "+Double.toString(location.getLatitude()));
        holder.long_text.setText("Longitude: "+Double.toString(location.getLongitude()));
    }

    @Override
    public int getItemCount() {
        return visible_locations.size();
    }

    public LocationWrapper getLocation(int position) {
        return visible_locations.get(position);
    }

    // Implement search logic
    @Override
    public Filter getFilter() {
        return locationFilter;
    }

    private Filter locationFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence sequence) {
            // Init arrays to hold final result of filter data
            ArrayList<LocationWrapper> filtered_locations = new ArrayList<>();

            // If there is no user input, display everything
            if (sequence == null || sequence.length() < 1) {
                filtered_locations.addAll(location_list);
            } else {
                // Search for string match/char sequence
                String pattern = sequence.toString().toLowerCase().trim();

                for (int i = 0; i < location_list.size(); i++) {
                    if (location_list.get(i).getAddress().toLowerCase().contains(pattern)) {
                        filtered_locations.add(location_list.get(i));
                    }
                }
            }

            // Return the filtered results
            // not this process is like a promise kinda like node because filter executes on another thread
            FilterResults results = new FilterResults();
            results.values = filtered_locations;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            // If the filter has values
            if (results.values != null) {
                prev_search = charSequence;
                visible_locations.clear();
                visible_locations.addAll((ArrayList<LocationWrapper>) results.values);
                notifyDataSetChanged();

                // double check we dont need to recall the filter
            //} else if (!charSequence.equals(prev_search)) {
            //    visible_locations.clear();
            //    notifyDataSetChanged();
            }
        }
    };
}
