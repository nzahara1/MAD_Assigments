package com.example.homework05;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class NewsActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    ListView listView;
    List<News> news = new ArrayList<>();
    public static String NEWS_URL = "url";
    public static String NEWS_NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Intent intent = getIntent();
        progressDialog = new ProgressDialog(this);
        listView = findViewById(R.id.news_list_view);

        if (intent != null) {
            Source source = (Source) intent.getSerializableExtra(MainActivity.SOURCE);
            setTitle(source.getName());
            new NewsApi().execute("https://newsapi.org/v2/top-headlines?sources=" + source.getId() +
                    "&apiKey=6b43fa14eb7947a5b47d5d149721196c");
        }

    }


    public class NewsApi extends AsyncTask<String, String, List<News>> {

        @Override
        protected List<News> doInBackground(String... strings) {
            publishProgress("start");
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String result = IOUtils.toString(httpURLConnection.getInputStream(), "UTF8");
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("articles");
                    int totalResults = Integer.parseInt(jsonObject.get("totalResults").toString());
                    if (totalResults > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            News news1 = new News(obj.get("title").toString(), obj.get("publishedAt").toString(),
                                    obj.get("urlToImage").toString(), obj.get("author").toString(), obj.get("url").toString());
                            news.add(news1);
                        }
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

            return news;
        }

        @Override
        protected void onPostExecute(final List<News> news) {
            super.onPostExecute(news);
            progressDialog.dismiss();
            CustomAdapter customAdapter = new CustomAdapter(NewsActivity.this, news);
            listView.setAdapter(customAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (news.get(i).getUrl().isEmpty()) {
                        Toast.makeText(NewsActivity.this, "Empty URL cannot be loaded", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent1 = new Intent(NewsActivity.this, WebViewActivity.class);
                    intent1.putExtra(NEWS_URL, news.get(i).getUrl());
                    intent1.putExtra(NEWS_NAME, getTitle());
                    startActivity(intent1);
                }
            });
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            progressDialog.setMessage("Loading Stories...");
            progressDialog.show();
        }
    }
}
