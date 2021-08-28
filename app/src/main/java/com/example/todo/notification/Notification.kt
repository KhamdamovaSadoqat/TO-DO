package com.example.todo.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.todo.MainActivity
import com.example.todo.R
import com.example.todo.utils.DataTimeUtils
import java.lang.IllegalStateException

const val CHANNEL_ID = "alarm_channel"
const val CHANNEL_NAME = "todo"

const val BUNDLE_EXTRA = "bundle_extra"
const val ALARM_KEY = "alarm_key"

const val NOTIFICATION_ID = 1000

class Notification : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val manager =
            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        createNotificationChannel(context)

        var mBitmap: Bitmap? = ContextCompat.getDrawable(
            context,
            R.mipmap.ic_launcher
        )?.toBitmap()

        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setLargeIcon(mBitmap)
        builder.setShowWhen(true)
        builder.color = ContextCompat.getColor(context, R.color.white)
        builder.setContentTitle("Time to do some jooob")
        builder.setChannelId(CHANNEL_ID)
        builder.setVibrate(longArrayOf(1000, 500, 1000, 500, 1000, 500))
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        builder.setContentIntent(launchAlarmLandingPage(context))
        builder.setAutoCancel(true)
        builder.setContentTitle("Qaysidr ishi vohti boldi lekin bu haqida keyinroq")
        mBitmap = ContextCompat.getDrawable(context, R.drawable.ic_baseline_star_border_24)?.toBitmap()
        builder.setLargeIcon(mBitmap)
        manager.notify(NOTIFICATION_ID, builder.build())

    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val notificationManager: NotificationManager =
            context.getSystemService(NotificationManager::class.java) ?: return

        if (notificationManager.getNotificationChannel(CHANNEL_NAME) == null) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.setBypassDnd(true)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun launchAlarmLandingPage(context: Context): PendingIntent? {
        return PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            launchIntent(context),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun launchIntent(context: Context?): Intent {
        val i = Intent(
            context,
            MainActivity::class.java
        )
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        return i
    }

    companion object{
        private val dateTimeUtils = DataTimeUtils()
        private var time: String? = ""
        private var date: String? = ""

        fun setNotification(context: Context){
            setRemainderNotification(context)
        }

        fun setTime(date: String?, time: String?){
            this.time = time
            this.date = date
        }

        private fun setRemainderNotification(context: Context){

            val time: Long = dateTimeUtils.timeToLong(date, time)

            val intent = Intent(context, Notification::class.java)
            val bundle = Bundle()
            bundle.putLong(ALARM_KEY, time)
            intent.putExtra(BUNDLE_EXTRA, bundle)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            Log.d("-------------", "setRemainderNotification: exact time: $time")
            ScheduleNotification.with(context).schedule(time, pendingIntent)

//            Log.d("-------------", "setRemainderAlarm: ")
//
//            val currentTime = System.currentTimeMillis()+2000
//            Log.d("-------------", "setRemainderAlarm: current : $currentTime")
//            ScheduleNotification.with(context).schedule(currentTime, pendingIntent)
        }
    }

    private class ScheduleNotification private constructor(
        private val am: AlarmManager
    ){
        fun schedule(time: Long, pi: PendingIntent?){
            Log.d("--------", "schedule: working")
//            Log.d("--------", "schedule: time: $time")
            am.setExact(AlarmManager.RTC, time, pi)
        }

        companion object{
            fun with(context: Context): ScheduleNotification{
                val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
                    ?: throw  IllegalStateException("alarm manager not found")
                return  ScheduleNotification(am)
            }
        }
    }
}