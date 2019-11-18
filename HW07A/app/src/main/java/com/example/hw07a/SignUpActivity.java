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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
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
    String imageUrl;
    String gender;
    static Map<String, StorageReference> map = new HashMap<>();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.username_id);
        password = findViewById(R.id.password);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        avatarimage = findViewById(R.id.avatar);
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
            avatarimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dispatchTakePictureIntent();
                }
            });
            findViewById(R.id.create_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!validateForm()) {
                        return;
                    }
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    User user = new User(firstName.getText().toString(), lastName.getText().toString(), userName.getText().toString(), password.getText().toString(), gender, imageUrl);
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void uploadImage(Bitmap photoBitmap){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();


        final StorageReference imageRepo = storageReference.child("images/camera_" + counter + ".jpg");

//        Converting the Bitmap into a bytearrayOutputstream....
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRepo.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e(TAG, "onFailure: "+e.getMessage());
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Log.d(TAG, "onSuccess: "+"Image Uploaded!!!");
//            }
//        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                return null;
                if (!task.isSuccessful()){
                    throw task.getException();
                }

                return imageRepo.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Log.d("demo", "Image Download URL"+ task.getResult());
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

//        Camera Callback........
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            avatarimage.setImageBitmap(imageBitmap);

            //bitmapUpload = imageBitmap;
            uploadImage(imageBitmap);
        }
    }


}
