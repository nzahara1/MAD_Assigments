package com.example.hw07a;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private Context context;

    private List<Trip> tripList;
    String members;

    TripAdapter(Context context, List<Trip> trips) {
        this.context = context;
        this.tripList = trips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trip_row_layout, parent, false);
        return new TripAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Trip trip = tripList.get(position);
        holder.title.setText(trip.getName());
        holder.location.setText(trip.getLat() + "," + trip.getLon());
        holder.chatName.setText(trip.getChatName());
        if (!trip.getImageUrl().isEmpty()) {
            Picasso.get().load(trip.getImageUrl()).into(holder.imageView);
        }
        //hit db and fetch members
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("chatrooms").document(trip.getChatName());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                members = documentSnapshot.getString("members");
                if (members.contains(LoginActivity.mAuth.getUid())) {
                    holder.joinBtn.setText("Already Joined");
                    holder.joinBtn.setClickable(false);
                    holder.joinBtn.setTextSize(8);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Unable to fetch chat members", Toast.LENGTH_LONG).show();
                return;
            }
        });

        holder.joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    HashMap<String, String> map = new HashMap<>();
                    if(!members.contains(LoginActivity.mAuth.getUid())){
                        map.put("members", members + "," + LoginActivity.mAuth.getUid());
                        members = members + "," + LoginActivity.mAuth.getUid();
                    }else{
                        map.put("members", members);
                    }
                    docRef.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            holder.joinBtn.setText("Already Joined");
                            holder.joinBtn.setTextSize(8);
                            holder.joinBtn.setClickable(false);
                            Toast.makeText(context, "Successfully added to the chatroom", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Unable to add to the chatroom", Toast.LENGTH_LONG).show();
                            return;
                        }
                    });
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                //check if user is owner .. if not remove from chat

                if (LoginActivity.mAuth.getUid().contains(trip.getUserId())) {
                    //Logged in user is the owner of trip
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Are you sure of deleting the trip for everyone?");
                            alertDialogBuilder.setPositiveButton("yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                         docRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 db.collection("trips").document(trip.getName()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {
                                                         Toast.makeText(context, "Trip removed successfully", Toast.LENGTH_LONG).show();
                                                         notifyItemRemoved(position);
                                                         return;
                                                     }
                                                 });
                                                 db.collection("chatrooms").document(trip.getChatName()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {
                                                         Toast.makeText(context, "Trip's chatroom removed successfully", Toast.LENGTH_LONG).show();
                                                         return;
                                                     }
                                                 });
                                             }
                                         });
                                        }
                                    }).setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }}).create().show();
                } else {
                    if (members.contains(LoginActivity.mAuth.getUid())){
                        //not the owner, check if he is a member
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setMessage("Are you sure of removing this trip from your account?");
                        alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                members = members.replace(LoginActivity.mAuth.getUid(), "");
                                HashMap<String, String> map = new HashMap<>();
                                map.put("members", members);
                                docRef.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Successfully removed from the chat!", Toast.LENGTH_LONG).show();
                                        holder.joinBtn.setText("Join");
                                        holder.joinBtn.setClickable(true);
                                        return;
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Unable to remove from chat", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                });

                            }
                        }).setNegativeButton("No",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }}).create().show();

                    }else{
                        Toast.makeText(context, "Not a member of the chat to remove", Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title;
        TextView location;
        Button joinBtn;
        TextView chatName;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.trip_cover);
            title = itemView.findViewById(R.id.title_val);
            location = itemView.findViewById(R.id.location_val);
            joinBtn = itemView.findViewById(R.id.join_btn);
            chatName = itemView.findViewById(R.id.chat_val);
        }
    }

}
