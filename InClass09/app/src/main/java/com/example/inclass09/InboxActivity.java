package com.example.inclass09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class InboxActivity extends AppCompatActivity {

    String token;
    private final OkHttpClient client = new OkHttpClient();
    List<Messages> messages = new ArrayList<>();
    ListView listView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        setTitle("Inbox");
        token = getSharedPreferences("prefs", MODE_PRIVATE).getString("token", null);
        TextView name = findViewById(R.id.inbox_username);
        name.setText(getSharedPreferences("prefs", MODE_PRIVATE).getString("user_name", null));
        listView = findViewById(R.id.list_view);
        listView.setFocusable(false);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.d("click", "dd");
////                                        Intent intent = new Intent(InboxActivity.this, DisplayMailActivity.class);
////                                        intent.putExtra("message", messages.get(i).toString());
////                                        startActivity(intent);
//            }
//        });
        try {
            run("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox");
        } catch (Exception e) {
            e.printStackTrace();
        }
        findViewById(R.id.inbox_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InboxActivity.this, CreateInboxActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.inbox_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSharedPreferences("prefs", MODE_PRIVATE).edit().remove("token").remove("user_name").commit();
                finish();
            }
        });
    }

    public void run(String url) throws Exception {
        Request request = new Request.Builder()
                .url(url).addHeader("Authorization", "BEARER " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        InboxActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), response.message(), Toast.LENGTH_LONG).show();
                            }
                        });
                        throw new IOException("Unexpected code " + response);
                    }
                    String json = responseBody.string();
                    JSONObject jsonObject = new JSONObject(json);
                    Log.d("login", jsonObject.toString());
                    if (!jsonObject.get("status").equals("ok")) {
                        Toast.makeText(InboxActivity.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("messages");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                        Messages messages1 = new Messages(jsonObject1.getString("sender_fname"), jsonObject1.getString("sender_lname"),
                                jsonObject1.getString("message"), jsonObject1.getString("subject"), jsonObject1.getString("created_at"), jsonObject1.get("id").toString());
                        messages.add(messages1);
                    }
                    InboxActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (messages.size() > 0) {
                                ListAdapter listAdapter = new ListAdapter(InboxActivity.this, R.layout.row_layout, messages);
                                listView.setAdapter(listAdapter);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            run("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
