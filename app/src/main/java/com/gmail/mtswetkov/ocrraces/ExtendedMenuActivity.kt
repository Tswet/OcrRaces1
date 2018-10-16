package com.gmail.mtswetkov.ocrraces

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.extended_menu_activity.*
import com.gmail.mtswetkov.ocrraces.model.Event
import com.gmail.mtswetkov.ocrraces.model.SharedPrefWorker
import android.widget.ArrayAdapter
import com.google.gson.Gson


class ExtendedMenuActivity : AppCompatActivity() {

    private var events: MutableList<Event> = mutableListOf()

    private var cities: MutableList<String> = mutableListOf()
    private var countries: MutableList<String> = mutableListOf()
    private var eventsSelectedList: MutableList<Event> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.extended_menu_activity)

        events = SharedPrefWorker(this).getAllEventsList()

        for (e in events) {
            if (!(cities.contains(e.contact!!.city!!.name))) cities.add(e.contact.city!!.name)
            if (!(countries.contains(e.contact.country!!.name))) countries.add(e.contact.country.name)
        }

        city_choice_view.setAdapter(ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line, cities))

        country_choice_view.setAdapter(ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line, countries))

        city_choice_view.setOnClickListener {
            eventsSelectedList = EventListSelector().cityEventSearch(events, city_choice_view.text.toString())
            srchActivityOpener(eventsSelectedList)
        }

        country_choice_view.setOnClickListener {
            eventsSelectedList = EventListSelector().countryEventSearch(events, country_choice_view.text.toString())
            srchActivityOpener(eventsSelectedList)
        }

        srch_btn.setOnClickListener {
            eventsSelectedList = events
            if(switch1.isChecked or switch2.isChecked or switch3.isChecked)eventsSelectedList =
                    EventListSelector().select(switch1.isChecked, switch2.isChecked, switch3.isChecked, events)
            if(city_choice_view.text.toString()!="")eventsSelectedList = EventListSelector().cityEventSearch(eventsSelectedList, city_choice_view.text.toString())
            if(country_choice_view.text.toString()!="")eventsSelectedList = EventListSelector().countryEventSearch(eventsSelectedList, country_choice_view.text.toString())
            srchActivityOpener(eventsSelectedList)
        }

    }

    fun srchActivityOpener(eventsSelectedList : MutableList<Event>) {
        if (eventsSelectedList.size != 0) {
            val i = Intent(this@ExtendedMenuActivity, SearchActivity::class.java)
            val jsonString = Gson().toJson(eventsSelectedList)
            i.putExtra("Selected_List", jsonString)
            startActivity(i)
        }
    }

}