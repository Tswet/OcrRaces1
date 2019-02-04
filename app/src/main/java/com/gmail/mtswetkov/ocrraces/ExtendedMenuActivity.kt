package com.gmail.mtswetkov.ocrraces


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import kotlinx.android.synthetic.main.extended_menu_activity.*
import com.gmail.mtswetkov.ocrraces.model.Event
import com.gmail.mtswetkov.ocrraces.model.SharedPrefWorker
import android.widget.ArrayAdapter
import android.widget.Toast
import com.gmail.mtswetkov.ocrraces.utils.ValueChangeListener
import com.google.gson.Gson


class ExtendedMenuActivity : AppCompatActivity() {

    private var events: MutableList<Event> = mutableListOf()
    private var eventsSelectedList: MutableList<Event> = mutableListOf()


    companion object {
        var cities: MutableList<String> = mutableListOf()
        var countries: MutableList<String> = mutableListOf()
        var choosenCity: MutableList<String> = mutableListOf()
        var choosenCountry: MutableList<String> = mutableListOf()
        var mOnChange = ValueChangeListener()
    }

    override fun onResume() {
        super.onResume()
        cityAndCountryTVUpdater()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        choosenCountry = mutableListOf()
        choosenCity = mutableListOf()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.extended_menu_activity)

        //this.supportActionBar?.hide()
        this.supportActionBar?.show()
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)

        events = SharedPrefWorker(this).getAllEventsList()

        for (e in events) {
            if (!(cities.contains(e.contact!!.city!!.name))) cities.add(e.contact.city!!.name)
            if (!(countries.contains(e.contact.country!!.name))) countries.add(e.contact.country.name)
        }


        city_choice_view.setAdapter(ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line, cities))
        

        country_choice_view.setAdapter(ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line, countries))

        city_choice_view.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (choosenCity.size == 0) choosenCity = cityAndCountryTVSplit(city_choice_view.text.toString().trim())
                eventsSelectedList = EventListSelector().cityEventSearch(events, choosenCity)
                srchActivityOpener(eventsSelectedList)
                return@OnKeyListener true
            }
            false
        })

        country_choice_view.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (choosenCountry.size == 0) choosenCountry = cityAndCountryTVSplit(country_choice_view.text.toString().trim())
                eventsSelectedList = EventListSelector().countryEventSearch(events, choosenCountry)
                srchActivityOpener(eventsSelectedList)
                return@OnKeyListener true
            }
            false
        })

/*        city_choice_view.setOnClickListener {
            choosenCity = mutableListOf()
            if (choosenCity.size == 0) choosenCity = cityAndCountryTVSplit(city_choice_view.text.toString().trim())
            eventsSelectedList = EventListSelector().cityEventSearch(events, choosenCity)
            srchActivityOpener(eventsSelectedList)
        }

        country_choice_view.setOnClickListener {
            choosenCountry = mutableListOf()
            if (choosenCountry.size == 0) choosenCountry = cityAndCountryTVSplit(country_choice_view.text.toString().trim())
            eventsSelectedList = EventListSelector().countryEventSearch(events, choosenCountry)
            srchActivityOpener(eventsSelectedList)
        }*/

        srch_btn.setOnClickListener {
            eventsSelectedList = events
            if (switch1.isChecked or switch2.isChecked or switch3.isChecked) eventsSelectedList =
                    EventListSelector().select(switch1.isChecked, switch2.isChecked, switch3.isChecked, events)
            if (city_choice_view.text.toString() != "") {
                if (choosenCity.size == 0) choosenCity = cityAndCountryTVSplit(city_choice_view.text.toString().trim())
                eventsSelectedList = EventListSelector().cityEventSearch(eventsSelectedList, choosenCity)
            }
            if (country_choice_view.text.toString() != "") {
                if (choosenCountry.size == 0) choosenCountry = cityAndCountryTVSplit(country_choice_view.text.toString().trim())
                eventsSelectedList = EventListSelector().countryEventSearch(eventsSelectedList, choosenCountry)
            }
            if (eventsSelectedList.size != 0) {
                srchActivityOpener(eventsSelectedList)
            } else {
                Toast.makeText(this, R.string.text_for_srch_btn, Toast.LENGTH_LONG).show()
            }

        }

        searchCityListBtn.setOnClickListener {
            choosenCity = cityAndCountryTVSplit(city_choice_view.text.toString().trim())
            getLocationCityOrCountry(1)
        }

        searchCountryListBtn.setOnClickListener {
            choosenCountry = cityAndCountryTVSplit(country_choice_view.text.toString().trim())
            getLocationCityOrCountry(2)
        }

        mOnChange.setListener(object : ValueChangeListener.ChangeListener {
            override fun onChange() {
                cityAndCountryTVUpdater()
            }
        })
    }

    private fun getLocationCityOrCountry(num: Int) {

        val cityAndCounryList = CityAndCounryList()
        val fm = this@ExtendedMenuActivity.supportFragmentManager
        val args = Bundle()
        args.putInt("num", num)
        cityAndCounryList.arguments = args
        cityAndCounryList.show(fm, "name")
    }

    private fun srchActivityOpener(eventsSelectedList: MutableList<Event>) {
        if (eventsSelectedList.size != 0) {
            val i = Intent(this@ExtendedMenuActivity, SearchActivity::class.java)
            val jsonString = Gson().toJson(eventsSelectedList)
            i.putExtra("Selected_List", jsonString)
            startActivity(i)
        }
    }

    fun cityAndCountryTVUpdater() {
        if (choosenCity.size != 0) {
            var cityList = ""
            if (choosenCity.size != 0) {
                for (c in choosenCity) {
                    if (c != choosenCity.last()) {
                        cityList += "$c, "
                    } else cityList += "$c "
                }
                city_choice_view.setText(cityList)
            }
        } else city_choice_view.setText("")
        if (choosenCountry.size != 0) {
            var countryList = ""
            if (choosenCountry.size != 0) {
                for (c in choosenCountry) {
                    if (c != choosenCountry.last()) {
                        countryList += "$c, "
                    } else countryList += "$c "
                }
                country_choice_view.setText(countryList)
            }
        } else country_choice_view.setText("")
    }

    private fun cityAndCountryTVSplit(input: String): MutableList<String> {
        val result: List<String>
        if(input != "") {
            result = input.split(", ").map { it.trim() }
        } else {
            result = mutableListOf()
        }
        return result as MutableList<String>
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.extended_activity, menu)
        return true
    }

    fun clearForm(view: View) {
        choosenCountry = mutableListOf()
        choosenCity = mutableListOf()
        switch1.isChecked = false
        switch2.isChecked = false
        switch3.isChecked = false
        country_choice_view.setText("")
        city_choice_view.setText("")
    }

}