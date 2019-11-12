package com.example.inclass09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Mailer");

        if (!isConnected()) {
            Toast.makeText(MainActivity.this, "Internet is not Connected", Toast.LENGTH_LONG).show();
            return;
        }
        String token = getSharedPreferences("prefs", MODE_PRIVATE).getString("token", null);
        final EditText email = findViewById(R.id.email_id);
        final EditText password = findViewById(R.id.password_id);

        if (token == null) {
            email.setText("");
            password.setText("");
        } else {
            Intent intent = new Intent(MainActivity.this, InboxActivity.class);
            startActivity(intent);
        }

        findViewById(R.id.login_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call login api

                if (email.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Email cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Password cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                Login login = new Login(email.getText().toString(), password.getText().toString());
                Gson gson = new Gson();

                try {
                    run("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/login", gson.toJson(login));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        findViewById(R.id.signup_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void run(String url, String json) throws Exception {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url).post(body)
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
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_LONG).show();
                            }
                        });
                        throw new IOException("Unexpected code " + response);
                    }
                    Gson gson = new Gson();
                    Login login = gson.fromJson(responseBody.string(), Login.class);
                    Log.d("login", login.toString());
                    if (!login.getStatus().equals("ok")) {
                        Toast.makeText(getBaseContext(), login.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    getSharedPreferences("prefs", MODE_PRIVATE).edit().putString("token", login.getToken()).
                            putString("user_name", login.getFirstName() + " " + login.getLastName()).commit();
                    Intent intent = new Intent(MainActivity.this, InboxActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }
}
