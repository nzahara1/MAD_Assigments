package com.example.hw07a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    EditText editText;
    String sendUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        setTitle("Chat Bot");
        editText = findViewById(R.id.editText);
        Intent intent = getIntent();
        if (intent != null) {
            sendUsers = intent.getStringExtra("send_users");
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
}
