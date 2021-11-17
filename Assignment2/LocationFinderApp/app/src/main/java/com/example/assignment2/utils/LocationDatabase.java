package com.example.assignment2.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LocationDatabase extends SQLiteOpenHelper {

    private Context context;

    private static final int VER = 1;
    private static final String DB_NAME = "geodata";
    private static final String TABLE_NAME = "locations";

    public LocationDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, VER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase dbInstance) {
        String createQuery = "CREATE TABLE "+TABLE_NAME +" ("+
                "id INTEGER PRIMARY KEY,"+
                "address TEXT,"+
                "latitude REAL,"+
                "longitude REAL"+
                ");";
        dbInstance.execSQL(createQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase dbInstance, int i, int i1) {
        dbInstance.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(dbInstance);
    }

    public void newLocation(String address, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("address", address);
        values.put("latitude", latitude);
        values.put("longitude", longitude);

        long result = db.insert(TABLE_NAME, null, values);
        if(result == -1) {
            Toast.makeText(context, "There was an issue adding this location :(", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Location added!", Toast.LENGTH_SHORT).show();
        }
    }

    public void editLocation(int id, String address, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("address", address);
        values.put("latitude", latitude);
        values.put("longitude", longitude);

        long result = db.update(TABLE_NAME, values,"_id = ?",new String[]{Integer.toString(id)});
        if(result == -1) {
            Toast.makeText(context, "There was an issue editing this location :(", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Location updated!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteLocationByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        db.execSQL(query, new String[]{Integer.toString(id)});
    }


    public LocationWrapper getLocationByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + "WHERE id = ?";

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, new String[]{Integer.toString(id)});

            if (cursor.moveToFirst()) {
                LocationWrapper location = new LocationWrapper(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3));
                return location;
            }
        }
        Toast.makeText(context, "Failed to load location :(", Toast.LENGTH_LONG).show();
        return null;
    }

    public ArrayList<LocationWrapper> getLocations() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<LocationWrapper> locations = new ArrayList<LocationWrapper>();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            while(cursor.moveToNext()) {
                locations.add(new LocationWrapper(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3)));
            }
            return locations;
        } else {
            Toast.makeText(context, "There was an issue getting the locations :(", Toast.LENGTH_LONG).show();
        }

        return null;
    }

}
