package com.example.assignment2.data;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;

import com.example.assignment2.utils.LocationDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DatasetImporter {

    // Quickest way to import a dataset of 50 locations
    private static double latitudes[] = {
            46.93778,
            49.78475,
            53.69456,
            46.20978,
            49.15003,
            53.8,
            47.7925,
            47.32722,
            49.58477,
            54.68333,
            54.65,
            47.57778,
            49.11667,
            51.83417,
            58.71194,
            48.15,
            50.98333,
            44.63639,
            50.08558,
            49.48333,
            60.31484,
            58.01667,
            49.91639,
            48.66278,
            44.10556,
            60.85556,
            60.87879,
            55.41361,
            55.48333,
            52.40007,
            59.63333,
            53.88333,
            52.23028,
            44.50847,
            47.08278,
            48.57556,
            42.80323,
            47.51111,
            52.38333,
            52.80005,
            52.34999,
            49.78336,
            45.35583,
            45.47833,
            69.53611,
            43.93444,
            57.65,
            50.1,
            43.77722,
            44.68904
    };

    private static double longitudes[] = {
            -79.24056,
            -112.15097,
            -56.70604,
            -59.9548,
            -103.1838,
            -122.71667,
            -84.51444,
            -65.01097,
            -92.19071,
            -122.6,
            -124.75,
            -54.21222,
            -117.71667,
            -102.47987,
            -98.48028,
            -69.71667,
            -118.65,
            -79.41722,
            -92.49533,
            -117.38333,
            -134.27151,
            -131,
            -126.66444,
            -71.825,
            -78.27778,
            -135.46056,
            -135.35921,
            -100.95944,
            -125.96667,
            -109.01739,
            -133.85,
            -125.86667,
            -111.26556,
            -79.12063,
            -72.24472,
            -71.62444,
            -81.2525,
            -52.96167,
            -126.83333,
            -106.96726,
            -102.65048,
            -103.66721,
            -73.27028,
            -75.70139,
            -93.52083,
            -80.00167,
            -121.16667,
            -123.01667,
            -79.2975,
            -63.52681
    };

    public static void importToDatabase(Context context) {
        Toast.makeText(context, "Checking database...", Toast.LENGTH_LONG);
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        LocationDatabase database = new LocationDatabase(context);

        for(int i=0; i<latitudes.length; i++) {
            if(database.locationExists(latitudes[i],longitudes[i]) == false) {
                // Create the entry if it doesnt exist
                try {
                    // Get the geocoder's input for address:
                    List<Address> addr = null;
                    addr = geocoder.getFromLocation(latitudes[i], longitudes[i], 1);
                    String address = addr.get(0).getAddressLine(0);

                    // Insert into the database
                    database.newLocation(address, latitudes[i], longitudes[i]);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Toast.makeText(context, "Database updated!", Toast.LENGTH_LONG);

        }

    }


}
