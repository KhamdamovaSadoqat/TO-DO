package com.example.todo.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.concurrent.Executors

class BootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if(Intent.ACTION_BOOT_COMPLETED == intent.action){
                Executors.newSingleThreadExecutor().execute{
                    if (context != null) {
                        Notification.setNotification(context)
                    }
                }
            }
        }
    }
}