package com.gmail.mtswetkov.ocrraces

import com.gmail.mtswetkov.ocrraces.model.Event


class EventListSelector {
    private var fav: Boolean = false
    private var not: Boolean = false
    private var mail: Boolean = false
    private lateinit var eventList: MutableList<Event>
    private lateinit var eventListSelected: MutableList<Event>
    private lateinit var favEventListSelected: MutableList<Event>
    private lateinit var notListSelected: MutableList<Event>
    private lateinit var mailListSelected: MutableList<Event>
    private var countryEvents: MutableList<Event> = mutableListOf()
    private var cityEvents: MutableList<Event> = mutableListOf()


    fun select(fav: Boolean, not: Boolean, mail: Boolean, eventList: MutableList<Event>): MutableList<Event> {

        this.fav = fav
        this.not = not
        this.mail = mail
        this.eventList = eventList
        eventListSelected = mutableListOf()
/*        favEventListSelected = mutableListOf()
        notListSelected = mutableListOf()
        mailListSelected = mutableListOf()*/

        if (fav) {
            for (event in eventList) {
                if (event.favourite) {
                    if(!eventListSelected.contains(event)) eventListSelected.add(event)
                }
            }
        }
        if (not) {
            for (event in eventList) {
                if (event.notifications) {
                    if(!eventListSelected.contains(event)) eventListSelected.add(event)
                }

            }
        }
        if (mail) {
            for (event in eventList) {
                if (event.signed) {
                    if(!eventListSelected.contains(event)) eventListSelected.add(event)
                }
            }

        }

/*        for (event in eventList)
            if( event.favourite == fav && event.notifications == not && event.signed == mail)
                eventListSelected.add(event)*/

        return eventListSelected
    }

    fun cityEventSearch(events: MutableList<Event>, cityList: MutableList<String>) : MutableList<Event> {
        for (e in events) {
            for(c in cityList) {
                if (c.equals(e.contact!!.city!!.name))
                    cityEvents.add(e)
            }
        }
        return cityEvents
    }

    fun countryEventSearch(events: MutableList<Event>,  countryList: MutableList<String>)  : MutableList<Event>  {
        for (e in events) {
            for(c in countryList) {
                if (c.equals(e.contact!!.country!!.name))
                    countryEvents.add(e)
            }
        }
        return countryEvents
    }
}