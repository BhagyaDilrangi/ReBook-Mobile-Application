package com.nibm.rebook;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize Views
        rvChat = findViewById(R.id.rvChat);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        // Setup Data
        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList);

        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(adapter);

        // Send Button Logic
        btnSend.setOnClickListener(v -> {
            String text = edtMessage.getText().toString();
            if (!text.isEmpty()) {
                chatList.add(new ChatMessage(text, true));
                adapter.notifyDataSetChanged();
                // Scroll to the latest message
                rvChat.scrollToPosition(chatList.size() - 1);
                edtMessage.setText("");
            }
        });
    }
}