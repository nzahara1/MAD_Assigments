package com.example.inclass09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SignUpActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        final EditText firstName = findViewById(R.id.first_name);
        final EditText lastName = findViewById(R.id.last_name);
        final EditText email = findViewById(R.id.email_signup);
        final EditText password = findViewById(R.id.password_signup);
        final EditText rPassword = findViewById(R.id.r_password_signup);
        findViewById(R.id.signup_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstName.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "First name cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                if (lastName.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Last name cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (email.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Email cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Password cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (rPassword.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Repeat Password cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.getText().toString().equals(rPassword.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "Password and repeat password mismatch", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    run("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/signup", new SignUp(firstName.getText().toString(), lastName.getText().toString(),
                            email.getText().toString(), password.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void run(String url, final SignUp signUp) throws Exception {

        FormBody formBody = new FormBody.Builder().add("email", signUp.getEmail()).add("password", signUp.getPassword()).
                add("fname", signUp.getFirstName()).add("lname", signUp.getLastName()).build();
        final String firstName = signUp.getFirstName();
        final String lastName = signUp.getLastName();
        Request request = new Request.Builder()
                .url(url).post(formBody)
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
                        SignUpActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), response.message(), Toast.LENGTH_LONG).show();
                            }
                        });
                        throw new IOException("Unexpected code " + response);
                    }
                    Gson gson = new Gson();
                    Login login = gson.fromJson(responseBody.string(), Login.class);
                    if (!login.getStatus().equals("ok")) {
                        Toast.makeText(getApplicationContext(), login.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    getSharedPreferences("prefs", MODE_PRIVATE).edit().putString("token", login.getToken()).
                            putString("user_name", firstName + " " + lastName).commit();
                    SignUpActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignUpActivity.this, "User was successfully created!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignUpActivity.this, InboxActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }
}
