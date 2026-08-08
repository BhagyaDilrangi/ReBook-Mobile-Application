package com.nibm.rebook;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
import java.util.List;

public class BuyerChat extends AppCompatActivity {

    private TextView txtSellerName;
    private RecyclerView rvMessages;
    private EditText edtMessage;
    private ImageButton btnSend;
    
    private ChatAdapter adapter;
    private List<ChatMessage> chatList;
    
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String sellerId, materialId, chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_buyer_chat_with_saller);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            finish();
            return;
        }

        // Get data from intent
        sellerId = getIntent().getStringExtra("SELLER_ID");
        materialId = getIntent().getStringExtra("MATERIAL_ID");
        String sellerName = getIntent().getStringExtra("SELLER_NAME");
        String buyerId = mAuth.getCurrentUser().getUid();

        // Unique Chat ID for this buyer-seller-material combination
        if (sellerId != null && materialId != null) {
            chatId = buyerId + "_" + sellerId + "_" + materialId;
        } else {
            chatId = "general_support_" + buyerId;
        }

        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("chats").child(chatId);

        txtSellerName = findViewById(R.id.txtSellerName);
        rvMessages = findViewById(R.id.rvChatMessages);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        if (sellerName != null) txtSellerName.setText(sellerName);

        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(adapter);

        fetchMessages();

        btnSend.setOnClickListener(v -> {
            String msgText = edtMessage.getText().toString().trim();
            if (!msgText.isEmpty()) {
                sendMessage(msgText, buyerId);
            }
        });
    }

    private void fetchMessages() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    ChatMessage msg = data.getValue(ChatMessage.class);
                    if (msg != null) {
                        chatList.add(msg);
                    }
                }
                adapter.notifyDataSetChanged();
                if (!chatList.isEmpty()) {
                    rvMessages.scrollToPosition(chatList.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BuyerChat.this, "Chat Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String text, String senderId) {
        String msgId = mDatabase.push().getKey();
        ChatMessage message = new ChatMessage(text, senderId, sellerId, System.currentTimeMillis());
        
        if (msgId != null) {
            mDatabase.child(msgId).setValue(message).addOnSuccessListener(aVoid -> {
                edtMessage.setText("");
            });
        }
    }
}
