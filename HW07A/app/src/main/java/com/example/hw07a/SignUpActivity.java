package com.example.hw07a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    EditText userName;
    EditText password;
    FirebaseAuth auth;
    EditText firstName;
    EditText lastName;
    String imageUrl;
    String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.username_id);
        password = findViewById(R.id.password);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        final RadioGroup radioGroup = findViewById(R.id.radio_grp);
        Intent intent = getIntent();

        if (intent != null) {
            auth = LoginActivity.mAuth;
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    RadioButton radioButton = findViewById(checkedId);
                    Log.d("radio test", radioButton.getText().toString());
                    gender = radioButton.getText().toString();
                }
            });
            findViewById(R.id.create_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!validateForm()) {
                        return;
                    }
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    User user = new User(firstName.getText().toString(), lastName.getText().toString(), userName.getText().toString(), password.getText().toString(), gender);
                    db.collection("users").document(user.getUserName()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("test", "success");
                            } else {

                            }
                        }
                    });
                    createAccount(userName.getText().toString(), password.getText().toString());
                }
            });

        }
    }

    private void createAccount(String email, String password) {
        //   Log.d(TAG, "createAccount:" + email);
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            //FirebaseUser user = auth.getCurrentUser();
                            Log.d("test", "sss");
                            Toast.makeText(SignUpActivity.this, "User is successfully created!", Toast.LENGTH_LONG).show();
                            finish();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //  Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //   updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = this.userName.getText().toString();
        if (TextUtils.isEmpty(email)) {
            userName.setError("Required.");
            valid = false;
        } else {
            userName.setError(null);
        }

        String password = this.password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            this.password.setError("Required.");
            valid = false;
        } else {
            this.password.setError(null);
        }

        if (TextUtils.isEmpty(firstName.getText().toString())) {
            this.firstName.setError("Required.");
            valid = false;
        } else {
            this.firstName.setError(null);
        }


        if (TextUtils.isEmpty(lastName.getText().toString())) {
            this.lastName.setError("Required.");
            valid = false;
        } else {
            this.lastName.setError(null);
        }
        Log.d("gender", gender);
        if (TextUtils.isEmpty(gender)) {
            valid = false;
        }
        return valid;
    }
}
