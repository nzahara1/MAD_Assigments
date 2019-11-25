package com.example.hw07a;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatsFragment extends Fragment {

    ListView listView;
    String members;
    Map<String, String> chatRooms;
    ArrayList<String> chatNames;
    ArrayAdapter<String> arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        listView = view.findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String chat = chatNames.get(i);
                Intent intent = new Intent(getContext(), MessageActivity.class);
                intent.putExtra("send_users", chatRooms.get(chat));
                intent.putExtra("chatroomname", chat);
                startActivity(intent);
            }
        });
        chatRooms = new HashMap<>();
        getChatRooms();
        return view;
    }

    public void refresh(){
        chatRooms = new HashMap<>();
        getChatRooms();
    }

    private void getChatRooms() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chatrooms").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        if (data.get("members").toString().contains(LoginActivity.mAuth.getUid())) {
                            chatRooms.put(documentSnapshot.getId(), data.get("members").toString());
                        }
                    }
                    chatNames = new ArrayList<String>(chatRooms.keySet());
                    arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, chatNames);
                    listView.setAdapter(arrayAdapter);


                } else {
                    Toast.makeText(getContext(), "Trip details could not be fetched", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
