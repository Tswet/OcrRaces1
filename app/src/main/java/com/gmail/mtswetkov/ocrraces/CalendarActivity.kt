package com.gmail.mtswetkov.ocrraces

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.util.Log
import android.view.View
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
    lateinit var event1: Event
    lateinit var event2: Event
    lateinit var event3: Event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_activity)
        events = SharedPrefWorker(this).getAllEventsList()
        val eventColor = ContextCompat.getColor(this, R.color.lightBlue)
        val favEventColor = ContextCompat.getColor(this, R.color.lightGreen)
        val myFormat = "M-d-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()

        for (e in events) {
            val year = DateFormat.format("yyyy", e.date)
            val month = DateFormat.format("MM", e.date)
            val day = DateFormat.format("dd", e.date)

            cal1.set(year.toString().toInt(), month.toString().toInt() - 1, day.toString().toInt())
            cal2.set(year.toString().toInt(), month.toString().toInt() - 1, day.toString().toInt(), 22, 59, 1)
            val setDays = getCalendarDaysSet(cal1, cal2)
            if (e.favourite) {
                calendarView.addDecorators(EventDecorator(favEventColor, setDays))
            } else {
                calendarView.addDecorators(EventDecorator(eventColor, setDays))
            }
        }


        calendarView.setOnDateChangedListener { _, date, _ ->
            race1_calendar.visibility = View.GONE
            race2_calendar.visibility = View.GONE
            race3_calendar.visibility = View.GONE

            val clcDate = getString(R.string.date_fornat, (date.month + 1).toString(), date.day.toString(), date.year.toString())
            for (e in events) {
                var iter = 0
                if (clcDate == sdf.format(e.date)) {
                    if (iter == 0) {
                        event1 = e
                        Picasso.get().load(e.icon).resize(400, 400).transform(CircularTransformation(200)).into(icon_calendar_item1)
                        name_calendar_item1.text = e.name
                        city_calendar_item1.text = e.contact?.city?.name
                        race1_calendar.visibility = View.VISIBLE
                        race1_calendar.setOnClickListener {
                            eventOpener(this.city_calendar_item1, event1)
                        }
                    }
                    if (iter == 1) {
                        event2 = e
                        Picasso.get().load(e.icon).resize(400, 400).transform(CircularTransformation(200)).into(icon_calendar_item2)
                        name_calendar_item2.text = e.name
                        city_calendar_item2.text = e.contact?.city?.name
                        race1_calendar.visibility = View.VISIBLE
                        race1_calendar.setOnClickListener {
                            eventOpener(this.city_calendar_item1, event1)
                        }
                    }
                    if (iter == 0) {
                        event3 = e
                        Picasso.get().load(e.icon).resize(400, 400).transform(CircularTransformation(200)).into(icon_calendar_item3)
                        name_calendar_item3.text = e.name
                        city_calendar_item3.text = e.contact?.city?.name
                        race1_calendar.visibility = View.VISIBLE
                        race1_calendar.setOnClickListener {
                            eventOpener(this.city_calendar_item1, event1)
                        }
                    }
                    iter++
                }
            }
        }
    }

     fun eventOpener(v: View?, event: Event) {
        val singleEvent = event
        val i = Intent(this@CalendarActivity, ShowSingleRaceActivity::class.java)
        i.putExtra("SHOW_RACE", singleEvent)
        startActivity(i)
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