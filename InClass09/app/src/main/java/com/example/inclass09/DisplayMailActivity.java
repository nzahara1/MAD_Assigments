package com.example.inclass09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DisplayMailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mail);
        setTitle("Display Mail");
        
        Intent intent = getIntent();
        if (intent != null) {
            Log.d("rr", "ddd");
            TextView senderName = findViewById(R.id.sender_name_val);
            TextView subject = findViewById(R.id.subject_val);
            TextView message = findViewById(R.id.message_val);
            TextView createdAt = findViewById(R.id.created_val);

            Messages messages = (Messages) intent.getExtras().get("message");

            senderName.setText(messages.getSenderFName() + " " + messages.getSenderLName());
            subject.setText(messages.getSubject());
            message.setText(messages.getMessage());
            createdAt.setText(messages.getCreatedAt());

            findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }
}
