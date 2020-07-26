package com.example.interest.ui.messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.interest.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
public class MessengerActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    EditText mEditTextMessages;
    Button mSendButton;
    RecyclerView mMessagerRecycler;
    ArrayList<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String chatId = getIntent().getStringExtra("chatId");
        this.myRef  = database.getReference("messages/" + chatId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        mSendButton = findViewById(R.id.send_message_b);
        mEditTextMessages =findViewById((R.id.message_input));
        mMessagerRecycler = findViewById(R.id.messaging_recycler);
        mMessagerRecycler.setLayoutManager(new LinearLayoutManager(this));
        final DataAdapter dataAdapter = new DataAdapter(this, messages);
        mMessagerRecycler.setAdapter(dataAdapter);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = mEditTextMessages.getText().toString();
                if(msg.equals("")){
                    Toast.makeText(getApplicationContext(), "Input your message", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(msg.length() > 1000){
                    Toast.makeText(getApplicationContext(), "Too many symbols", Toast.LENGTH_SHORT).show();
                    return;
                }
                myRef.push().setValue(msg);
                mEditTextMessages.setText("");
            }



        });


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String msg = dataSnapshot.getValue(String.class);
                messages.add(msg);
                dataAdapter.notifyDataSetChanged();
                mMessagerRecycler.smoothScrollToPosition(messages.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}