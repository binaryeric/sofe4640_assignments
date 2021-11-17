package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.assignment2.fragments.Search;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            // Load in Search Fragment to the container:
            Fragment search_frag = new Search();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_view, search_frag)
                    .commitNow();
        }

    }
}