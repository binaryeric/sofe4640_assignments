package com.example.assignment2.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment2.utils.LocationDatabase;
import com.example.assignment2.R;
import com.example.assignment2.utils.LocationWrapper;

import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Editor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Editor extends Fragment {

    private int ID;

    public Editor() {
        // Required empty public constructor
    }

    public static Editor newInstance(int id) {
        Editor fragment = new Editor();
        Bundle args = new Bundle();
        args.putInt("ID", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make ui more intuitive

        if (getArguments() != null) {
            ID = getArguments().getInt("ID");
        } else {
            ID = -1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editor, container, false);
    }

    @Nullable
    @Override
    public void onViewCreated(View mainView, @Nullable Bundle savedInstanceState) {
        Resources res = getResources();
        Geocoder geocoder = new Geocoder(mainView.getContext(), Locale.getDefault());
        LocationDatabase database = new LocationDatabase(getContext());
        Fragment search_frag = Search.newInstance();

        // Make the UI more intuitive for adding entries
        Button delete_btn = mainView.findViewById(R.id.delete_btn);
        Button update_btn = mainView.findViewById(R.id.update_button);
        Button grab_btn = mainView.findViewById(R.id.grab_location_btn);

        // input parameters
        TextView address_edit = mainView.findViewById(R.id.address_edit);
        EditText lat_edit = mainView.findViewById(R.id.latitude_edit);
        EditText long_edit = mainView.findViewById(R.id.longitude_edit);

        // No delete actions here
        if (ID == -1) {
            delete_btn.setText(res.getString(R.string.cancel_btn));
            update_btn.setText(res.getString(R.string.create_btn));
        } else {
            // Load the information for the data
            LocationWrapper result = database.getLocationByID(ID);
            if(result != null) {
                lat_edit.setText(Double.toString(result.getLatitude()));
                long_edit.setText(Double.toString(result.getLongitude()));
                address_edit.setText(result.getAddress());
            }
        }

        lat_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    double lat = Double.valueOf(lat_edit.getText().toString());
                    double lon = Double.valueOf(long_edit.getText().toString());

                    if(lat != 0 && lon != 0) {
                        List<Address> addr = null;
                        addr = geocoder.getFromLocation(lat, lon, 1);
                        address_edit.setText(addr.get(0).getAddressLine(0));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        long_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            public void afterTextChanged(Editable editable) {
                try {
                    double lat = Double.valueOf(lat_edit.getText().toString());
                    double lon = Double.valueOf(long_edit.getText().toString());

                    if(lat != 0 && lon != 0) {
                        List<Address> addr = null;
                        addr = geocoder.getFromLocation(lat, lon, 1);
                        address_edit.setText(addr.get(0).getAddressLine(0));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addr = address_edit.getText().toString();

                if(addr.length() > 0 && lat_edit.getText().length() > 0 && long_edit.getText().length() > 0) {

                    double lat = Double.parseDouble(String.valueOf(lat_edit.getText()));
                    double lon = Double.parseDouble(String.valueOf(long_edit.getText()));

                    if(ID == -1) {
                        database.newLocation(addr, lat, lon);
                    } else {
                        database.editLocation(ID, addr, lat, lon);
                    }
                    // Go home
                    // Switch to the new fragment
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_view, search_frag);
                    // they cannot return to this
                    transaction.disallowAddToBackStack();
                    transaction.commit();
                } else {
                    // Notify them that data cannot be added if it is not there
                    Toast.makeText(mainView.getContext(), "You need to enter latitude and longitude!", Toast.LENGTH_LONG);
                }


            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ID != -1) {
                    database.deleteLocationByID(ID);
                }
                // Go home
                // Switch to the new fragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_view, search_frag);
                transaction.disallowAddToBackStack();
                transaction.commit();
            }
        });

        grab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check permission
                LocationManager location_manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(mainView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(mainView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(mainView.getContext(), "Location Services Permission Required", Toast.LENGTH_SHORT).show();
                    // Request the permission
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);
                    return;
                }
                try {
                    Location current_loc = location_manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    long_edit.setText(Double.toString(current_loc.getLongitude()));
                    lat_edit.setText(Double.toString(current_loc.getLatitude()));
                } catch (Exception e) {
                    Toast.makeText(mainView.getContext(), "Error, Location Services Failed :(", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

}