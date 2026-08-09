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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nibm.rebook.CustomAdapter.ChatAdapter;
import com.nibm.rebook.dto.ChatMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    RecyclerView rvChat;
    EditText edtMessage;
    Button btnSend;

    private List<ChatMessage> chatList;
    private ChatAdapter adapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUserId, receiverId, materialId, chatId;

    private final String DATABASE_URL = "https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentUserId = mAuth.getCurrentUser().getUid();

        // Retrieve intent extras passed from Buyer or Seller workflows
        receiverId = getIntent().getStringExtra("RECEIVER_ID");
        materialId = getIntent().getStringExtra("MATERIAL_ID");

        // If receiverId is not directly passed, default or map dynamically
        if (receiverId == null || receiverId.isEmpty()) {
            receiverId = getIntent().getStringExtra("SELLER_ID");
        }

        // Establish unique chat room node based on users/material to keep conversations contextual
        chatId = getIntent().getStringExtra("CHAT_ID");
        if (chatId == null || chatId.isEmpty()) {
            if (materialId != null && !materialId.isEmpty()) {
                chatId = "chat_" + materialId + "_" + currentUserId;
            } else if (receiverId != null && !receiverId.isEmpty()) {
                // Generate a deterministic chat ID between two users
                chatId = currentUserId.compareTo(receiverId) < 0 ?
                        currentUserId + "_" + receiverId : receiverId + "_" + currentUserId;
            } else {
                chatId = "general_chat_" + currentUserId;
            }
        }

        // Initialize Firebase Database Reference
        try {
            mDatabase = FirebaseDatabase.getInstance(DATABASE_URL).getReference("chats").child(chatId);
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
        layoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(layoutManager);
        rvChat.setAdapter(adapter);

        // Read chat messages from Database in real-time
        if (mDatabase != null) {
            mDatabase.child("messages").addValueEventListener(new ValueEventListener() {
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

        // Send Button Logic with Validation & Notification Trigger
        btnSend.setOnClickListener(v -> {
            try {
                String text = edtMessage.getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    if (mDatabase != null) {
                        String messageId = mDatabase.child("messages").push().getKey();
                        long timestamp = System.currentTimeMillis();

                        Map<String, Object> messageMap = new HashMap<>();
                        messageMap.put("messageId", messageId);
                        messageMap.put("senderId", currentUserId);
                        messageMap.put("message", text);
                        messageMap.put("timestamp", timestamp);

                        if (messageId != null) {
                            mDatabase.child("messages").child(messageId).setValue(messageMap)
                                    .addOnSuccessListener(aVoid -> {
                                        // Trigger notification to the receiver if a target ID exists
                                        if (receiverId != null && !receiverId.isEmpty() && !receiverId.equals(currentUserId)) {
                                            sendChatNotification(receiverId, text);
                                        }
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(ChatActivity.this, "Failed to send: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        }
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

    private void sendChatNotification(String targetUserId, String messageText) {
        DatabaseReference notifRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference("notifications").child(targetUserId);
        String notifId = notifRef.push().getKey();

        Map<String, Object> notifData = new HashMap<>();
        notifData.put("id", notifId);
        notifData.put("title", "New Chat Message");
        notifData.put("message", "You received a new message: " + messageText);
        notifData.put("timestamp", System.currentTimeMillis());
        notifData.put("type", "Chat");

        if (notifId != null) {
            notifRef.child(notifId).setValue(notifData);
        }
    }
}