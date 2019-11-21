package com.example.hw07a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText userName;
    EditText password;
    FirebaseAuth auth;
    EditText firstName;
    EditText lastName;
    ImageView avatarimage;
    String imageUrl = "";
    String gender;
    static Map<String, StorageReference> map = new HashMap<>();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    int counter = 0;
    RadioGroup radioGroup;
    Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.username_id);
        password = findViewById(R.id.password);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        avatarimage = findViewById(R.id.avatar);
        radioGroup = findViewById(R.id.radio_grp);
        createBtn = findViewById(R.id.create_btn);
        Intent intent = getIntent();

        if (intent != null) {
            auth = LoginActivity.mAuth;
            User user = (User) intent.getSerializableExtra("users");
            if (user != null) {
                updateUser(user);
            }
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    RadioButton radioButton = findViewById(checkedId);
                    gender = radioButton.getText().toString();
                }
            });
            avatarimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dispatchTakePictureIntent();
                }
            });
            createBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!validateForm()) {
                        return;
                    }
                    if (auth.getCurrentUser() != null) {
                        Log.d("image", imageUrl);
                        updateDatabase();
                    }
                    createAccount(userName.getText().toString(), password.getText().toString());
                }
            });

        }
    }

    private void updateDatabase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        User user = new User(firstName.getText().toString(), lastName.getText().toString(), userName.getText().toString(), password.getText().toString(), gender, imageUrl);
        db.collection("users").document(auth.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("test", "success");
                } else {

                }
            }
        });
        Toast.makeText(SignUpActivity.this, "User profile is successfully updated!", Toast.LENGTH_LONG).show();
        finish();
    }


    private void updateUser(User user) {
        userName.setText(user.getUserName());
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        if (!user.getAvatar_url().isEmpty()) {
            Picasso.get().load(user.getAvatar_url()).into(avatarimage);
        }
        imageUrl = user.getAvatar_url();
        password.setText(user.getPassword());
        if (user.getGender().equals("Female")) {
            radioGroup.check(R.id.female_btn);
            gender = "Female";
        } else {
            radioGroup.check(R.id.male_btn);
            gender = "Male";
        }
        createBtn.setText("Update");
    }

    private void createAccount(final String email, final String userPassword) {
        if (auth.getCurrentUser() == null) {
            auth.createUserWithEmailAndPassword(email, userPassword)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                signIn(email, userPassword);
                            } else {
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
            });
        }
    }

    private void signIn(String email, String userPassword) {
        auth.signInWithEmailAndPassword(email, userPassword)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            User user = new User(firstName.getText().toString(), lastName.getText().toString(), userName.getText().toString(), password.getText().toString(), gender, imageUrl);
                            db.collection("users").document(auth.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("test", "success");
                                    } else {

                                    }
                                }
                            });
                            Toast.makeText(SignUpActivity.this, "User is successfully created!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        });
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
        // 6 character validation
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
        if (TextUtils.isEmpty(gender)) {
            valid = false;
        }
        return valid;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void uploadImage(Bitmap photoBitmap) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();


        final StorageReference imageRepo = storageReference.child("images/camera_" + counter + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRepo.putBytes(data);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return imageRepo.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Log.d("demo", "Image Download URL" + task.getResult());
                    String imageURL = task.getResult().toString();
                    imageUrl = imageURL;
                    map.put(imageURL, imageRepo);
                    //Picasso.get().load(imageURL).into(avatarimage);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            avatarimage.setImageBitmap(imageBitmap);
            uploadImage(imageBitmap);
        }
    }


}
