package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.service.autofill.Dataset;

import com.example.assignment2.data.DatasetImporter;
import com.example.assignment2.fragments.Search;
import com.example.assignment2.utils.LocationDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            // Check to see if we need to import data;
            DatasetImporter.importToDatabase(this);

            // Load in Search Fragment to the container:
            Fragment search_frag = new Search();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_view, search_frag)
                    .commitNow();
        }

    }
}