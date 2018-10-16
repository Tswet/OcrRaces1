package com.gmail.mtswetkov.ocrraces.btnAction

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.support.v7.app.AlertDialog
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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


    fun click(event: Event, notifBtn: ImageView, context: Context, notList: MutableList<LocalNotification>, imageTipe: Int) {

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
            val builder = AlertDialog.Builder(context)
            val dialLayout = LinearLayout(context)
            dialLayout.orientation = LinearLayout.VERTICAL
            val textLine1 = TextView(context)
            val textLine2 = TextView(context)
            val textLine3 = TextView(context)
            val textLineLabel = TextView(context)
            val textLineMsg = TextView(context)
            textLine1.setSingleLine()
            textLine2.setSingleLine()
            textLine3.setSingleLine()
            //textLineLabel.setText(R.string.notif_box_label)
            textLineLabel.text = ""
            textLineMsg.setText(R.string.notif_box_questeon)
            textLine1.setText(R.string.notif_line_1)
            textLine2.setText(R.string.notif_line_2)
            textLine3.setText(R.string.notif_line_3)
            textLineLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,18F)
            textLineLabel.setTypeface(null, Typeface.BOLD)
            textLineMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP,18F)
            textLineMsg.setTypeface(null, Typeface.BOLD)
            //dialLayout.addView(textLineLabel)
            dialLayout.addView(textLineMsg)
            dialLayout.addView(textLineLabel)
            dialLayout.addView(textLine1)
            dialLayout.addView(textLine2)
            dialLayout.addView(textLine3)
            textLineLabel.gravity = Gravity.CENTER_HORIZONTAL
            textLineMsg.gravity = Gravity.CENTER_HORIZONTAL
            textLine1.gravity = Gravity.CENTER_HORIZONTAL
            textLine2.gravity = Gravity.CENTER_HORIZONTAL
            textLine3.gravity = Gravity.CENTER_HORIZONTAL
            dialLayout.setPadding(30, 100, 100, 0)
            builder.setView(dialLayout)
            builder.setPositiveButton("OK") { _, _ ->
                when (imageTipe) {
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
                SharedPrefWorker(context).setNotificationsList(notifList)

                if (NotificationsJobScheduler.started == false) {
                    val cn = ComponentName(context, NotificationsJobScheduler::class.java)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        val notifBuilder: JobInfo.Builder = JobInfo.Builder(ShowSingleRaceActivity.JOB_ID, cn)
                                .setPeriodic(24 * 60 * 60 * 1000)
                                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                                .setPersisted(true)
                        jobInfo = notifBuilder.build()
                        js = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                        js.schedule(jobInfo)
                        Log.d("JS_job", js.allPendingJobs[0].intervalMillis.toString())
                    } else {
                        val notifBuilder: JobInfo.Builder = JobInfo.Builder(ShowSingleRaceActivity.JOB_ID, cn)
                                .setPeriodic(24 * 60 * 60 * 1000)
                                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                                .setPersisted(true)
                        jobInfo = notifBuilder.build()
                        js = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                        js.schedule(jobInfo)
                    }
                    NotificationsJobScheduler.started = true
                }
            }
            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() } //_ = which
            builder.create().show()
        } else {
            when (imageTipe) {
                1 -> notifBtn.setImageResource(R.drawable.bellsm)
                2 -> notifBtn.setImageResource(R.drawable.bell)
            }
            val deleteItems: ArrayList<Int> = ArrayList()
            for (notif in notifList) {
                if (notif.raceId == event.id) deleteItems.add(notifList.indexOf(notif))
            }
            if (deleteItems.size != 0) {
                notifList.removeAt(deleteItems[2])
                notifList.removeAt(deleteItems[1])
                notifList.removeAt(deleteItems[0])
                deleteItems.clear()
                SharedPrefWorker(context).setNotificationsList(notifList)
            }
            event.notifications = false
            NotificationsJobScheduler.started = false
        }
    }
}