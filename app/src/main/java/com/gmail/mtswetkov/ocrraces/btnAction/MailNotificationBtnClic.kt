package com.gmail.mtswetkov.ocrraces.btnAction

import android.content.Context
import android.support.v7.app.AlertDialog
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.gmail.mtswetkov.ocrraces.utils.EmailValidator
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

    fun click(event: Event, mailNotifBtn: ImageView, userEmailfromSP: String, context: Context, subsList: MutableList<Subscribe>, imageTipe: Int) {
        this.subscribeList = subsList
        if (!event.signed) {
            val builder = AlertDialog.Builder(context)
            val dialLayout = LinearLayout(context)
            val input = EditText(context)
            input.setSingleLine()
            input.width = 1800
            input.hint = "email@domain.com"
            dialLayout.addView(input)
            dialLayout.setPadding(45, 0, 36, 0)
            builder.setTitle(R.string.mail_title)
            builder.setMessage(R.string.mail_dialog_message)
            builder.setView(dialLayout)
            builder.setPositiveButton("Подписаться") { _, _ ->
                //dialog, wichButton
                if (EmailValidator.isEmailValid(input.text.toString())) {
                    userEmail = input.text.toString().trim()
                    repository.subscribe("iQQnMEX22LPn5Ipy4Rxx83zs5", userEmail, event.id)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({
                                event.signed = it
                                when (imageTipe) {
                                    1 -> mailNotifBtn.setImageResource(R.drawable.emailrsm)
                                    2 -> mailNotifBtn.setImageResource(R.drawable.emailr)
                                }
                                subscribeList.add(Subscribe(event.id, userEmail))
                                SharedPrefWorker(context).setSubscrtibesList(subscribeList)
                                ShowSingleRaceActivity.userEmail = userEmail
                            }, {
                                println("Error++ ")
                            })
                }else{
                    Toast.makeText(context, "Введите корректный email", Toast.LENGTH_LONG).show()
                }
            }
            builder.setNegativeButton("Отмена") { dialog, _ -> dialog.cancel() } //_ = which
            builder.create().show()
        } else {
            if (userEmailfromSP != "") userEmail = userEmailfromSP
            //Toast.makeText(context, userEmail, Toast.LENGTH_LONG).show()
            repository.unsubscribe("iQQnMEX22LPn5Ipy4Rxx83zs5", userEmail, event.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        event.signed = false
                        when (imageTipe) {
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

