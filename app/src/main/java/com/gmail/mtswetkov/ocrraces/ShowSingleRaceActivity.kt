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
import android.text.format.DateFormat
import android.util.Log
import java.text.SimpleDateFormat


class ShowSingleRaceActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var event: Event? = null
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var favList: MutableList<Int> = mutableListOf()
    private var notifList: MutableList<LocalNotification> = mutableListOf(LocalNotification(0, Date(), "", ""))
    private var subscribeList: MutableList<Subscribe> = mutableListOf(Subscribe(0, ""))
    val gson = Gson()
    val PREFS_FILENAME = "com.gmail.mtswetkov.ocrracesfavoritRaceHrefs"
    val NOTIFICATION_OBJECTS = "NOTIFICATION_OBJECTS"
    var actTitle = ""


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
        rType.text = event!!.eventType!!.name
        eventShortDescription.text = event!!.fullDescription

        val day = DateFormat.format("d", event!!.date)
        val year = DateFormat.format("yyyy", event!!.date)
        val mounth = DateFormat.format("MM", event!!.date)



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

        actTitle = getString(R.string.activity_title,event!!.name, day.toString(), MonthNameFormater().formaterText(mounth.toString().toInt()-1), year)
        //Log.d("title_act", actTitle)
        this.supportActionBar?.title = actTitle

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
        val pfSiaze = event?.prices?.size
        if (pfSiaze!! > 0) {
            if (event?.prices!!.get(0).participationFormat!!.name != "") {
                rPartKids.setText(event?.prices!!.get(0).participationFormat!!.name)
                rPartKids.visibility = View.VISIBLE
                rPartKidsDesc.setText(event?.prices!!.get(0).participationFormat!!.shortDescription)
                rPartKidsDesc.visibility = View.VISIBLE
            }
        }
        if (pfSiaze > 1) {
            if (event?.prices!!.get(1).participationFormat!!.name != "") {
                rPartOne.setText(event?.prices!!.get(1).participationFormat!!.name)
                rPartOne.visibility = View.VISIBLE
                rPartOneDesc.setText(event?.prices!!.get(1).participationFormat!!.shortDescription)
                rPartOneDesc.visibility = View.VISIBLE
            }
        }
        if (pfSiaze > 2) {
            if (event?.prices!!.get(2).participationFormat!!.name != "") {
                rPartTeam.setText(event?.prices!!.get(2).participationFormat!!.name)
                rPartTeam.visibility = View.VISIBLE
                rPartTeamDesc.setText(event?.prices!!.get(2).participationFormat!!.shortDescription)
                rPartTeamDesc.visibility = View.VISIBLE
            }
        }
        if (pfSiaze > 3) {
            if (event?.prices!!.get(3).participationFormat!!.name != "") {
                rPartMass.setText(event?.prices!!.get(3).participationFormat!!.name)
                rPartMass.visibility = View.VISIBLE
                rPartMassDesc.setText(event?.prices!!.get(3).participationFormat!!.shortDescription)
                rPartMassDesc.visibility = View.VISIBLE
            }
        }
        if (pfSiaze > 4) {
            if (event?.prices!!.get(4).participationFormat!!.name != "") {
                rPartPair.setText(event?.prices!!.get(4).participationFormat!!.name)
                rPartPair.visibility = View.VISIBLE
                rPartPairDesc.setText(event?.prices!!.get(4).participationFormat!!.shortDescription)
                rPartPairDesc.visibility = View.VISIBLE
            }
        }
        if (pfSiaze > 5) {
            if (event?.prices!!.get(5).participationFormat!!.name != "") {
                rPartTeamChemp.setText(event?.prices!!.get(5).participationFormat!!.name)
                rPartTeamChemp.visibility = View.VISIBLE
                rPartTeamChempDesc.setText(event?.prices!!.get(5).participationFormat!!.shortDescription)
                rPartTeamChempDesc.visibility = View.VISIBLE
            }
        }
        if (pfSiaze > 6) {
            if (event?.prices!!.get(6).participationFormat!!.name != "") {
                rPartExtended.setText(event?.prices!!.get(6).participationFormat!!.name)
                rPartExtended.visibility = View.VISIBLE
                rPartExtendedDesc.setText(event?.prices!!.get(6).participationFormat!!.shortDescription)
                rPartExtendedDesc.visibility = View.VISIBLE
            }
        }
        if (pfSiaze > 7) {
            if (event?.prices!!.get(7).participationFormat!!.name != "") {
                rPartExtended2.setText(event?.prices!!.get(7).participationFormat!!.name)
                rPartExtended2.visibility = View.VISIBLE
                rPartExtended2Desc.setText(event?.prices!!.get(7).participationFormat!!.shortDescription)
                rPartExtended2Desc.visibility = View.VISIBLE
            }
        }


        //Distance Section
        val distListLenght = event!!.distances!!.size
        if (distListLenght > 0) {
            rDist1?.setText(getString(R.string.raceDist1, event!!.distances!!.get(0).value.toInt().toString(), event!!.distances!!.get(0).measure!!.name))
            rDist1?.visibility = View.VISIBLE
        }
        if (distListLenght > 1) {
            rDist2?.setText(getString(R.string.raceDist2, event!!.distances!!.get(1).value.toInt().toString(), event!!.distances!!.get(1).measure!!.name))
            rDist2?.visibility = View.VISIBLE
        }
        if (distListLenght > 2) {
            rDist3?.setText(getString(R.string.raceDist3, event!!.distances!!.get(2).value.toInt().toString(), event!!.distances!!.get(2).measure!!.name))
            rDist3?.visibility = View.VISIBLE
        }
        if (distListLenght > 3) {
            rDist4?.setText(getString(R.string.raceDist3, event!!.distances!!.get(3).value.toInt().toString(), event!!.distances!!.get(3).measure!!.name))
            rDist4?.visibility = View.VISIBLE
        }
        if (distListLenght > 4) {
            rDist5?.setText(getString(R.string.raceDist3, event!!.distances!!.get(4).value.toInt().toString(), event!!.distances!!.get(4).measure!!.name))
            rDist5?.visibility = View.VISIBLE
        }

        //Price Section
        val priceSize = event!!.prices!!.size
        if (priceSize > 0) {
            rPrice1?.text = getString(R.string.priceOne, event!!.prices!![0].amount.toInt().toString(), event!!.prices!![0].currency!!.name, event!!.prices!![0].participationFormat!!.name)
            rPrice1.visibility = View.VISIBLE
        }
        if (priceSize > 1) {
            rPrice2?.text = getString(R.string.priceTwo, event!!.prices!!.get(1).amount.toInt().toString(), event!!.prices!!.get(1).currency!!.name, event!!.prices!![1].participationFormat!!.name)
            rPrice2.visibility = View.VISIBLE
        }
        if (priceSize > 2) {
            rPrice3?.text = getString(R.string.priceTwo, event!!.prices!!.get(2).amount.toInt().toString(), event!!.prices!!.get(2).currency!!.name, event!!.prices!![2].participationFormat!!.name)
            rPrice3.visibility = View.VISIBLE
        }
        if (priceSize > 3) {
            rPrice4?.text = getString(R.string.priceTwo, event!!.prices!!.get(3).amount.toInt().toString(), event!!.prices!!.get(3).currency!!.name, event!!.prices!![3].participationFormat!!.name)
            rPrice4.visibility = View.VISIBLE
        }
        if (priceSize > 4) {
            rPrice5?.text = getString(R.string.priceTwo, event!!.prices!!.get(4).amount.toInt().toString(), event!!.prices!!.get(4).currency!!.name, event!!.prices!![4].participationFormat!!.name)
            rPrice5.visibility = View.VISIBLE
        }
        if (priceSize > 5) {
            rPrice6?.text = getString(R.string.priceTwo, event!!.prices!!.get(5).amount.toInt().toString(), event!!.prices!!.get(5).currency!!.name, event!!.prices!![5].participationFormat!!.name)
            rPrice6.visibility = View.VISIBLE
        }
        if (priceSize > 6) {
            rPrice7?.text = getString(R.string.priceTwo, event!!.prices!!.get(6).amount.toInt().toString(), event!!.prices!!.get(6).currency!!.name, event!!.prices!![6].participationFormat!!.name)
            rPrice7.visibility = View.VISIBLE
        }
        if (priceSize > 7) {
            rPrice8?.text = getString(R.string.priceTwo, event!!.prices!!.get(7).amount.toInt().toString(), event!!.prices!!.get(7).currency!!.name, event!!.prices!![7].participationFormat!!.name)
            rPrice8.visibility = View.VISIBLE
        }

        //Contact Section
        if (event!!.contact!!.country!!.name != "") rContactsCountry?.setText(getString(R.string.country, event!!.contact!!.country!!.name))
        if (event!!.contact!!.city!!.name != "") rContactsCity?.setText(getString(R.string.sity, event!!.contact!!.city!!.name))
        if (event!!.contact!!.address != "") rContactsAdress?.setText(getString(R.string.address, event?.contact?.address))
        if (event!!.contact!!.site != "") rContactsSite?.setText(getString(R.string.site, event!!.contact!!.site))
        if (event!!.contact!!.email != "") rContactsEmail?.setText(getString(R.string.email, event!!.contact!!.email))
        if (event!!.contact!!.phone != "") rContactsPhone?.setText(getString(R.string.phone, event!!.contact!!.phone))

        //Social Network Section
        val socNets = event!!.contact!!.socialNetworks
        for (sn in socNets!!) {
            if (sn.type.equals(SocNameList.VKONTAKTE.toString())) {
                rSnVK?.text = getString(R.string.vkName, sn.link)
                rSnVK.textSize = 0F
                rSnVK?.visibility = View.VISIBLE
            }
            if (sn.type.equals(SocNameList.FACEBOOK.toString())) {
                rSnFb?.text = getString(R.string.fbName, sn.link)
                rSnFb.textSize = 0F
                rSnFb?.visibility = View.VISIBLE
            }
            if (sn.type.equals(SocNameList.INSTAGRAM.toString())) {
                rSnInst?.text = getString(R.string.inNane, sn.link)
                rSnInst.textSize = 0F
                rSnInst?.visibility = View.VISIBLE
            }
            if (sn.type.equals(SocNameList.TELEGRAM.toString())) {
                rSnTele?.text = getString(R.string.teleName, sn.link)
                rSnTele.textSize = 0F
                rSnTele?.visibility = View.VISIBLE
            }
            if (sn.type.equals(SocNameList.YOUTUBE.toString())) {
                rSnYt?.text = getString(R.string.ytName, sn.link)
                rSnYt.textSize = 0F
                rSnYt?.visibility = View.VISIBLE
            }
            if (sn.type.equals(SocNameList.TWITTER.toString())) {
                rSnTwt?.text = getString(R.string.twName, sn.link)
                rSnTwt.textSize = 0F
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
            NotificationBtnClick().click(event!!, notifBtn, this, notifList, 2)
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





