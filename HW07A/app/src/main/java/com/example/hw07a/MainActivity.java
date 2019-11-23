package com.example.hw07a;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Message;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements RoomListener {

    private String channelID = "MyGKIeU5M9Kpot9j";
    private String roomName = "observable-room";
    private EditText editText;
    private Scaledrone scaledrone;
    ListView messagesView;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        editText = (EditText) findViewById(R.id.editText);
//        messagesView = findViewById(R.id.messages_view);
//        messageAdapter = new MessageAdapter(MainActivity.this);
//        messagesView.setAdapter(messageAdapter);
//        MemberData data = new MemberData(getRandomName(), getRandomColor());
//
//        scaledrone = new Scaledrone(channelID, data);
//        scaledrone.connect(new Listener() {
//            @Override
//            public void onOpen() {
//                System.out.println("Scaledrone connection open");
//                // Since the MainActivity itself already implement RoomListener we can pass it as a target
//                scaledrone.subscribe(roomName, MainActivity.this);
//            }
//
//            @Override
//            public void onOpenFailure(Exception ex) {
//                System.err.println(ex);
//            }
//
//            @Override
//            public void onFailure(Exception ex) {
//                System.err.println(ex);
//            }
//
//            @Override
//            public void onClosed(String reason) {
//                System.err.println(reason);
//            }
//        });
    }

    @Override
    public void onOpen(Room room) {
        System.out.println("Conneted to room");
    }

    @Override
    public void onOpenFailure(Room room, Exception ex) {
        System.err.println(ex);
    }

    @Override
    public void onMessage(Room room, Message receivedMessage) {
//        final ObjectMapper mapper = new ObjectMapper();
//        try {
//            // member.clientData is a MemberData object, let's parse it as such
//            final MemberData data = mapper.treeToValue(receivedMessage.getMember().getClientData(), MemberData.class);
//            // if the clientID of the message sender is the same as our's it was sent by us
//            boolean belongsToCurrentUser = receivedMessage.getClientID().equals(scaledrone.getClientID());
//            // since the message body is a simple string in our case we can use json.asText() to parse it as such
//            // if it was instead an object we could use a similar pattern to data parsing
//            final com.example.hw07a.Message message = new com.example.hw07a.Message(receivedMessage.getData().asText(), data, belongsToCurrentUser);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    messageAdapter.add(message);
//                    // scroll the ListView to the last added element
//                    messagesView.setSelection(messagesView.getCount() - 1);
//                }
//            });
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

    }

    private String getRandomName() {
        String[] adjs = {"autumn", "hidden", "bitter", "misty", "silent", "empty", "dry", "dark", "summer", "icy", "delicate", "quiet", "white", "cool", "spring", "winter", "patient", "twilight", "dawn", "crimson", "wispy", "weathered", "blue", "billowing", "broken", "cold", "damp", "falling", "frosty", "green", "long", "late", "lingering", "bold", "little", "morning", "muddy", "old", "red", "rough", "still", "small", "sparkling", "throbbing", "shy", "wandering", "withered", "wild", "black", "young", "holy", "solitary", "fragrant", "aged", "snowy", "proud", "floral", "restless", "divine", "polished", "ancient", "purple", "lively", "nameless"};
        String[] nouns = {"waterfall", "river", "breeze", "moon", "rain", "wind", "sea", "morning", "snow", "lake", "sunset", "pine", "shadow", "leaf", "dawn", "glitter", "forest", "hill", "cloud", "meadow", "sun", "glade", "bird", "brook", "butterfly", "bush", "dew", "dust", "field", "fire", "flower", "firefly", "feather", "grass", "haze", "mountain", "night", "pond", "darkness", "snowflake", "silence", "sound", "sky", "shape", "surf", "thunder", "violet", "water", "wildflower", "wave", "water", "resonance", "sun", "wood", "dream", "cherry", "tree", "fog", "frost", "voice", "paper", "frog", "smoke", "star"};
        return (
                adjs[(int) Math.floor(Math.random() * adjs.length)] +
                        "_" +
                        nouns[(int) Math.floor(Math.random() * nouns.length)]
        );
    }

    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while (sb.length() < 7) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        Log.d("color",sb.toString().substring(0, 7));
        return sb.toString().substring(0, 7);
    }

    public void sendMessage(View view) {
        String message = editText.getText().toString();
        if (message.length() > 0) {
            scaledrone.publish("observable-room", message);
            editText.getText().clear();
        }
    }

}
