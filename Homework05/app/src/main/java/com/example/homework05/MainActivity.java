package com.example.homework05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ListView listView;
    List<Source> sources = new ArrayList<>();
    public static String SOURCE = "source_obj";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.list_view);
        if (!isConnected()) {
            Toast.makeText(this, "Internet not connected", Toast.LENGTH_LONG).show();
        }else{
            new ParseSourceTask().execute("https://newsapi.org/v2/sources?apiKey=6b43fa14eb7947a5b47d5d149721196c");
        }

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(MainActivity.this, NewsActivity.class);
            intent.putExtra(SOURCE, sources.get(i));
            if (!isConnected()) {
                Toast.makeText(this, "Internet not connected", Toast.LENGTH_LONG).show();
            }else{
                startActivity(intent);
            }

        });

    }


    public class ParseSourceTask extends AsyncTask<String, String, List<Source>> {

        @Override
        protected List<Source> doInBackground(String... strings) {
            publishProgress("start");
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String response = IOUtils.toString(httpURLConnection.getInputStream(), "UTF-8");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("sources");
                    for (int index = 0; index < jsonArray.length(); index++) {
                        Source source = new Source(jsonArray.getJSONObject(index).get("id").toString(),
                                jsonArray.getJSONObject(index).get("name").toString());
                        sources.add(source);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return sources;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Source> sources) {
            super.onPostExecute(sources);
            progressBar.setVisibility(View.INVISIBLE);
            List<String> sourceNames = sources.stream().map(Source::getName).collect(Collectors.toList());
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,
                    sourceNames);
            listView.setAdapter(arrayAdapter);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);

        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)) {
            return true;
        }
        return false;
    }
}
