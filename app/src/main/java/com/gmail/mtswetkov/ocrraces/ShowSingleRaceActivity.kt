package com.gmail.mtswetkov.ocrraces

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.gmail.mtswetkov.ocrraces.model.Race



class ShowSingleRaceActivity  : AppCompatActivity()  {

    var race : Race? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showsinglerace)
        val raceName : TextView = findViewById(R.id.singleRaceName)
        val raceShortDescription : TextView = findViewById(R.id.singleRaceShortDescription)
        val raceDate : TextView = findViewById(R.id.singleRaceDate)
        val raceIcon : ImageView = findViewById(R.id.singleRaceIcon)
        race = intent.getSerializableExtra("SHOW_RACE") as Race
        raceName.text = race!!.name
        raceShortDescription.text = race!!.shortDescription
        raceDate.text = race!!.date.toString()

    }
}