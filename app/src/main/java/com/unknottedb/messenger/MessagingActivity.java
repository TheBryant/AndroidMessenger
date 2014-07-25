package com.unknottedb.messenger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;

import java.util.List;

public class MessagingActivity extends Activity implements ServiceConnection, MessageClientListener {
    public static final String RECIPIENT_ID = "com.unknotted.messenger.recipient_id";
    private static final String TAG = "MessagingActivity";
    private String recipientId;
    private Button sendButton;
    private EditText messageBodyField;
    private MessageService.MessageServiceInterface messageService;
    private MessageAdapter messageAdapter;
    private ListView messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging);
        doBind();

        Intent i = getIntent();
        recipientId = i.getStringExtra(RECIPIENT_ID);
        messageBodyField = (EditText)findViewById(R.id.messageBodyField);

        sendButton = (Button)findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        messageAdapter = new MessageAdapter(this);
        messagesList = (ListView)findViewById(R.id.listMessages);
        messagesList.setAdapter(messageAdapter);
    }
    @Override
    public void onDestroy() {
        unbindService(this);
        super.onDestroy();
    }



    private void doBind(){
        Log.d(TAG, "IN DOBIND");
        Intent serviceIntent = new Intent(this, MessageService.class);
        startService(serviceIntent);
        bindService(serviceIntent, this, BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "IN onServiceConnected");
        messageService = (MessageService.MessageServiceInterface)service;
        messageService.addMessageClientListener(this);
        if (!messageService.isSinchClientStarted()) {
            Toast.makeText(this, "The message client didn't start!",Toast.LENGTH_LONG).show();
        }
    }

    private void sendMessage(){
        String message = messageBodyField.getText().toString();
        Log.d(TAG, "MESS:");
        if (message.isEmpty()){
            Toast.makeText(this, "Enter a message!", Toast.LENGTH_SHORT).show();
        }else {
            messageService.sendMessage(recipientId, message);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        messageService = null;
    }
    @Override
    public void onIncomingMessage(MessageClient messageClient, Message message) {
        messageAdapter.addMessage(message, MessageAdapter.DIRECTION_INCOMING);
    }
    @Override
    public void onMessageSent(MessageClient messageClient, Message message, String s) {
        messageAdapter.addMessage(message, MessageAdapter.DIRECTION_OUTGOING);
    }
    @Override
    public void onMessageFailed(MessageClient messageClient, Message message, MessageFailureInfo messageFailureInfo) {
        Toast.makeText(this, "Message failed to send.", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onMessageDelivered(MessageClient messageClient, MessageDeliveryInfo messageDeliveryInfo) {
        //
    }
    @Override
    public void onShouldSendPushData(MessageClient messageClient, Message message, List<PushPair> pushPairs) {
        //
    }

}
