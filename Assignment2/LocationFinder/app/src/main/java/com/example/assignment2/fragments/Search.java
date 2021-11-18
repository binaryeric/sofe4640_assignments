package com.example.assignment2.fragments;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.assignment2.R;
import com.example.assignment2.utils.LocationClickListener;
import com.example.assignment2.utils.LocationDatabase;
import com.example.assignment2.utils.LocationWrapper;
import com.example.assignment2.utils.LocationRecycler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Search extends Fragment {

    public Search() {
        // Required empty public constructor
    }

    public static Search newInstance() {
        return new Search();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Nullable
    @Override
    public void onViewCreated(View mainView, @Nullable Bundle savedInstanceState) {
        Resources res = getResources();

        // Load in data
        LocationDatabase database = new LocationDatabase(mainView.getContext());
        ArrayList<LocationWrapper> location_data = database.getLocations();

        // Search Queries
        SearchView search = (SearchView) mainView.findViewById(R.id.searchView);

        // Location Count
        TextView num_locations = (TextView) mainView.findViewById(R.id.search_result_text);
        String locations_text = res.getString(R.string.results_text, location_data.size());
        num_locations.setText(locations_text);

        // Handle items in the recycler changing
        RecyclerView recycler = mainView.findViewById(R.id.location_recycler);
        LocationRecycler locations_adapter = new LocationRecycler(mainView.getContext(), location_data, new LocationClickListener() {
            @Override
            public void locationClicked(int recycler_position) {
                LocationRecycler adapter = (LocationRecycler) recycler.getAdapter();
                LocationWrapper location = adapter.getLocation(recycler_position);
                // Open location to edit
                Fragment edit_frag = Editor.newInstance(location.getID());
                // Switch to the new fragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_view, edit_frag);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        // Finish the recycler layout
        recycler.setAdapter(locations_adapter);
        recycler.setLayoutManager(new LinearLayoutManager(mainView.getContext()));

        // Allow user to query for data
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String userSelection) {
                locations_adapter.getFilter().filter(userSelection);
                String text = res.getString(R.string.results_text, locations_adapter.getItemCount());
                num_locations.setText(text);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String userSelection) {
                locations_adapter.getFilter().filter(userSelection);
                num_locations.setText("Searching...");
                return false;
            }
        });

        // New Location Entries
        FloatingActionButton add_btn = (FloatingActionButton) mainView.findViewById(R.id.add_location_button);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // By not passing arguments to the instance the bundle will be empty and assumed add new location
                Fragment add_frag = new Editor();
                // Switch to the new fragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_view, add_frag);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}