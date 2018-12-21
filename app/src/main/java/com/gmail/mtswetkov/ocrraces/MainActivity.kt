package com.gmail.mtswetkov.ocrraces


import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.net.ConnectivityManager
import android.support.design.widget.BottomNavigationView
import android.support.v4.widget.SwipeRefreshLayout
import android.text.format.DateFormat
import android.text.format.DateFormat.*
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.gmail.mtswetkov.ocrraces.btnAction.FavoritBtnClick
import com.gmail.mtswetkov.ocrraces.btnAction.MailNotificationBtnClic
import com.gmail.mtswetkov.ocrraces.btnAction.NotificationBtnClick
import com.gmail.mtswetkov.ocrraces.model.*
import com.google.gson.Gson


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.favoriteMenu -> {
                eventsSelectedList = EventListSelector().select(true, false, false, events)
                if (eventsSelectedList.size != 0) {
                    val i = Intent(this@MainActivity, SearchActivity::class.java)
                    val jsonString = Gson().toJson(eventsSelectedList)
                    i.putExtra("Selected_List", jsonString)
                    startActivity(i)
                }
            }
            R.id.extMenu -> {
                eventsSelectedList = mutableListOf()
                SharedPrefWorker(this@MainActivity).setAllEventsList(events)
                //val i = Intent(this, ExtendedMenuActivity::class.java)
                val i = Intent(this@MainActivity, ExtendedMenuActivity::class.java)
                startActivity(i)
                Log.d("menuuuu", "extMenu")
            }
            R.id.calendarMenu -> {
                eventsSelectedList = mutableListOf()
                SharedPrefWorker(this@MainActivity).setAllEventsList(events)
                val i = Intent(this@MainActivity, CalendarActivity::class.java)
                startActivity(i)
                Log.d("menuuuu", "calMenu")
            }
        }
        false
    }

    private lateinit var itemAdapter: EventAdapter
    private var events: MutableList<Event> = mutableListOf()
    private var eventsSelectedList: MutableList<Event> = mutableListOf()
    var singleEvent: Event = Event()
    private val repository = SearchRepositoryProvider.provideSearchRepository()
    private var userMail: String = ""


    companion object {
        const val selected_list = "Selected_List"
        const val all_events = "ALL_EVENTS"
    }

    override fun onRefresh() {
        events.clear()
        progress_bar.visibility = View.VISIBLE
        getDataDespatcher()
        btnPicReplacer()
        itemAdapter.notifyDataSetChanged()
        ma_swipe.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        btnPicReplacer()
        itemAdapter.notifyDataSetChanged()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ma_swipe.setOnRefreshListener(this)

        this.supportActionBar?.hide()

        itemAdapter = EventAdapter()
        race_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        race_list.adapter = itemAdapter
        getDataDespatcher()

        val navigation = findViewById<View>(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.getMenu().findItem(R.id.favoriteMenu).setChecked(false)

    }

    private fun getDataFromServer() {
        repository.getEvent("iQQnMEX22LPn5Ipy4Rxx83zs5", 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    itemAdapter.setEvents(it)
                    SharedPrefWorker(this).setAllEventsList(events)
                    progress_bar.visibility = View.GONE
                }, {
                    println("Error++ " + it.message)
                })
    }

    private fun getDataDespatcher() {
        val cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        if (netInfo != null && netInfo.isConnected) {
            getDataFromServer()
        } else {
            events = SharedPrefWorker(this).getAllEventsList()
            if (events.size > 0) {
                itemAdapter.notifyDataSetChanged()
                Toast.makeText(this, getString(R.string.noInternet_with_data), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, getString(R.string.noInternet), Toast.LENGTH_LONG).show()
            }
        }
    }

    inner class EventAdapter : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
            return EventViewHolder(layoutInflater.inflate(R.layout.race_item, parent, false))
        }

        override fun getItemCount(): Int {
            return events.size
        }

        override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
            holder.bindModel(events[position])
        }

        fun setEvents(item: List<Event>) {
            events.addAll(item)
            notifyDataSetChanged()
        }

        inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

            val eventImage: ImageView? = itemView.findViewById(R.id.eventImage)
            val eventIcon: ImageView? = itemView.findViewById(R.id.eventIcon)
            val eventName: TextView? = itemView.findViewById(R.id.eventName)
            val eventShortDescription: TextView? = itemView.findViewById(R.id.eventShortDescription)
            val eventDay: TextView? = itemView.findViewById(R.id.eventDay)
            val eventMounth: TextView? = itemView.findViewById(R.id.eventMounth)
            val eventLocation: TextView? = itemView.findViewById(R.id.eventLocation)
            val favoritBtnMA: ImageButton? = itemView.findViewById(R.id.favoritBtnMA)
            val notifBtnMA: ImageButton? = itemView.findViewById(R.id.notifBtnMA)
            val mailNotifBtnMA: ImageButton? = itemView.findViewById(R.id.mailNotifBtnMA)
            private var favoritList: MutableList<Int> = mutableListOf()
            private var notifList: MutableList<LocalNotification> = mutableListOf()
            private var subList: MutableList<Subscribe> = mutableListOf()


            init {
                itemView.setOnClickListener(this)
            }

            fun bindModel(event: Event) {
                eventName?.text = event.name
                eventShortDescription?.text = event.shortDescription
                val day = format("d", event.date)
                val mounth = DateFormat.format("MM", event.date)
                eventDay?.text = day.toString()
                eventMounth?.text = MonthNameFormater().formater(mounth.toString().toInt() - 1)
                eventLocation?.text = getString(R.string.event_location, event.contact!!.country!!.name, event.contact.city!!.name)//"${event.contact!!.country!!.name} - ${event.contact.city!!.name}"
                Picasso.get().load(event.icon).resize(400, 400).transform(CircularTransformation(200)).into(eventIcon)
                Picasso.get().load(event.image).error(getDrawable(R.drawable.opanki)).into(eventImage)
                btnPicReplacer()
                if (event.favourite) {
                    favoritBtnMA?.setImageResource(R.drawable.starrsm)
                } else {
                    favoritBtnMA?.setImageResource(R.drawable.starsm)
                }
                if (event.notifications) {
                    notifBtnMA?.setImageResource(R.drawable.bellrsm)
                } else {
                    notifBtnMA?.setImageResource(R.drawable.bellsm)
                }
                if (event.signed) {
                    mailNotifBtnMA?.setImageResource(R.drawable.emailrsm)
                } else {
                    mailNotifBtnMA?.setImageResource(R.drawable.emailsm)
                }

                favoritBtnMA?.setOnClickListener {
                    favoritList = SharedPrefWorker(this@MainActivity).getFavoritrList()
                    FavoritBtnClick().click(event, favoritBtnMA, this@MainActivity, favoritList, 1)
                }
                notifBtnMA?.setOnClickListener {
                    notifList = SharedPrefWorker(this@MainActivity).getNotificationList()
                    NotificationBtnClick().click(event, notifBtnMA, this@MainActivity, notifList, 1)
                }

                mailNotifBtnMA?.setOnClickListener {
                    val ml = SharedPrefWorker(this@MainActivity).getSubscribeList()
                    for (e in events) {
                        for (item in ml) {
                            if (e.id == item.id) {
                                userMail = item.email
                            }
                        }
                    }
                    subList = SharedPrefWorker(this@MainActivity).getSubscribeList()
                    MailNotificationBtnClic().click(event, mailNotifBtnMA, userMail, this@MainActivity, subList, 1)
                }
            }

            override fun onClick(v: View?) {
                val pos: Int = this.layoutPosition //this.position
                singleEvent = events[pos]
                val i = Intent(this@MainActivity, ShowSingleRaceActivity::class.java)
                i.putExtra("SHOW_RACE", singleEvent)
                startActivity(i)
            }
        }
    }

/*    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_main_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.favoriteMenu ->
                eventsSelectedList = EventListSelector().select(true, false, false, events)

*//*            R.id.notificationMenu ->
                eventsSelectedList = EventListSelector().select(false, true, false, events)

            R.id.mailMenu ->
                eventsSelectedList = EventListSelector().select(false, false, true, events)*//*

            R.id.extMenu -> {
                eventsSelectedList = mutableListOf()
                SharedPrefWorker(this).setAllEventsList(events)
                val i = Intent(this, ExtendedMenuActivity::class.java)
                startActivity(i)
            }
            R.id.calendarMenu -> {
                eventsSelectedList = mutableListOf()
                SharedPrefWorker(this).setAllEventsList(events)
                val i = Intent(this, CalendarActivity::class.java)
                startActivity(i)
            }
        }
        if (eventsSelectedList.size != 0) {
            val i = Intent(this@MainActivity, SearchActivity::class.java)
            val jsonString = Gson().toJson(eventsSelectedList)
            i.putExtra("Selected_List", jsonString)
            startActivity(i)
        }

        return super.onOptionsItemSelected(item)
    }*/


    fun btnPicReplacer() {
        val spW = SharedPrefWorker(this)
        val fl = spW.getFavoritrList()
        for (event in events) {
            event.favourite = fl.contains(event.id)
        }
        val nl = spW.getNotificationList()
        for (event in events) {
            event.notifications = false
            for (item in nl) {
                if (event.id == item.raceId) event.notifications = true

            }
        }
        val ml = spW.getSubscribeList()
        for (event in events) {
            event.signed = false
            for (item in ml) {
                if (event.id == item.id) {
                    event.signed = true
                    userMail = item.email
                }
            }
        }
    }

}

