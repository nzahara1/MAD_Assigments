package com.example.inclass09;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CreateInboxActivity extends AppCompatActivity {


    private final OkHttpClient client = new OkHttpClient();
    Map<String, String> users = new HashMap<>();
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_inbox);
        setTitle("Create New Email");
        spinner = findViewById(R.id.spinner);
        final String token = getSharedPreferences("prefs", MODE_PRIVATE).getString("token", null);

        try {
            run("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/users", token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spinner.setSelection(0);
        final EditText subject = findViewById(R.id.subject_val);
        final EditText message = findViewById(R.id.message_val);
        findViewById(R.id.cancel_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner.getSelectedItem() == "Users") {
                    Toast.makeText(CreateInboxActivity.this, "Kindly select the recipient user", Toast.LENGTH_LONG).show();
                    return;
                }
                if (subject.getText().toString().isEmpty()) {
                    Toast.makeText(CreateInboxActivity.this, "Subject cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (message.getText().toString().isEmpty()) {
                    Toast.makeText(CreateInboxActivity.this, "Message cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                String date = formatter.format(new Date());
                try {
                    sendEmail("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox/add", token, new Messages(message.getText().toString(), subject.getText().toString(),
                            users.get(spinner.getSelectedItem().toString()), spinner.getSelectedItem().toString(), date));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void sendEmail(String url, String token, Messages messages) throws Exception {
        Log.d("date", messages.getCreatedAt());
        FormBody formBody = new FormBody.Builder().add("receiver_id", messages.getId()).add("subject", messages.getSubject()).
                add("message", messages.getMessage()).build(); //add("created_at", messages.getCreatedAt())
        Request request = new Request.Builder()
                .url(url).addHeader("Authorization", "BEARER " + token)
                .post(formBody)
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
                        CreateInboxActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), response.message(), Toast.LENGTH_LONG).show();
                            }
                        });
                        Log.d("Unexpected code ", response.toString());
                    }
                    CreateInboxActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CreateInboxActivity.this, "Message was sent successfully", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }
            }
        });
    }

    public void run(String url, String token) throws Exception {
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
                        CreateInboxActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), response.message(), Toast.LENGTH_LONG).show();
                            }
                        });
                        throw new IOException("Unexpected code " + response);
                    }
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    if (!jsonObject.get("status").equals("ok")) {
                        Toast.makeText(getBaseContext(), jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("users");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                        users.put(jsonObject1.get("fname") + " " + jsonObject1.get("lname"), jsonObject1.get("id").toString());
                    }
                    CreateInboxActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<String> userList = new ArrayList<>(users.keySet());
                            userList.add(0, "Users");
                            Log.d("users1", userList.toString());
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item,
                                    userList);
                            spinner.setAdapter(arrayAdapter);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
