package com.gmail.mtswetkov.ocrraces


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import com.gmail.mtswetkov.ocrraces.model.Event
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.text.format.DateFormat
import android.text.format.DateFormat.*
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.gmail.mtswetkov.ocrraces.utils.CircularTransformation
import com.gmail.mtswetkov.ocrraces.utils.MonthNameFormater
import com.gmail.mtswetkov.ocrraces.btnAction.FavoritBtnClick
import com.gmail.mtswetkov.ocrraces.btnAction.MailNotificationBtnClic
import com.gmail.mtswetkov.ocrraces.btnAction.NotificationBtnClick
import com.gmail.mtswetkov.ocrraces.model.LocalNotification
import com.gmail.mtswetkov.ocrraces.model.SharedPrefWorker
import com.gmail.mtswetkov.ocrraces.model.Subscribe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchActivity : AppCompatActivity() {

    private lateinit var itemAdapter: EventAdapter
    private var events: MutableList<Event> = mutableListOf()
    private var userMail: String = ""

    lateinit var singleEvent: Event

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed();
        return true;
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        this.supportActionBar?.show()
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);

        val b : Bundle = intent.extras
        val jsonString = b.getString(MainActivity.selected_list)
        events = Gson().fromJson(jsonString, object : TypeToken<MutableList<Event>>(){}.type)
        events.sortBy { it.date }

        itemAdapter = EventAdapter()
        race_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        race_list.adapter = itemAdapter

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
            private var favoritList: MutableList<Int> = mutableListOf(0)
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
                eventMounth?.text = MonthNameFormater().formater(mounth.toString().toInt()-1)
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
                    favoritList = SharedPrefWorker(this@SearchActivity).getFavoritrList()
                    FavoritBtnClick().click(event, favoritBtnMA, this@SearchActivity, favoritList, 1)
                }
                notifBtnMA?.setOnClickListener {
                    notifList = SharedPrefWorker(this@SearchActivity).getNotificationList()
                    NotificationBtnClick().click(event, notifBtnMA, this@SearchActivity, notifList, 1)
                }

                mailNotifBtnMA?.setOnClickListener {
                    subList = SharedPrefWorker(this@SearchActivity).getSubscribeList()
                    MailNotificationBtnClic().click(event, mailNotifBtnMA, userMail, this@SearchActivity, subList, 1)
                }
            }



            override fun onClick(v: View?) {
                val pos: Int = this.layoutPosition
                singleEvent = events[pos]
                val i = Intent(this@SearchActivity, ShowSingleRaceActivity::class.java)
                i.putExtra("SHOW_RACE", singleEvent)
                startActivity(i)
            }
        }

        fun btnPicReplacer() {
            val spW = SharedPrefWorker(this@SearchActivity)
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
}

