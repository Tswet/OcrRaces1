package com.gmail.mtswetkov.ocrraces.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.gmail.mtswetkov.ocrraces.MainActivity
import com.gmail.mtswetkov.ocrraces.ShowSingleRaceActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class SharedPrefWorker(context: Context) {
    private var prefs: SharedPreferences? = context.getSharedPreferences("com.gmail.mtswetkov.ocrracesfavoritRaceHrefs", 0)
    val gson = Gson()
    private var jsonPerf: String = ""
    private var notificationList: MutableList<LocalNotification> = mutableListOf()  //LocalNotification(0, Date(), "", "")
    private var favoritList: MutableList<Int> = mutableListOf(0)
    private var events: MutableList<Event> = mutableListOf()
    private var subscribeList: MutableList<Subscribe> = mutableListOf(Subscribe(0, ""))
    private val editor = prefs!!.edit()

    open fun getNotificationList(): MutableList<LocalNotification> {

        jsonPerf = prefs!!.getString(ShowSingleRaceActivity.NOTIFICATION_OBJECTS, "")
        if (jsonPerf != "") notificationList = gson.fromJson(jsonPerf, object : TypeToken<MutableList<LocalNotification>>() {}.type)
        return notificationList
    }

    open fun getFavoritrList(): MutableList<Int> {

        jsonPerf = prefs!!.getString(ShowSingleRaceActivity.FAVORITS_RACE_ID, "")
        if (jsonPerf != "") favoritList = gson.fromJson(jsonPerf, object : TypeToken<MutableList<Int>>() {}.type)
        return favoritList
    }

    open fun getSubscribeList(): MutableList<Subscribe> {

        jsonPerf = prefs!!.getString(ShowSingleRaceActivity.MAIL_SUBCRIBE, "")
        if (jsonPerf != "") subscribeList = gson.fromJson(jsonPerf, object : TypeToken<MutableList<Subscribe>>() {}.type)
        return subscribeList
    }

    open fun getAllEventsList() : MutableList<Event>{
        jsonPerf = prefs!!.getString(MainActivity.all_events, "")
        if(jsonPerf!="")events = gson.fromJson(jsonPerf, object : TypeToken<MutableList<Event>>(){}.type)
        return events
    }

    fun setNotificationsList(notifList : MutableList<LocalNotification>){
        val jsonString: String = gson.toJson(notifList)
        editor.putString(ShowSingleRaceActivity.NOTIFICATION_OBJECTS, jsonString)
        editor.apply()
    }

    fun setFavoritsList(favList : MutableList<Int>){
        val jsonString: String = gson.toJson(favList)
        editor.putString(ShowSingleRaceActivity.FAVORITS_RACE_ID, jsonString)
        editor.apply()
    }

    fun setSubscrtibesList(subsList : MutableList<Subscribe>){
        val jsonString: String = gson.toJson(subsList)
        editor.putString(ShowSingleRaceActivity.MAIL_SUBCRIBE, jsonString)
        editor.apply()
    }

    fun setAllEventsList(events : MutableList<Event>){
        val jsonString : String = gson.toJson(events)
        Log.d("events_LIST", jsonString)
        editor.putString(MainActivity.all_events, jsonString)
        editor.apply()
    }

}



