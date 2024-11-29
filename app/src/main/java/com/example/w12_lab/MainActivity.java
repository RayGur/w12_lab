package com.example.w12_lab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView counterTextView;
    private Button startButton;
    private CounterReceiver counterReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counterTextView = findViewById(R.id.counterTextView);
        startButton = findViewById(R.id.startButton);

        // Register BroadcastReceiver
        counterReceiver = new CounterReceiver();
        IntentFilter filter = new IntentFilter("com.example.COUNTER_UPDATE");
        registerReceiver(counterReceiver, filter);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the service when button is clicked
                Intent serviceIntent = new Intent(MainActivity.this, MyService.class);
                startService(serviceIntent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the receiver to prevent memory leaks
        if (counterReceiver != null) {
            unregisterReceiver(counterReceiver);
        }
    }

    // Inner class for BroadcastReceiver
    public class CounterReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Check if the intent is our specific broadcast
            if (intent.getAction().equals("com.example.COUNTER_UPDATE")) {
                // Extract the seconds from the intent
                int seconds = intent.getIntExtra("seconds", 0);

                // Update the TextView
                counterTextView.setText(String.valueOf(seconds));
            }
        }
    }
}