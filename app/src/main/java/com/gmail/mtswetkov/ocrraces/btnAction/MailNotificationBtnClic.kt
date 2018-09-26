package com.gmail.mtswetkov.ocrraces.btnAction

import android.content.Context
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import com.gmail.mtswetkov.ocrraces.R
import com.gmail.mtswetkov.ocrraces.ShowSingleRaceActivity
import com.gmail.mtswetkov.ocrraces.model.Event
import com.gmail.mtswetkov.ocrraces.model.SearchRepositoryProvider
import com.gmail.mtswetkov.ocrraces.model.SharedPrefWorker
import com.gmail.mtswetkov.ocrraces.model.Subscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MailNotificationBtnClic {

    private lateinit var subscribeList: MutableList<Subscribe>
    private val repository = SearchRepositoryProvider.provideSearchRepository()
    private var userEmail = ""

    fun click(event: Event, mailNotifBtn: ImageView, userEmailfromSP: String, context: Context, subsList: MutableList<Subscribe>, imageTipe : Int) {
        this.subscribeList = subsList
        if (!event.signed) {
            val builder = AlertDialog.Builder(context)
            val input = EditText(context)
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setTitle(R.string.mail_title)
            builder.setView(input)
            builder.setPositiveButton("OK") { _, _ ->  //dialog, wichButton
                userEmail = input.text.toString()
                repository.subscribe("iQQnMEX22LPn5Ipy4Rxx83zs5", userEmail, event.id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            event.signed = it
                            when(imageTipe){
                                1 -> mailNotifBtn.setImageResource(R.drawable.emailrsm)
                                2 -> mailNotifBtn.setImageResource(R.drawable.emailr)
                            }
                            subscribeList.add(Subscribe(event.id, userEmail))
                            SharedPrefWorker(context).setSubscrtibesList(subscribeList)
                            ShowSingleRaceActivity.userEmail = userEmail
                        }, {
                            println("Error++ ")
                        })
            }
            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() } //_ = which
            builder.create().show()
        } else {
            if(userEmailfromSP != "")userEmail = userEmailfromSP
            repository.unsubscribe("iQQnMEX22LPn5Ipy4Rxx83zs5", userEmail, event.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        event.signed = false
                        when(imageTipe){
                            1 -> mailNotifBtn.setImageResource(R.drawable.emailsm)
                            2 -> mailNotifBtn.setImageResource(R.drawable.email)
                        }
                        val deleteItems: ArrayList<Int> = ArrayList() //war
                        for (sub in subscribeList) {
                            if (sub.id == event.id) deleteItems.add(subscribeList.indexOf(sub))
                        }
                        if (deleteItems.size > 0) {
                            subscribeList.removeAt(deleteItems[0])
                            SharedPrefWorker(context).setSubscrtibesList(subscribeList)
                            deleteItems.clear()
                        }
                    }, {
                        println("Error++ ")
                    })

        }

    }
}

