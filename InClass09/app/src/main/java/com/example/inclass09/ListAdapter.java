package com.example.inclass09;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ListAdapter extends ArrayAdapter<Messages> {


    List<Messages> messagesList;
    private final OkHttpClient client = new OkHttpClient();

    public ListAdapter(@NonNull Context context, int resource, @NonNull List<Messages> messages) {
        super(context, resource, messages);
        this.messagesList = messages;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Messages message = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.emailSummary = convertView.findViewById(R.id.email_summary);
            viewHolder.emailDate = convertView.findViewById(R.id.email_date);
            viewHolder.imageButton = convertView.findViewById(R.id.delete_btn);
            convertView.setTag(viewHolder);
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.emailSummary.setText(message.getSubject());
        viewHolder.emailDate.setText(message.getCreatedAt());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DisplayMailActivity.class);
                intent.putExtra("message", message);
                getContext().startActivity(intent);
            }
        });

        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    deleteEmail("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox/delete/" + message.getId(), getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE).getString("token", null));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                return;
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView emailDate;
        TextView emailSummary;
        ImageButton imageButton;
    }


    public void deleteEmail(String url, String token) throws Exception {
        Request request = new Request.Builder()
                .url(url).addHeader("Authorization", "BEARER " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                        Log.d("Unexpected code ", response.toString());
                    }
                    JSONObject jsonObject = new JSONObject(responseBody.string());

                    if (jsonObject.get("status").equals("ok")) {
                        Toast.makeText(getContext(), "Message was deleted successfully", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
