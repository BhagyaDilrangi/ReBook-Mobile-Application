package com.nibm.rebook;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.nibm.rebook.dto.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    RecyclerView rvChat;
    EditText edtMessage;
    Button btnSend;

    private List<ChatMessage> chatList;
    private ChatAdapter adapter;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize Firebase Realtime Database safely with exception handling
        try {
            mDatabase = FirebaseDatabase.getInstance().getReference("messages");
        } catch (Exception e) {
            Toast.makeText(this, "Database Connection Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            mDatabase = null;
        }

        // Initialize Views
        rvChat = findViewById(R.id.rvChat);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        // Setup Data and Adapter
        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvChat.setLayoutManager(layoutManager);
        rvChat.setAdapter(adapter);

        // Read from Database with robust error handling
        if (mDatabase != null) {
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
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
                    } catch (Exception e) {
                        Toast.makeText(ChatActivity.this, "Data parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ChatActivity.this, "Failed to load messages: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Send Button Logic with Validation & Exception Handling
        btnSend.setOnClickListener(v -> {
            try {
                String text = edtMessage.getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    if (mDatabase != null) {
                        ChatMessage chatMessage = new ChatMessage(text, true);
                        mDatabase.push().setValue(chatMessage)
                                .addOnFailureListener(e -> Toast.makeText(ChatActivity.this, "Failed to send: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        edtMessage.setText("");
                    } else {
                        Toast.makeText(ChatActivity.this, "Database reference is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    edtMessage.setError("Message cannot be empty");
                }
            } catch (Exception e) {
                Toast.makeText(ChatActivity.this, "Error sending message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}