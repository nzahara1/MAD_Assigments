package com.example.hw07a;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;


public class CreateTripActivity extends AppCompatActivity {

    int GALLERY_REQUEST_CODE = 201;
    ImageView image;
    String trip_image_url;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        final EditText name = findViewById(R.id.trip_name);
        image = findViewById(R.id.trip_photo);
        final EditText lat = findViewById(R.id.lat_id);
        final EditText lon = findViewById(R.id.lon_id);
        final EditText chatName = findViewById(R.id.chat_name);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });

        Intent intent = getIntent();

        if (intent != null) {

            findViewById(R.id.trip_create_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // store the trip
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final Trip trip = new Trip(trip_image_url, name.getText().toString(), lat.getText().toString(),
                            lon.getText().toString(), chatName.getText().toString(), user.getUid());

                    db.collection("trips").document(trip.getName()).set(trip).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // logic for members
                                HashMap<String, String> map = new HashMap<>();
                                map.put("members", user.getUid());
                                db.collection("chatrooms").document(trip.getChatName()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(CreateTripActivity.this, "Trip was successfully created!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                finish();
                            } else {
                                Toast.makeText(CreateTripActivity.this, "There was a probem while creating the trip!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        verifyStoragePermissions(this);
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void uploadImage(Bitmap photoBitmap) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        String randname = RandomStringUtils.randomAlphanumeric(6).toUpperCase();

        final StorageReference imageRepo = storageReference.child("images/camera_" + randname + ".jpg");

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
                    trip_image_url = imageURL;
                    SignUpActivity.map.put(imageURL, imageRepo);
                    //Picasso.get().load(imageURL).into(avatarimage);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            image.setImageBitmap(bitmap);
            uploadImage(bitmap);


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();


        }
    }
}
