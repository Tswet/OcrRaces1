package com.gmail.mtswetkov.ocrraces

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.gmail.mtswetkov.ocrraces.model.LocalNotification
import com.gmail.mtswetkov.ocrraces.model.Notification
import com.gmail.mtswetkov.ocrraces.model.Race
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
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList


class ShowSingleRaceActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    var race: Race? = null
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var favList: MutableList<Int> = mutableListOf(0)
    var notifList: MutableList<LocalNotification> = mutableListOf(LocalNotification(0, Date(), "", ""))
    val PREFS_FILENAME = "com.gmail.mtswetkov.ocrracesfavoritRaceHrefs"
    val FAVORITS_RACE_ID = "FAVORITS_RACE_ID"
    val NOTIFICATION_OBJECTS = "NOTIFICATION_OBJECTS"
    var prefs: SharedPreferences? = null
    val gson = Gson()
    var notifActive: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_singl)
        val raceImage: ImageView = findViewById(R.id.rImage)
        val raceIcon: ImageView = findViewById(R.id.rIcon)
        val raceName: TextView = findViewById(R.id.rName)
        val raceShortDescription: TextView = findViewById(R.id.rDesc)
        val rPartOne: TextView = findViewById(R.id.rPartOne)
        val rPartTeam: TextView = findViewById(R.id.rPartTeam)
        val rDistance1: TextView = findViewById(R.id.rDist1)
        val rDistance2: TextView = findViewById(R.id.rDist2)
        val rDistance3: TextView = findViewById(R.id.rDist3)
        val rPriceOne: TextView = findViewById(R.id.rPriceOne)
        val rPriceTeam: TextView = findViewById(R.id.rPriceTeam)
        val rContactsCountry: TextView = findViewById(R.id.rContactsCountry)
        val rContactsCity: TextView = findViewById(R.id.rContactsCity)
        val rContactsSite: TextView = findViewById(R.id.rContactsSite)
        val rContactsEmail: TextView = findViewById(R.id.rContactsEmail)
        val rContactsPhone: TextView = findViewById(R.id.rContactsPhone)
        val rSnVK: TextView = findViewById(R.id.rSnVK)
        val rSnFb: TextView = findViewById(R.id.rSnFb)
        val rSnInst: TextView = findViewById(R.id.rSnInst)
        val rSnTele: TextView = findViewById(R.id.rSnTele)
        val rSnYt: TextView = findViewById(R.id.rSnYt)
        val rSnTwt: TextView = findViewById(R.id.rSnTwt)
        //val raceDate : TextView = findViewById(R.id.singleRaceDate)
        race = intent.getSerializableExtra("SHOW_RACE") as Race
        raceName.text = race!!.name
        raceShortDescription.text = race!!.shortDescription

        val favoritBtn: ImageButton = findViewById(R.id.favoritBtn)
        val notifBtn: ImageButton = findViewById(R.id.notifBtn)
        val mailNotifBtn: ImageButton = findViewById(R.id.mailNotifBtn)

        //Favorit section
        prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        var jsonPerf: String = prefs!!.getString(FAVORITS_RACE_ID, "")
        if (jsonPerf != "") favList = gson.fromJson(jsonPerf, object : TypeToken<MutableList<Int>>() {}.type)
        if(race!!.favourite == true)favoritBtn.setImageResource(R.drawable.yelstar)
        if (favList.contains(race!!.id)) {
            favoritBtn.setImageResource(R.drawable.yelstar)
            race!!.favourite = true
        }

        //Notification section
        jsonPerf = prefs!!.getString(NOTIFICATION_OBJECTS, "")
        if (jsonPerf != "") notifList = gson.fromJson(jsonPerf, object : TypeToken<MutableList<LocalNotification>>() {}.type)
        for (notif in notifList) {
            if (notif.raceId == race!!.id) {
                notifBtn.setImageResource(R.drawable.yelbell)
                notifActive = true
                break
            }
        }

        //Race format Section

        if (race!!.prices!!.get(0).participation!!.name != "") {
            rPartOne.text = race!!.prices!!.get(0).participation!!.name
        } else {
            rPartOne.visibility = View.INVISIBLE
        }
        if (race!!.prices!!.get(1).participation!!.name != "") {
            rPartTeam.text = race!!.prices!!.get(1).participation!!.name
        } else {
            rPartTeam.visibility = View.INVISIBLE
        }
        //Distance Section
        rDistance1.text = race!!.distances!!.get(0).value.toString() + "+ " + race!!.distances!!.get(0).measure!!.name
        rDistance2.text = race!!.distances!!.get(1).value.toString() + "+ " + race!!.distances!!.get(1).measure!!.name
        rDistance3.text = race!!.distances!!.get(2).value.toString() + "+ " + race!!.distances!!.get(2).measure!!.name

        //Price Section
        rPriceOne.text = race!!.prices!!.get(0).amount.toString() + " " + race!!.prices!!.get(0).currency!!.name
        rPriceTeam.text = race!!.prices!!.get(1).amount.toString() + " " + race!!.prices!!.get(0).currency!!.name

        //Contact Section
        rContactsCountry.text = "Страна: " + race!!.contact!!.country!!.name
        rContactsCity.text = "Город: " + race!!.contact!!.city!!.name
        rContactsSite.text = "Сайт: " + race!!.contact!!.site
        rContactsEmail.text = "Email: " + race!!.contact!!.email
        rContactsPhone.text = "Телефон: " + race!!.contact!!.phone

        //Social Network Section
        val socNetSize: Int = race!!.contact!!.socialNetworks!!.size
        if (socNetSize > 0) rSnVK.text = "VKontakte: " + race!!.contact!!.socialNetworks!!.get(0).engName
        if (socNetSize > 1) rSnFb.text = "FaceBook: " + race!!.contact!!.socialNetworks!!.get(1).engName
        if (socNetSize > 2) rSnInst.text = "Instagram: " + race!!.contact!!.socialNetworks!!.get(2).engName
        if (socNetSize > 3) rSnTele.text = "Telegram: " + race!!.contact!!.socialNetworks!!.get(3).engName
        if (socNetSize > 4) rSnYt.text = "YouTube: " + race!!.contact!!.socialNetworks!!.get(4).engName
        if (socNetSize > 5) rSnTwt.text = "Twitter: " + race!!.contact!!.socialNetworks!!.get(5).engName

        //raceDate.text = race!!.date.toString()
        Picasso.get().load(race!!.icon).transform(CircularTransformation(490)).into(raceIcon)
        Picasso.get().load(race!!.image).into(raceImage)

        //Map Section
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //Button Section
        favoritBtn.setOnClickListener(View.OnClickListener {
            val editor = prefs!!.edit()
            if (race!!.favourite) {
                favoritBtn.setImageResource(R.drawable.blackstar)
                race!!.favourite = false
                favList.remove(race!!.id)
            } else {
                favoritBtn.setImageResource(R.drawable.yelstar)
                race!!.favourite = true
                favList.add(race!!.id)
            }
            var jsonString: String = gson.toJson(favList)
            editor.putString(FAVORITS_RACE_ID, jsonString)
            editor.apply()
        })
        notifBtn.setOnClickListener(View.OnClickListener {
            val temporaryDate: Date? = race!!.date
            val dateForNotifInSevenDayBefore: Date? = Date(temporaryDate!!.getYear(), temporaryDate!!.getMonth(), (temporaryDate!!.getDay() - 7))
            val dateForNotifInThreeDayBefore: Date? = Date(temporaryDate!!.getYear(), temporaryDate!!.getMonth(), (temporaryDate!!.getDay() - 3))
            val dateForNotifInOneDayBefore: Date? = Date(temporaryDate!!.getYear(), temporaryDate!!.getMonth(), (temporaryDate!!.getDay() - 1))
            println("Date" + dateForNotifInOneDayBefore + dateForNotifInSevenDayBefore)
            if (notifActive == false) {
                notifBtn.setImageResource(R.drawable.yelbell)
                var NotifInSevenDayBefore = LocalNotification(race!!.id, dateForNotifInSevenDayBefore!!, "до гонки 7 дней", race!!.name )
                var NotifInThreeDayBefore = LocalNotification(race!!.id, dateForNotifInThreeDayBefore!!,"до гонки 3 дня", race!!.name )
                var NotifInOneDayBefore = LocalNotification(race!!.id, dateForNotifInOneDayBefore!!,"до гонки 1 день", race!!.name )
                notifList.add(NotifInOneDayBefore)
                notifList.add(NotifInThreeDayBefore)
                notifList.add(NotifInSevenDayBefore)
                notifActive = true
            } else {
                notifBtn.setImageResource(R.drawable.bell)
                var deleteItems : ArrayList<Int> = ArrayList()
                for (notif in notifList) {
                    if (notif.raceId == race!!.id) deleteItems.add(notifList.indexOf(notif))
                }
                if(deleteItems[0]!=null)
                {
                    notifList.removeAt(deleteItems[2])
                    notifList.removeAt(deleteItems[1])
                    notifList.removeAt(deleteItems[0])
                    deleteItems.clear()
                }
                notifActive = false
            }
            val editor = prefs!!.edit()
            var jsonString: String = gson.toJson(notifList)
            println("NOTIF" + jsonString)
            editor.putString(NOTIFICATION_OBJECTS, jsonString)
            editor.apply()

        })
        mailNotifBtn.setOnClickListener(View.OnClickListener {
            mailNotifBtn.setImageResource(R.drawable.yelpostcard)
        })

    }

    override fun onMapReady(p0: GoogleMap?) {

        var raceLat = 0.0
        if (race!!.contact!!.coordinate!!.latitude != "") {
            raceLat = race!!.contact!!.coordinate!!.latitude.toDouble()
        }
        var raceLong = 0.0
        if (race!!.contact!!.coordinate!!.longitude != "") {
            raceLong = race!!.contact!!.coordinate!!.longitude.toDouble()
        }
        mMap = p0!!

        println("Coordinates: " + race!!.contact!!.coordinate!!.longitude + " " + race!!.contact!!.coordinate!!.latitude)

        val raceLocation = LatLng(raceLat, raceLong)
        mMap?.addMarker(MarkerOptions().position(raceLocation).title("You race here"))
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(raceLocation))

        mMap.getUiSettings().setZoomControlsEnabled(true)
        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(p0: Marker?) = false
}




