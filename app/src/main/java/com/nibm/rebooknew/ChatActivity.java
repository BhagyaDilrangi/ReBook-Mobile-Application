package com.nibm.rebooknew;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nibm.rebooknew.CustomAdapter.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    RecyclerView rvChat;
    EditText edtMessage;

    Button btnSend;

    private List<com.nibm.rebooknew.ChatMessage> chatList;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure this matches your XML file name
        setContentView(R.layout.activity_chat);

        // Initialize Views
        RecyclerView rvChat = findViewById(R.id.rvChat);
        EditText edtMessage = findViewById(R.id.edtMessage);
        Button btnSend = findViewById(R.id.btnSend);

        // Setup Data
        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList);

        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(adapter);

        // Send Button Logic
        btnSend.setOnClickListener(v -> {
            String text = edtMessage.getText().toString();
            if (!text.isEmpty()) {
                chatList.add(new com.nibm.rebooknew.ChatMessage(text, true));
                adapter.notifyDataSetChanged();
                // Scroll to the latest message
                rvChat.scrollToPosition(chatList.size() - 1);
                edtMessage.setText("");
            }
        });
    }
}