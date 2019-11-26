package com.example.inclass13;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddTripActivity extends AppCompatActivity {

    AlertDialog.Builder alertBuilder;
    String selectedPlace;
    String selectedPlaceId;
    EditText tripName;
    EditText tripCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        setTitle("Add Trip");

        tripName = findViewById(R.id.trip_add_name);
        tripCity = findViewById(R.id.trip_add_city);

        alertBuilder = new AlertDialog.Builder(AddTripActivity.this);

        findViewById(R.id.trip_search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validate
                if (tripCity.getText().toString().isEmpty()) {
                    Toast.makeText(AddTripActivity.this, "Trip City cannot be empy", Toast.LENGTH_LONG).show();
                    return;
                }
                new SearchTask().execute("https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyAdLZSZC6naDeztF-_-lPLTRXGM6O5cWM4&types=(cities)&input=" + tripCity.getText().toString());
            }
        });

        findViewById(R.id.add_trip_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddTripActivity.this, MainActivity.class);
                Trip trip = new Trip(tripName.getText().toString(), selectedPlace, selectedPlaceId);
                intent.putExtra("trip", trip);
                setResult(10, intent);
                finish();
            }
        });
    }

    class SearchTask extends AsyncTask<String, Void, List<Place>> {

        @Override
        protected List<Place> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<Place> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONArray places = root.getJSONArray("predictions");
                    for (int i = 0; i < places.length(); i++) {
                        JSONObject placesJSONObject = places.getJSONObject(i);
                        Place place = new Place(placesJSONObject.getString("description").toString(), placesJSONObject.getString("place_id").toString());
                        result.add(place);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(List<Place> places) {
            super.onPostExecute(places);
            if (places.size() > 0) {
                List<String> descriptions = places.stream().map(place -> place.getDescription()).collect(Collectors.toList());
                String[] strings = descriptions.toArray(new String[descriptions.size()]);
                alertBuilder.setItems(strings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tripCity.setText(strings[i]);
                        selectedPlace = strings[i];
                        selectedPlaceId = places.get(i).getPlaceId();
                    }
                }).create().show();
            }
        }
    }
}
