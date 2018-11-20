package com.example.kubik.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatActivity extends AppCompatActivity {

    private static final String USER_NAME = "userName";
    private static final String TAG = "myLogs";
    private static final String COLLECTION = "Messages";
    private RecyclerView recyclerView;
    private EditText etInput;
    private Button btnSend;
    
    private String userName;
    private FirebaseFirestore db;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        db  = FirebaseFirestore.getInstance();
        userName = getIntent().getStringExtra(USER_NAME);

        recyclerView = findViewById(R.id.recycler);
        etInput = findViewById(R.id.etInput);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(v -> send());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter();
        adapter.setMessages(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        db.collection(COLLECTION).addSnapshotListener(this::updateChat);
    }
    
    private void send() {
        Message message = new Message(userName, etInput.getText().toString());
        etInput.setText("");

        db.collection(COLLECTION)
                .add(message)
                .addOnSuccessListener(s -> Log.d(TAG, "sendSucces"))
                .addOnFailureListener(f -> Log.d(TAG, "sendFailure: " + f.getMessage()));
    }

    private void updateChat(@Nullable QuerySnapshot snapshots,
                            @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "listen:error", e);
            return;
        }

        if (snapshots != null) {
            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                if (dc.getType() == DocumentChange.Type.ADDED) {
                    Log.d(TAG, "New message: " + dc.getDocument().getData());
                    adapter.addNewMessage(new Message(
                            dc.getDocument().get("userName").toString(),
                            dc.getDocument().get("message").toString(),
                            (long) dc.getDocument().get("date")
                    ));
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout(final Context context) {
        AuthUI.getInstance().signOut(context).addOnSuccessListener(aVoid -> {
            startActivity(MainActivity.makeIntent(context));
            finish();
        });
    }

    public static Intent makeIntent(Context context, String userName) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(USER_NAME, userName);
        return intent;
    }
}
