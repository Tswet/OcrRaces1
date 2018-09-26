package com.gmail.mtswetkov.ocrraces

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.gmail.mtswetkov.ocrraces.model.Event
import com.gmail.mtswetkov.ocrraces.model.SharedPrefWorker
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.calendar_activity.*
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {

    private var events: MutableList<Event> = mutableListOf()

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_activity)

        events = SharedPrefWorker(this).getAllEventsList()
        calendarView?.setOnDateChangeListener { _, year, month, dayOfMonth ->
            race1_calendar.visibility = View.GONE
            race2_calendar.visibility = View.GONE
            race3_calendar.visibility = View.GONE
            val myFormat = "M-d-yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            val clcDate = getString(R.string.date_fornat, (month+1).toString(),dayOfMonth.toString(),year.toString())
            for(e in events){
                var iter = 0
                if(clcDate == sdf.format(e.date)){
                    if(iter == 0) {
                        Picasso.get().load(e.icon).resize(400, 400).transform(CircularTransformation(200)).into(icon_calendar_item1)
                        name_calendar_item1.text = e.name
                        city_calendar_item1.text = e.contact?.city?.name
                        race1_calendar.visibility = View.VISIBLE
                    }
                    if(iter == 1) {
                        Picasso.get().load(e.icon).resize(400, 400).transform(CircularTransformation(200)).into(icon_calendar_item2)
                        name_calendar_item2.text = e.name
                        city_calendar_item2.text = e.contact?.city?.name
                        race1_calendar.visibility = View.VISIBLE
                    }
                    if(iter == 0) {
                        Picasso.get().load(e.icon).resize(400, 400).transform(CircularTransformation(200)).into(icon_calendar_item3)
                        name_calendar_item3.text = e.name
                        city_calendar_item3.text = e.contact?.city?.name
                        race1_calendar.visibility = View.VISIBLE
                    }
                    iter++
                }
            }
        }



    }

}
