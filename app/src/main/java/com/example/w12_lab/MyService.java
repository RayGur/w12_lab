package com.example.w12_lab;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyService extends Service {
    private Thread counterThread;
    private boolean isRunning = false;
    private int seconds = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // If thread is already running, don't start another
        if (!isRunning) {
            isRunning = true;

            // Create and start the counter thread
            counterThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isRunning) {
                        try {
                            // Sleep for 1 second
                            Thread.sleep(1000);

                            // Increment seconds
                            seconds++;

                            // Create intent to broadcast
                            Intent broadcastIntent = new Intent("com.example.COUNTER_UPDATE");
                            broadcastIntent.putExtra("seconds", seconds);

                            // Send broadcast
                            sendBroadcast(broadcastIntent);
                        } catch (InterruptedException e) {
                            // Handle interruption
                            Thread.currentThread().interrupt();
                            isRunning = false;
                        }
                    }
                }
            });

            counterThread.start();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Stop the thread when service is destroyed
        isRunning = false;
        if (counterThread != null) {
            counterThread.interrupt();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}