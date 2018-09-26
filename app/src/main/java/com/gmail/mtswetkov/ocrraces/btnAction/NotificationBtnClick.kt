package com.gmail.mtswetkov.ocrraces.btnAction

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.ImageView
import com.gmail.mtswetkov.ocrraces.NotificationsJobScheduler
import com.gmail.mtswetkov.ocrraces.R
import com.gmail.mtswetkov.ocrraces.ShowSingleRaceActivity
import com.gmail.mtswetkov.ocrraces.model.Event
import com.gmail.mtswetkov.ocrraces.model.LocalNotification
import com.gmail.mtswetkov.ocrraces.model.SharedPrefWorker
import java.util.*

class NotificationBtnClick {

    private lateinit var jobInfo: JobInfo
    private lateinit var js: JobScheduler
    lateinit var notifList: MutableList<LocalNotification>

    fun click(event: Event, notifBtn: ImageView, context: Context, notList: MutableList<LocalNotification>,  imageTipe : Int){
        this.notifList = notList
        val temporaryDate: Date? = event.date
        val dateForNotifInSevenDayBefore: Calendar = Calendar.getInstance()
        dateForNotifInSevenDayBefore.set(Calendar.DATE, temporaryDate!!.date - 7)
        dateForNotifInSevenDayBefore.set(Calendar.MONTH, temporaryDate.month)
        dateForNotifInSevenDayBefore.set(Calendar.YEAR, temporaryDate.year)
        val dateForNotifInThreeDayBefore: Calendar = Calendar.getInstance()
        dateForNotifInThreeDayBefore.set(Calendar.DATE, temporaryDate.date - 3)
        dateForNotifInThreeDayBefore.set(Calendar.MONTH, temporaryDate.month)
        dateForNotifInThreeDayBefore.set(Calendar.YEAR, temporaryDate.year)
        val dateForNotifInOneDayBefore: Calendar = Calendar.getInstance()
        dateForNotifInOneDayBefore.set(Calendar.DATE, temporaryDate.date - 1)
        dateForNotifInOneDayBefore.set(Calendar.MONTH, temporaryDate.month)
        dateForNotifInOneDayBefore.set(Calendar.YEAR, temporaryDate.year)
        val oneDate = Date(dateForNotifInOneDayBefore[1], dateForNotifInOneDayBefore[2], dateForNotifInOneDayBefore[5])
        val threeDate = Date(dateForNotifInThreeDayBefore[1], dateForNotifInThreeDayBefore[2], dateForNotifInThreeDayBefore[5])
        val sevenDate = Date(dateForNotifInSevenDayBefore[1], dateForNotifInSevenDayBefore[2], dateForNotifInSevenDayBefore[5])

        if (event.notifications == false) {
            when(imageTipe){
                1 -> notifBtn.setImageResource(R.drawable.bellrsm)
                2 -> notifBtn.setImageResource(R.drawable.bellr)
            }
            val NotifInSevenDayBefore = LocalNotification(event.id, sevenDate, "до гонки 7 дней", event.name)
            val NotifInThreeDayBefore = LocalNotification(event.id, threeDate, "до гонки 3 дня", event.name)
            val NotifInOneDayBefore = LocalNotification(event.id, oneDate, "до гонки 1 день", event.name)
            notifList.add(NotifInOneDayBefore)
            notifList.add(NotifInThreeDayBefore)
            notifList.add(NotifInSevenDayBefore)
            event.notifications = true

            if (NotificationsJobScheduler.started == false) {
                val cn = ComponentName(context, NotificationsJobScheduler::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val notifBuilder: JobInfo.Builder = JobInfo.Builder(ShowSingleRaceActivity.JOB_ID, cn)
                            .setPeriodic(24*60*60*1000)
                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                            .setPersisted(true)
                    jobInfo = notifBuilder.build()
                    js = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                    js.schedule(jobInfo)
                    Log.d("JS_job", js.allPendingJobs[0].intervalMillis.toString())
                } else {
                    val notifBuilder: JobInfo.Builder = JobInfo.Builder(ShowSingleRaceActivity.JOB_ID, cn)
                            .setPeriodic(24*60*60*1000)
                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                            .setPersisted(true)
                    jobInfo = notifBuilder.build()
                    js = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                    js.schedule(jobInfo)
                }
                NotificationsJobScheduler.started = true
            }
        } else {
            when(imageTipe){
                1 -> notifBtn.setImageResource(R.drawable.bellsm)
                2 -> notifBtn.setImageResource(R.drawable.bell)
            }
            val deleteItems: ArrayList<Int> = ArrayList()
            for (notif in notifList) {
                if (notif.raceId == event.id) deleteItems.add(notifList.indexOf(notif))
            }
            if (deleteItems[0] != null) {
                notifList.removeAt(deleteItems[2])
                notifList.removeAt(deleteItems[1])
                notifList.removeAt(deleteItems[0])
                deleteItems.clear()
            }
            event.notifications = false
            NotificationsJobScheduler.started = false
        }
        SharedPrefWorker(context).setNotificationsList(notifList)
    }

}