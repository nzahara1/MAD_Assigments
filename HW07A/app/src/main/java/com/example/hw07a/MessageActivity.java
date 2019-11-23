package com.example.hw07a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    EditText editText;
    String sendUsers;

    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    List<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        setTitle("Chat Bot");
        editText = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.messages_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        Intent intent = getIntent();
        if (intent != null) {
            sendUsers = intent.getStringExtra("send_users");
            sendUsers = sendUsers.replace(LoginActivity.mAuth.getUid(), "");
            readMessage(LoginActivity.mAuth.getUid(), sendUsers);
        }

    }


    public void sendMessage(View view) {
        String message = editText.getText().toString();
        if (message.length() > 0) {
            HashMap<String, String> map = new HashMap<>();
            map.put("sender", LoginActivity.mAuth.getUid());
            map.put("reciever", sendUsers);
            map.put("message", message);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Chats").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    Toast.makeText(MessageActivity.this, "Message sent", Toast.LENGTH_LONG).show();
                    editText.setText("");
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

    private void readMessage(final String myId, final String userId) {
        messages = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Chats").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    messages.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Message message = new Message(documentSnapshot.get("sender").toString(), documentSnapshot.get("reciever").toString(), documentSnapshot.get("message").toString());
                        if (message.getReceiver().contains(myId) && userId.contains(message.getSender()) ||
                                message.getReceiver().contains(userId) && message.getSender().contains(myId)) {
                            messages.add(message);
                        }
                    }
                    Log.d("messages", messages.toString());
                    messageAdapter = new MessageAdapter(MessageActivity.this, messages);
                    recyclerView.setAdapter(messageAdapter);

                } else {
                    Toast.makeText(MessageActivity.this, "Messages could not be fetched", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
