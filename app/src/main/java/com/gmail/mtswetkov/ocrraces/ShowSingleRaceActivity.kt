package com.gmail.mtswetkov.ocrraces

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import java.util.*
import kotlinx.android.synthetic.main.act_singl.*
import android.view.MotionEvent
import android.widget.*
import com.gmail.mtswetkov.ocrraces.btnAction.FavoritBtnClick
import com.gmail.mtswetkov.ocrraces.btnAction.MailNotificationBtnClic
import com.gmail.mtswetkov.ocrraces.btnAction.NotificationBtnClick
import com.gmail.mtswetkov.ocrraces.model.*
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.format.DateFormat
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import com.gmail.mtswetkov.ocrraces.Utils.ActionBarModifier
import com.gmail.mtswetkov.ocrraces.Utils.CircularTransformation
import java.text.DecimalFormat


class ShowSingleRaceActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var event: Event = Event()
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var favList: MutableList<Int> = mutableListOf()
    private var notifList: MutableList<LocalNotification> = mutableListOf(LocalNotification(0, Date(), "", ""))
    private var subscribeList: MutableList<Subscribe> = mutableListOf(Subscribe(0, ""))
    val gson = Gson()
    val PREFS_FILENAME = "com.gmail.mtswetkov.ocrracesfavoritRaceHrefs"
    val NOTIFICATION_OBJECTS = "NOTIFICATION_OBJECTS"
    private var actTitle = ""


    private var raceLat = 0.0
    private var raceLong = 0.0

    companion object {
        const val JOB_ID: Int = 101023
        const val MAIL_SUBCRIBE = "MAIL_SUBCRIBE"
        const val FAVORITS_RACE_ID = "FAVORITS_RACE_ID"
        const val NOTIFICATION_OBJECTS = "NOTIFICATION_OBJECTS"
        var userEmail = ""
    }

    override fun onBackPressed() {
        super.onBackPressed()
        event = Event()
    }


    //обработка стрелки назад из-за кастомного аскшен бара
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        this.finish()
        return true
    }

    @SuppressLint("ClickableViewAccessibility")//!!!WARNING!!!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_singl)

        val eventShortDescription: TextView = findViewById(R.id.rDesc)
        event = intent.getSerializableExtra("SHOW_RACE") as Event
        rName.text = event.name
        rType.text = event.eventType!!.name
        eventShortDescription.text = event.fullDescription

        val day = DateFormat.format("dd", event.date)
        val year = DateFormat.format("yy", event.date)
        val mounth = DateFormat.format("MM", event.date)


        val favoritBtn: ImageButton = findViewById(R.id.favoritBtn)
        val notifBtn: ImageButton = findViewById(R.id.notifBtn)
        val mailNotifBtn: ImageButton = findViewById(R.id.mailNotifBtn)

        // val jsonString: String = gson.toJson(event.contact)
        //Log.d("events_LIST", jsonString)

        if (event.contact?.coordinate == null)
            event.contact?.coordinate = Coordinate()

        if (event.contact?.coordinate?.latitude != "" && event.contact?.coordinate?.latitude != null) {
            raceLat = event.contact!!.coordinate.latitude.toDouble()
        }

        if (event.contact?.coordinate?.longitude != "" && event.contact?.coordinate?.longitude != null) {
            raceLong = event.contact!!.coordinate.longitude.toDouble()
        }


        //Favorit section
        favList = SharedPrefWorker(this).getFavoritrList()
        if (favList.contains(event.id)) {
            favoritBtn.setImageResource(R.drawable.starr)
            event.favourite = true
        }

        //Notification section
        notifList = SharedPrefWorker(this).getNotificationList()
        for (notif in notifList) {
            if (notif.raceId == event.id) {
                notifBtn.setImageResource(R.drawable.bellr)
                event.notifications = true
                break
            }
        }

        //actTitle = getString(R.string.activity_title, event.name, day.toString(), MonthNameFormater().formaterText(mounth.toString().toInt() - 1), year)
        actTitle = getString(R.string.activity_title_label_dot, day.toString(), mounth.toString(), year)
        ActionBarModifier().modify(this.supportActionBar, this, actTitle)


        //Mail Subscribe section
        eventLatitude.text = raceLat.toString()
        eventLongitude.text = raceLong.toString()

        subscribeList = SharedPrefWorker(this).getSubscribeList()
        for (sub in subscribeList) {
            if (sub.id == event.id) {
                mailNotifBtn.setImageResource(R.drawable.emailr)
                userEmail = sub.email
                event.signed = true
            }
        }

        //Event format Section
        val partLL: LinearLayout = formatPartListLayout
        if (event.prices!!.isNotEmpty()) pricetNoInfo.visibility = View.GONE
        for (f in event.prices!!) {
            val labelTextView = TextView(this)
            val discTextViev = TextView(this)
            val blankTextView = TextView(this)
            labelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
            blankTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8f)
            labelTextView.setPadding(0, 6, 0, 0)
            labelTextView.setTextColor(Color.BLACK)
            labelTextView.text = getString(R.string.format_part, f.participationFormat!!.name)
            discTextViev.text = f.participationFormat.shortDescription
            blankTextView.text = getString(R.string.blank)
            partLL.addView(labelTextView)
            partLL.addView(discTextViev)
            partLL.addView(blankTextView)
        }

        //Distance Section
        val sortedDistList: MutableList<Distance> = mutableListOf()
        var eventDistCount = 0

        if (event.distances!!.isNotEmpty()) distNoInfo.visibility = View.GONE
        while (eventDistCount < event.distances!!.size) {
            sortedDistList.add(event.distances!![eventDistCount])
            eventDistCount++
        }
        sortedDistList.sortBy { it.value }

        val distLL: LinearLayout = distListLayout
        for (d in sortedDistList) {
            val textView = TextView(this)
            textView.text = getString(R.string.raceDist1, DecimalFormat("#######.############").format(d.value), event.distances!![0].measure!!.name)
            distLL.addView(textView)
        }

        //Price Section
        val sortPriceList: MutableList<Price> = mutableListOf()
        var eventPriceCount = 0

        if (event.prices!!.isNotEmpty()) formNoInfo.visibility = View.GONE
        while (eventPriceCount < event.prices!!.size) {
            sortPriceList.add(event.prices!![eventPriceCount])
            eventPriceCount++
        }

        sortPriceList.sortBy { it.amount }

        val priceLL: LinearLayout = priceListLayout
        for (p in sortPriceList) {
            val textView = TextView(this)
            val textView1 = TextView(this)
            val blankTextView = TextView(this)
            blankTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8f)
            textView.text = getString(R.string.priceOne, p.amount.toInt().toString(), p.currency!!.name)

            textView1.text = getString(R.string.priceTwo, p.participationFormat!!.name, p.shortDescription)
            priceLL.addView(textView)
            priceLL.addView(textView1)
            priceLL.addView(blankTextView)
        }


        //Contact Section
        if (event.contact!!.country!!.name != "") rContactsCountry?.text = getString(R.string.country, event.contact!!.country!!.name)
        if (event.contact!!.city!!.name != "") rContactsCity?.text = getString(R.string.sity, event.contact!!.city!!.name)
        if (event.contact!!.address != "") rContactsAdress?.text = getString(R.string.address, event.contact?.address)
        if (event.contact!!.site != "") rContactsSite?.text = getString(R.string.site, event.contact!!.site)
        if (event.contact!!.email != "") rContactsEmail?.text = getString(R.string.email, event.contact!!.email)
        if (event.contact!!.phone != "") rContactsPhone?.text = getString(R.string.phone, event.contact!!.phone)

        //Social Network Section
        val socNets = event.contact!!.socialNetworks
        if (event.contact!!.socialNetworks!!.isNotEmpty()) scNetNoInfo.visibility = View.GONE
        for (sn in socNets!!) {
            if (sn.type == SocNameList.VKONTAKTE.toString()) {
                rSnVK?.text = getString(R.string.vkName, sn.link)
                rSnVK.textSize = 0F
                rSnVK?.visibility = View.VISIBLE
            }
            if (sn.type == SocNameList.FACEBOOK.toString()) {
                rSnFb?.text = getString(R.string.fbName, sn.link)
                rSnFb.textSize = 0F
                rSnFb?.visibility = View.VISIBLE
            }
            if (sn.type == SocNameList.INSTAGRAM.toString()) {
                rSnInst?.text = getString(R.string.inNane, sn.link)
                rSnInst.textSize = 0F
                rSnInst?.visibility = View.VISIBLE
            }
            if (sn.type == SocNameList.TELEGRAM.toString()) {
                rSnTele?.text = getString(R.string.teleName, sn.link)
                rSnTele.textSize = 0F
                rSnTele?.visibility = View.VISIBLE
            }
            if (sn.type == SocNameList.YOUTUBE.toString()) {
                rSnYt?.text = getString(R.string.ytName, sn.link)
                rSnYt.textSize = 0F
                rSnYt?.visibility = View.VISIBLE
            }
            if (sn.type == SocNameList.TWITTER.toString()) {
                rSnTwt?.text = getString(R.string.twName, sn.link)
                rSnTwt.textSize = 0F
                rSnTwt?.visibility = View.VISIBLE
            }
        }

        //Картинка и иконка
        Picasso.get().load(event.icon).error(R.drawable.opanki1).resize(400, 400).transform(CircularTransformation(200)).into(rIcon)
        Picasso.get().load(event.image).error(R.drawable.opanki).into(rImage)

        //Map Section
        val mapWrapper: ImageView = findViewById(R.id.transparent_image)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mapWrapper.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN ->
                    scrollView.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP ->
                    scrollView.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_MOVE ->
                    scrollView.requestDisallowInterceptTouchEvent(true)
            }
            v?.onTouchEvent(event) ?: true
        }

        favoritBtn.setOnClickListener {
            FavoritBtnClick().click(event, favoritBtn, this, favList, 2)
        }

        //Mail Subscribe BUTTON Action
        mailNotifBtn.setOnClickListener {
            //subscribeList = SharedPrefWorker(this).getSubscribeList(this)
            MailNotificationBtnClic().click(event, mailNotifBtn, userEmail, this, subscribeList, 2)
        }
        //NOTIFICATION BUTTON Action
        notifBtn.setOnClickListener {
            NotificationBtnClick().click(event, notifBtn, this, notifList, 2)
        }
    }

    fun googleMapsOpener(view: View) {
        val yourLocationName = event.name
        val strUri = "http://maps.google.com/maps?q=loc:$raceLat,$raceLong ($yourLocationName)"
        val intent = Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri))
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        startActivity(intent)
    }

    override fun onMapReady(p0: GoogleMap?) {

        mMap = p0!!

        val raceLocation = LatLng(raceLat, raceLong)
        mMap.addMarker(MarkerOptions().position(raceLocation).title("Your events here"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(raceLocation))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(raceLocation, 12.0f))
        mMap.uiSettings?.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(p0: Marker?) = false

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.single_race_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun shareEvent(item: MenuItem) {
        val link: String = getString(R.string.shareUrlLink, event.id)
        val title: String = getString(R.string.share)
        val mShareIntent = Intent(Intent.ACTION_SEND)
        mShareIntent.setType("text/plain")
        mShareIntent.putExtra(Intent.EXTRA_TEXT, link)
        startActivity(Intent.createChooser(mShareIntent, title))
    }

}





