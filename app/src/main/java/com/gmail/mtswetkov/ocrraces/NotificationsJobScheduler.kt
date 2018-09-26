package com.gmail.mtswetkov.ocrraces

import android.app.*
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import com.gmail.mtswetkov.ocrraces.model.LocalNotification
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class NotificationsJobScheduler : JobService() {

    private lateinit var mNotification: Notification
    private lateinit var alarmIntent: PendingIntent
    private lateinit var prefs: SharedPreferences
    val gson = Gson()
    private var notificationManager: NotificationManager? = null
    private lateinit var channel: NotificationChannel
    private lateinit var notifList: MutableList<LocalNotification> //= mutableListOf(LocalNotification(0, Date(2017, 6, 11), "", ""))


    companion object {
        var started: Boolean = false
        const val CHANNEL_ID = "com.google.MTsvetkov.CHANNEL_ID"
        var notificationID = 101
        const val CHANNEL_NAME = "OCR Notification"
        const val CHANNEL_DISC = "The race is coming"
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("JSON_today", "started")
        sendNotification()
        jobFinished(params, false)
        return true
    }

    private fun sendNotification() {
        val intent = Intent(this, MainActivity::class.java)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        alarmIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        //Get Notification list from SharedPreferences
        prefs = this.getSharedPreferences(ShowSingleRaceActivity().PREFS_FILENAME, 0)
        val jsonPerf: String = prefs.getString(ShowSingleRaceActivity().NOTIFICATION_OBJECTS, "")
        if (jsonPerf != "") notifList = gson.fromJson(jsonPerf, object : TypeToken<MutableList<LocalNotification>>() {}.type)

        //Check the date
        val today = DateFormat.format("yyyy-MM-dd", Calendar.getInstance())
        for (notif in notifList) {
            val raceday = DateFormat.format("yyyy-MM-dd", notif.notifDate)
            if (today.toString() == raceday.toString()) {
                createNotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        CHANNEL_DISC)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mNotification = Notification.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.bellr)
                            .setContentTitle(notif.raceName)
                            .setContentText(notif.message)
                            .setContentIntent(alarmIntent)
                            .setAutoCancel(true)
                            .build()
                    notificationID++
                } else {
                    mNotification = Notification.Builder(this)
                            .setSmallIcon(R.drawable.bellr)
                            .setAutoCancel(true)
                            .setContentTitle(notif.raceName)
                            .setContentText(notif.message)
                            .setContentIntent(alarmIntent)
                            .setAutoCancel(true)
                            .build()
                }
                notificationManager?.notify(notificationID, mNotification)
            }
        }
    }

    private fun createNotificationChannel(id: String, name: String,
                                          description: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            channel = NotificationChannel(id, name, importance)
            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern =
                    longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}