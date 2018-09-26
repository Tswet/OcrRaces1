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
import android.net.Uri


class ShowSingleRaceActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var event: Event? = null
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var favList: MutableList<Int> = mutableListOf(0)
    private var notifList: MutableList<LocalNotification> = mutableListOf(LocalNotification(0, Date(), "", ""))
    private var subscribeList: MutableList<Subscribe> = mutableListOf(Subscribe(0, ""))
    val gson = Gson()
    val PREFS_FILENAME = "com.gmail.mtswetkov.ocrracesfavoritRaceHrefs"
    val NOTIFICATION_OBJECTS = "NOTIFICATION_OBJECTS"

    var raceLat = 0.0
    var raceLong = 0.0

    companion object {
        const val JOB_ID: Int = 101023
        const val MAIL_SUBCRIBE = "MAIL_SUBCRIBE"
        const val FAVORITS_RACE_ID = "FAVORITS_RACE_ID"
        const val NOTIFICATION_OBJECTS = "NOTIFICATION_OBJECTS"
        var userEmail = ""
    }

    @SuppressLint("ClickableViewAccessibility")//!!!WARNING!!!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_singl)

        val eventShortDescription: TextView = findViewById(R.id.rDesc)
        event = intent.getSerializableExtra("SHOW_RACE") as Event
        rName.text = event!!.name
        eventShortDescription.text = event!!.fullDescription

        val favoritBtn: ImageButton = findViewById(R.id.favoritBtn)
        val notifBtn: ImageButton = findViewById(R.id.notifBtn)
        val mailNotifBtn: ImageButton = findViewById(R.id.mailNotifBtn)

        if (event!!.contact!!.coordinate!!.latitude != "") {
            raceLat = event!!.contact!!.coordinate!!.latitude.toDouble()
        }

        if (event!!.contact!!.coordinate!!.longitude != "") {
            raceLong = event!!.contact!!.coordinate!!.longitude.toDouble()
        }


        //Favorit section
        favList = SharedPrefWorker(this).getFavoritrList()
        if (favList.contains(event!!.id)) {
            favoritBtn.setImageResource(R.drawable.starr)
            event!!.favourite = true
        }

        //Notification section
        notifList = SharedPrefWorker(this).getNotificationList()
        for (notif in notifList) {
            if (notif.raceId == event!!.id) {
                notifBtn.setImageResource(R.drawable.bellr)
                event!!.notifications = true
                break
            }
        }

        //Mail Subscribe section
        eventLatitude.text = raceLat.toString()
        eventLongitude.text = raceLong.toString()
        subscribeList = SharedPrefWorker(this).getSubscribeList()
        for (sub in subscribeList) {
            if (sub.id == event!!.id) {
                mailNotifBtn.setImageResource(R.drawable.emailr)
                userEmail = sub.email
                event!!.signed = true
            }
        }

        //Event format Section
        if (event?.eventType!!.name != "") {
            rPartOneDesc?.text = event?.eventType!!.name
        } else {
            rPartOne?.visibility = View.INVISIBLE
        }

        //Distance Section
        val distListLenght = event!!.distances!!.size
        if (distListLenght > 0) {
            rDist1?.setText(getString(R.string.raceDist1, event!!.distances!!.get(0).value.toString(), event!!.distances!!.get(0).measure!!.name))
            rDist1?.visibility = View.VISIBLE
        }
        if (distListLenght > 1) {
            rDist2?.setText(getString(R.string.raceDist2, event!!.distances!!.get(1).value.toString(), event!!.distances!!.get(1).measure!!.name))
            rDist2?.visibility = View.VISIBLE
        }
        if (distListLenght > 2) {
            rDist3?.setText(getString(R.string.raceDist3, event!!.distances!!.get(2).value.toString(), event!!.distances!!.get(2).measure!!.name))
            rDist3?.visibility = View.VISIBLE
        }

        //Price Section
        val priceSize = event!!.prices!!.size
        if (priceSize > 0) rPriceOne?.text = getString(R.string.priceOne, event!!.prices!![0].amount.toInt().toString(), event!!.prices!![0].currency!!.code, event!!.prices!![0].participationFormat!!.name)
        if (priceSize > 1) rPriceTeam?.text = getString(R.string.priceTwo, event!!.prices!!.get(1).amount.toInt().toString(), event!!.prices!!.get(1).currency!!.code, event!!.prices!![1].participationFormat!!.name)

        //Contact Section
        rContactsCountry?.setText(getString(R.string.country, event!!.contact!!.country!!.name))
        rContactsCity?.setText(getString(R.string.sity, event!!.contact!!.city!!.name))
        rContactsSite?.setText(getString(R.string.site, event!!.contact!!.site))
        rContactsEmail?.setText(getString(R.string.email, event!!.contact!!.email))
        rContactsPhone?.setText(getString(R.string.phone, event!!.contact!!.phone))

        //Social Network Section
        val socNets = event!!.contact!!.socialNetworks
        for( sn in socNets!! ){
            if(sn.type.equals(SocNameList.VKONTAKTE.toString())) {
                rSnVK?.text = getString(R.string.vkName, sn.link)
                rSnVK?.visibility = View.VISIBLE
            }
            if(sn.type.equals(SocNameList.FACEBOOK.toString())) {
                rSnFb?.text = getString(R.string.fbName, sn.link)
                rSnFb?.visibility = View.VISIBLE
            }
            if(sn.type.equals(SocNameList.INSTAGRAM.toString())) {
                rSnInst?.text = getString(R.string.inNane, sn.link)
                rSnInst?.visibility = View.VISIBLE
            }
            if(sn.type.equals(SocNameList.TELEGRAM.toString())) {
                rSnTele?.text = getString(R.string.teleName, sn.link)
                rSnTele?.visibility = View.VISIBLE
            }
            if(sn.type.equals(SocNameList.YOUTUBE.toString())) {
                rSnYt?.text = getString(R.string.ytName, sn.link)
                rSnYt?.visibility = View.VISIBLE
            }
            if(sn.type.equals(SocNameList.TWITTER.toString())) {
                rSnTwt?.text = getString(R.string.twName, sn.link)
                rSnTwt?.visibility = View.VISIBLE
            }
        }

        //raceDate.text = events!!.date.toString()
        Picasso.get().load(event!!.icon).resize(400, 400).transform(CircularTransformation(200)).into(rIcon)
        Picasso.get().load(event!!.image).into(rImage)

        //Map Section
        val mapWrapper: ImageView = findViewById(R.id.transparent_image)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mapWrapper.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        scrollView.requestDisallowInterceptTouchEvent(true)
                    MotionEvent.ACTION_UP ->
                        scrollView.requestDisallowInterceptTouchEvent(true)
                    MotionEvent.ACTION_MOVE ->
                        scrollView.requestDisallowInterceptTouchEvent(true)
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        //_________________Button Section______________________________________________
        //FAVORIT BUTTON Action
        favoritBtn.setOnClickListener {
            FavoritBtnClick().click(event!!, favoritBtn, this, favList, 2)
        }

        //Mail Subscribe BUTTON Action
        mailNotifBtn.setOnClickListener {
            //subscribeList = SharedPrefWorker(this).getSubscribeList(this)
            MailNotificationBtnClic().click(event!!, mailNotifBtn, userEmail, this, subscribeList, 2)
        }
        //NOTIFICATION BUTTON Action
        notifBtn.setOnClickListener {
            NotificationBtnClick().click(event!!, notifBtn,this,notifList, 2)
        }
    }

    fun googleMapsOpener(view: View) {
        val yourLocationName = event?.name
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

}





