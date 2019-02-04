package com.gmail.mtswetkov.ocrraces

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import com.gmail.mtswetkov.ocrraces.utils.CircularTransformation
import com.gmail.mtswetkov.ocrraces.utils.MyDotSpan
import com.gmail.mtswetkov.ocrraces.model.Event
import com.gmail.mtswetkov.ocrraces.model.SharedPrefWorker
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.calendar_activity.*
import java.text.SimpleDateFormat
import java.util.*
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator


class CalendarActivity : AppCompatActivity() {


    private var events: MutableList<Event> = mutableListOf()
    private var favList: MutableList<Int> = mutableListOf()
    private lateinit var event1: Event
    private lateinit var event2: Event
    private lateinit var event3: Event
    private lateinit var event4: Event
    private lateinit var event5: Event
    private lateinit var event6: Event
    private lateinit var event7: Event
    private lateinit var event8: Event
    private var eventColor = 0
    private var favEventColor = 0
    private val myFormat = "M-d-yyyy"
    private val sdf = SimpleDateFormat(myFormat, Locale.US)
    private val cal1 = Calendar.getInstance()
    private val cal2 = Calendar.getInstance()

    override fun onResume() {
        super.onResume()
        val tempfavList = SharedPrefWorker(this).getFavoritrList()
        if(favList != tempfavList){
            favList = tempfavList
            tempfavList.clear()
            dateDecorator()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_activity)
        eventColor = ContextCompat.getColor(this, R.color.lightBlue)
        favEventColor = ContextCompat.getColor(this, R.color.lightRed)

        //this.supportActionBar?.hide()
        this.supportActionBar?.show()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val calendar = Calendar.getInstance()
        calendarView.setDateSelected(calendar.time, true)


        dateDecorator()

        calendarView.setOnDateChangedListener { _, date, _ ->
            race1_calendar.visibility = View.GONE
            race2_calendar.visibility = View.GONE
            race3_calendar.visibility = View.GONE
            race4_calendar.visibility = View.GONE
            race5_calendar.visibility = View.GONE
            race6_calendar.visibility = View.GONE
            race7_calendar.visibility = View.GONE
            race8_calendar.visibility = View.GONE
            line3.visibility = View.GONE
            line4.visibility = View.GONE
            line5.visibility = View.GONE
            line6.visibility = View.GONE
            line7.visibility = View.GONE
            line8.visibility = View.GONE
            var iter = 0
            val clcDate = getString(R.string.date_fornat, (date.month + 1).toString(), date.day.toString(), date.year.toString())
            for (e in events) {
                if (clcDate == sdf.format(e.date)) {
                    if (iter == 0) {
                        event1 = e
                        Picasso.get().load(e.icon).resize(400, 400).transform(CircularTransformation(200)).into(icon_calendar_item1)
                        name_calendar_item1.text = e.name
                        city_calendar_item1.text = getString(R.string.event_location, e.contact!!.country!!.name, e.contact.city!!.name) //e.contact?.city?.name
                        race1_calendar.visibility = View.VISIBLE
                        line3.visibility = View.VISIBLE
                        race1_calendar.setOnClickListener {
                            eventOpener(event1)
                        }
                    }
                    if (iter == 1) {
                        event2 = e
                        Picasso.get().load(e.icon).resize(400, 400).transform(CircularTransformation(200)).into(icon_calendar_item2)
                        name_calendar_item2.text = e.name
                        city_calendar_item2.text = getString(R.string.event_location, e.contact!!.country!!.name, e.contact.city!!.name)
                        race2_calendar.visibility = View.VISIBLE
                        line4.visibility = View.VISIBLE
                        race2_calendar.setOnClickListener {
                            eventOpener(event2)
                        }
                    }
                    if (iter == 2) {
                        event3 = e
                        Picasso.get().load(e.icon).resize(400, 400).transform(CircularTransformation(200)).into(icon_calendar_item3)
                        name_calendar_item3.text = e.name
                        city_calendar_item3.text = getString(R.string.event_location, e.contact!!.country!!.name, e.contact.city!!.name)
                        race3_calendar.visibility = View.VISIBLE
                        line5.visibility = View.VISIBLE
                        race3_calendar.setOnClickListener {
                            eventOpener(event3)
                        }
                    }
                    if (iter == 3) {
                        event4 = e
                        Picasso.get().load(e.icon).resize(400, 400).transform(CircularTransformation(200)).into(icon_calendar_item4)
                        name_calendar_item4.text = e.name
                        city_calendar_item4.text = getString(R.string.event_location, e.contact!!.country!!.name, e.contact.city!!.name)
                        race4_calendar.visibility = View.VISIBLE
                        line6.visibility = View.VISIBLE
                        race4_calendar.setOnClickListener {
                            eventOpener(event4)
                        }
                    }
                    if (iter == 4) {
                        event5 = e
                        Picasso.get().load(e.icon).resize(400, 400).transform(CircularTransformation(200)).into(icon_calendar_item5)
                        name_calendar_item5.text = e.name
                        city_calendar_item5.text = getString(R.string.event_location, e.contact!!.country!!.name, e.contact.city!!.name)
                        race5_calendar.visibility = View.VISIBLE
                        line7.visibility = View.VISIBLE
                        race5_calendar.setOnClickListener {
                            eventOpener(event5)
                        }
                    }
                    if (iter == 5) {
                        event6 = e
                        Picasso.get().load(e.icon).resize(400, 400).transform(CircularTransformation(200)).into(icon_calendar_item6)
                        name_calendar_item6.text = e.name
                        city_calendar_item6.text = getString(R.string.event_location, e.contact!!.country!!.name, e.contact.city!!.name)
                        race6_calendar.visibility = View.VISIBLE
                        line8.visibility = View.VISIBLE
                        race6_calendar.setOnClickListener {
                            eventOpener(event6)
                        }
                    }
                    if (iter == 6) {
                        event7 = e
                        Picasso.get().load(e.icon).resize(400, 400).transform(CircularTransformation(200)).into(icon_calendar_item7)
                        name_calendar_item7.text = e.name
                        city_calendar_item7.text = getString(R.string.event_location, e.contact!!.country!!.name, e.contact.city!!.name)
                        race7_calendar.visibility = View.VISIBLE
                        line9.visibility = View.VISIBLE
                        race7_calendar.setOnClickListener {
                            eventOpener(event7)
                        }
                    }
                    if (iter == 7) {
                        event8 = e
                        Picasso.get().load(e.icon).resize(400, 400).transform(CircularTransformation(200)).into(icon_calendar_item8)
                        name_calendar_item8.text = e.name
                        city_calendar_item8.text = getString(R.string.event_location, e.contact!!.country!!.name, e.contact.city!!.name)
                        race8_calendar.visibility = View.VISIBLE
                        race8_calendar.setOnClickListener {
                            eventOpener(event8)
                        }
                    }
                    iter++
                }
            }
        }
    }

    private fun eventOpener(event: Event) {
        val i = Intent(this@CalendarActivity, ShowSingleRaceActivity::class.java)
        i.putExtra("SHOW_RACE", event)
        startActivity(i)
    }

    private fun dateDecorator() {
        events = SharedPrefWorker(this).getAllEventsList()
        favList = SharedPrefWorker(this).getFavoritrList()
        val favDateControlList: MutableList<Date> = mutableListOf()
        //Log.d("logevent", favList[1].toString())

        for (e in events) {
            if (favList.contains(e.id)) {
                e.favourite = true
            }
            Log.d("logevent", e.favourite.toString())
            val year = DateFormat.format("yyyy", e.date)
            val month = DateFormat.format("MM", e.date)
            val day = DateFormat.format("dd", e.date)
            cal1.set(year.toString().toInt(), month.toString().toInt() - 1, day.toString().toInt())
            cal2.set(year.toString().toInt(), month.toString().toInt() - 1, day.toString().toInt(), 23, 59, 1)
            val setDays = getCalendarDaysSet(cal1, cal2)
            if (e.favourite) {
                favDateControlList.add(e.date!!)
                calendarView.addDecorators(EventDecorator(favEventColor, setDays))
            } else {
                if (!favDateControlList.contains(e.date!!))
                    calendarView.addDecorators(EventDecorator(eventColor, setDays))
            }
        }
    }

    private inner class EventDecorator(var color: Int, dates: Collection<CalendarDay>) : DayViewDecorator {

        private val dates: HashSet<CalendarDay> = HashSet(dates)


        override fun shouldDecorate(day: CalendarDay): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(MyDotSpan(30f, color))
        }
    }

    private fun getCalendarDaysSet(cal1: Calendar, cal2: Calendar): HashSet<CalendarDay> {
        val setDays = HashSet<CalendarDay>()
        while (cal1.time.before(cal2.time)) {
            val calDay = CalendarDay.from(cal1)
            setDays.add(calDay)
            cal1.add(Calendar.DATE, 1)
        }
        return setDays
    }
}