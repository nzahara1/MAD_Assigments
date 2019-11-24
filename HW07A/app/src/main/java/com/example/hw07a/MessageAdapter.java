package com.example.hw07a;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    List<Message> messages = new ArrayList<Message>();
    Context context;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
        Log.d("messages", messages.toString());
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.MessageViewHolder(view);
        }
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.MessageViewHolder(view);



    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, final int position) {
        final Message message = messages.get(position);
        if(message.getMessage().contains("https")){
            holder.imagetext.setVisibility(View.VISIBLE);
            holder.showMessage.setVisibility(View.INVISIBLE);
            holder.imagetext.setClickable(true);
            holder.imagetext.setMaxHeight(80);
            holder.imagetext.setMaxWidth(80);
            Picasso.get().load(message.getMessage()).into(holder.imagetext);
            holder.imagetext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Open image?");
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(message.getMessage()));
                            context.startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });



                    AlertDialog dialog = builder.create();
                    dialog.show();



                }
            });

            holder.imagetext.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if(message.getSender().equals(LoginActivity.mAuth.getUid())) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Delete message for everyone?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //messages.remove(i);
                                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("chats").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                if(document.getString("message").equals(message.getMessage())) {
                                                    String delete_chat_id = document.getId();
                                                    db.collection("chats").document(delete_chat_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("demo", "DocumentSnapshot successfully deleted!");
                                                        }
                                                    });

                                                }
                                            }
                                        } else {
                                            Log.d("demo", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                                messages.remove(position);
                                notifyDataSetChanged();
                                return;
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }else{
                        Toast.makeText(context, "Cannot delete other user's messages", Toast.LENGTH_LONG).show();
                    }


                    return false;
                }
            });
        }else{
            holder.showMessage.setText(message.getMessage());
            holder.imagetext.setVisibility(View.INVISIBLE);
            holder.showMessage.setVisibility(View.VISIBLE);
            holder.imagetext.setClickable(false);
            holder.showMessage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if(message.getSender().equals(LoginActivity.mAuth.getUid())) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Delete message for everyone?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //messages.remove(i);
                                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("chats").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                if(document.getString("message").equals(message.getMessage())) {
                                                    String delete_chat_id = document.getId();
                                                    db.collection("chats").document(delete_chat_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("demo", "DocumentSnapshot successfully deleted!");
                                                        }
                                                    });

                                                }
                                            }
                                        } else {
                                            Log.d("demo", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                                messages.remove(position);
                                notifyDataSetChanged();
                                return;
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }else{
                        Toast.makeText(context, "Cannot delete other user's messages", Toast.LENGTH_LONG).show();
                    }


                    return false;
                }
            });
        }



        //set image url
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        public View avatar;
        public TextView showMessage;
        public TextView messageBody;
        public ImageView imagetext;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            showMessage = itemView.findViewById(R.id.show_message);
            imagetext = itemView.findViewById(R.id.imagetext);
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) imagetext.getLayoutParams();
            params.width = 400;
            params.height = 400;
            imagetext.setLayoutParams(params);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (LoginActivity.mAuth.getUid().equals(messages.get(position).getSender())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
