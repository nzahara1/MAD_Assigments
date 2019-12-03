package com.example.hw07a;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    EditText editText;
    String sendUsers;
    String chatRoomname;
    ImageView addimage;
    static String message_image_url;

    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    List<Message> messages;
    int GALLERY_REQUEST_CODE = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        setTitle("Chat Bot");
        editText = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.messages_view);
        addimage = findViewById(R.id.photo_add_button);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        Intent intent = getIntent();
        if (intent != null) {
            sendUsers = intent.getStringExtra("send_users");
            chatRoomname = intent.getStringExtra("chatroomname");
            sendUsers = sendUsers.replace(LoginActivity.mAuth.getUid(), "");
            while(sendUsers.startsWith(",")){
                sendUsers = sendUsers.substring(1);
            }
            while(sendUsers.endsWith(",")){
                sendUsers = sendUsers.substring(0, sendUsers.length()-1);
            }
            readMessage(LoginActivity.mAuth.getUid(), sendUsers, chatRoomname);
        }

        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });

    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        CreateTripActivity.verifyStoragePermissions(this);
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }


    public void sendMessage(View view) {
        String message = editText.getText().toString();
        if (message.length() > 0) {
            HashMap<String, String> map = new HashMap<>();
            map.put("sender", LoginActivity.mAuth.getUid());
            map.put("reciever", sendUsers);
            map.put("message", message);
            map.put("chatroomname", chatRoomname);
            Date date = new Date();
            map.put("sendtime", date.toString());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("chats").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    Toast.makeText(MessageActivity.this, "Message sent", Toast.LENGTH_LONG).show();
                    editText.setText("");
                    readMessage(LoginActivity.mAuth.getUid(), sendUsers, chatRoomname);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        } else {
            Toast.makeText(MessageActivity.this, "Cannot send empty messages", Toast.LENGTH_LONG).show();
        }
    }

    private void readMessage(final String myId, final String userId, final String chatRoomname) {
        messages = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chats").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    messages.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Message message = new Message(documentSnapshot.get("sender").toString(), documentSnapshot.get("reciever").toString(), documentSnapshot.get("message").toString(), documentSnapshot.get("chatroomname").toString(), documentSnapshot.get("sendtime").toString());
                        if (message.getReceiver().contains(myId) && userId.contains(message.getSender()) && message.getChatroomname().equals(chatRoomname) ||
                                message.getReceiver().contains(userId) && message.getSender().contains(myId) && message.getChatroomname().equals(chatRoomname)) {
                            messages.add(message);
                        }
                    }
                    Log.d("messages", messages.toString());
                    Collections.sort(messages, new Comparator<Message>() {
                        public int compare(Message o1, Message o2) {
                            return o1.getDateTime().compareTo(o2.getDateTime());
                        }
                    });

                    messageAdapter = new MessageAdapter(MessageActivity.this, messages);
                    recyclerView.setAdapter(messageAdapter);

                } else {
                    Toast.makeText(MessageActivity.this, "Messages could not be fetched", Toast.LENGTH_LONG).show();
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
            Bitmap photoBitmap = BitmapFactory.decodeFile(imagePath, options);

            //image.setImageBitmap(bitmap);
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();

            String randname = RandomStringUtils.randomAlphanumeric(6).toUpperCase();

            final StorageReference imageRepo = storageReference.child("images/camera_" + randname + ".jpg");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data1 = baos.toByteArray();

            UploadTask uploadTask = imageRepo.putBytes(data1);

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
                        message_image_url = imageURL;
                        SignUpActivity.map.put(imageURL, imageRepo);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("sender", LoginActivity.mAuth.getUid());
                        map.put("reciever", sendUsers);
                        map.put("message", message_image_url);
                        map.put("chatroomname", chatRoomname);
                        Date date = new Date();
                        map.put("sendtime", date.toString());
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("chats").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(MessageActivity.this, "Message sent image", Toast.LENGTH_LONG).show();
                                editText.setText("");
                                readMessage(LoginActivity.mAuth.getUid(), sendUsers, chatRoomname);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                    }
                }
            });



            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();


        }
    }
}
