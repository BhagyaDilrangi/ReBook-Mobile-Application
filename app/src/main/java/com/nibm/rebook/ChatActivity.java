package com.nibm.rebook;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nibm.rebook.CustomAdapter.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    RecyclerView rvChat;
    EditText edtMessage;

    Button btnSend;

    private List<com.nibm.rebook.ChatMessage> chatList;
    private ChatAdapter adapter;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure this matches your XML file name
        setContentView(R.layout.activity_chat);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

<<<<<<< HEAD
=======
        // Initialize Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference("messages");
>>>>>>> 4862004 (Implement successful admin login)

        // Initialize Views
        RecyclerView rvChat = findViewById(R.id.rvChat);
        EditText edtMessage = findViewById(R.id.edtMessage);
        Button btnSend = findViewById(R.id.btnSend);

        // Setup Data
        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList);

        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(adapter);

        // Read from Database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ChatMessage message = postSnapshot.getValue(ChatMessage.class);
                    if (message != null) {
                        chatList.add(message);
                    }
                }
                adapter.notifyDataSetChanged();
                if (!chatList.isEmpty()) {
                    rvChat.scrollToPosition(chatList.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Failed to load messages: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Send Button Logic
        btnSend.setOnClickListener(v -> {
            String text = edtMessage.getText().toString().trim();
            if (!text.isEmpty()) {
<<<<<<< HEAD
                chatList.add(new com.nibm.rebook.ChatMessage(text, true));
                adapter.notifyDataSetChanged();
                // Scroll to the latest message
                rvChat.scrollToPosition(chatList.size() - 1);
=======
                // For now, we set isSentByUser to true for messages sent from this device
                ChatMessage chatMessage = new ChatMessage(text, true);
                mDatabase.push().setValue(chatMessage)
                        .addOnFailureListener(e -> Toast.makeText(ChatActivity.this, "Failed to send: " + e.getMessage(), Toast.LENGTH_SHORT).show());
>>>>>>> 4862004 (Implement successful admin login)
                edtMessage.setText("");
            }
        });
    }
}