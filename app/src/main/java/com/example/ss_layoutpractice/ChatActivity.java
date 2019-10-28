package com.example.ss_layoutpractice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatActivity extends AppCompatActivity {

    private EditText EditText_chat;
    private Button Button_send;
    private DatabaseReference myRef;

    private RecyclerView my_recycler_view;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ChatData> chatList;
    private String nick = "real device 유네";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        EditText_chat = findViewById(R.id.EditText_chat);
        Button_send = findViewById (R.id.Button_send);

        Button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = EditText_chat.getText().toString();

                if(msg != null) {
                    ChatData chat = new ChatData();
                    chat.setNickname(nick);
                    chat.setMsg(msg);
                    myRef.push().setValue(chat);
                    EditText_chat.setText("");
                }
            }
        });

        my_recycler_view = findViewById(R.id.my_recycler_view);
        my_recycler_view.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        my_recycler_view.setLayoutManager(layoutManager);

        chatList = new ArrayList<>();
        mAdapter = new ChatAdapter(chatList, ChatActivity.this, nick);
        my_recycler_view.setAdapter(mAdapter);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(/*"message"*/);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("CHATCHAT", dataSnapshot.getValue().toString());

                try{
                     ChatData chat = dataSnapshot.getValue(ChatData.class);
                     ((ChatAdapter) mAdapter).addChat(chat);
                }
                catch(DatabaseException e){
                    //Log the exception and the key
                    dataSnapshot.getKey();
                }

                /*
                line 77:
                Process: com.example.ss_layoutpractice, PID: 18230
                com.google.firebase.database.DatabaseException: Can't convert object of type java.lang.String to type com.example.ss_layoutpractice.ChatData

                에러 발생

                -> try-catch 구문 삽입으로 해결!
                :https://stackoverflow.com/questions/47186438/error-when-getting-an-object-from-datasnapshot
                */


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
        //1. recylcerView - loop
        //2. db내용을 넣는다
        //3. 상대방 폰에 채팅 내용이 보임 - get

        //1-1. recyclerView  = chat data
            //1. message, nickname <- Data Transfer Object
    }
}
