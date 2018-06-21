package com.gmail.mtswetkov.ocrraces

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.gmail.mtswetkov.ocrraces.model.Race
import com.squareup.picasso.Picasso


class ShowSingleRaceActivity : AppCompatActivity () {

    var race: Race? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_singl)
        val raceImage: ImageView = findViewById(R.id.rImage)
        val raceIcon: ImageView = findViewById(R.id.rIcon)
        val raceName: TextView = findViewById(R.id.rName)
        val raceShortDescription: TextView = findViewById(R.id.rDesc)
        val rPartOne: TextView = findViewById(R.id.rPartOne)
        val rPartTeam: TextView = findViewById(R.id.rPartTeam)
        val rDistance1 : TextView = findViewById(R.id.rDist1)
        val rDistance2 : TextView = findViewById(R.id.rDist2)
        val rDistance3 : TextView = findViewById(R.id.rDist3)
        val rPriceOne : TextView = findViewById(R.id.rPriceOne)
        val rPriceTeam : TextView = findViewById(R.id.rPriceTeam)
        val rContactsCountry : TextView = findViewById(R.id.rContactsCountry)
        val rContactsCity : TextView = findViewById(R.id.rContactsCity)
        val rContactsSite : TextView = findViewById(R.id.rContactsSite)
        val rContactsEmail : TextView = findViewById(R.id.rContactsEmail)
        val rContactsPhone : TextView = findViewById(R.id.rContactsPhone)
        val rSnVK : TextView = findViewById(R.id.rSnVK)
        val rSnFb: TextView = findViewById(R.id.rSnFb)
        val rSnInst : TextView = findViewById(R.id.rSnInst)
        val rSnTele : TextView = findViewById(R.id.rSnTele)
        val rSnYt : TextView = findViewById(R.id.rSnYt)
        val rSnTwt : TextView = findViewById(R.id.rSnTwt)
        //val raceDate : TextView = findViewById(R.id.singleRaceDate)
        race = intent.getSerializableExtra("SHOW_RACE") as Race
        raceName.text = race!!.name
        raceShortDescription.text = race!!.shortDescription

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
        rContactsEmail.text = "Email: " +  race!!.contact!!.email
        rContactsPhone.text = "Телефон: " + race!!.contact!!.phone

        //Social Network Section
        var socNetSize : Int = race!!.contact!!.socialNetworks!!.size
        if(socNetSize>0)rSnVK.text = "VKontakte: " + race!!.contact!!.socialNetworks!!.get(0).engName
        if(socNetSize>1) rSnFb.text = "FaceBook: " + race!!.contact!!.socialNetworks!!.get(1).engName
        if(socNetSize>2) rSnInst.text = "Instagram: " + race!!.contact!!.socialNetworks!!.get(2).engName
        if(socNetSize>3) rSnTele.text = "Telegram: " + race!!.contact!!.socialNetworks!!.get(3).engName
        if(socNetSize>4) rSnYt.text = "YouTube: " + race!!.contact!!.socialNetworks!!.get(4).engName
        if(socNetSize>5) rSnTwt.text = "Twitter: " + race!!.contact!!.socialNetworks!!.get(5).engName

        //raceDate.text = race!!.date.toString()
        Picasso.get().load(race!!.icon).transform(CircularTransformation(490)).into(raceIcon)
        Picasso.get().load(race!!.image).into(raceImage)

    }
}