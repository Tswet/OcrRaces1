package com.gmail.mtswetkov.ocrraces

import com.gmail.mtswetkov.ocrraces.model.Event

class EventListSelector {
    private var fav: Boolean = false
    private var not: Boolean = false
    private var mail: Boolean = false
    private lateinit var eventList: MutableList<Event>
    private lateinit var eventListSelected: MutableList<Event>


    fun select(fav: Boolean, not: Boolean, mail: Boolean, eventList: MutableList<Event>): MutableList<Event> {

        this.fav = fav
        this.not = not
        this.mail = mail
        this.eventList = eventList
        eventListSelected = mutableListOf()

        if (fav) {
            for (event in eventList) {
                if (event.favourite) {
                    eventListSelected.add(event)
                }
            }
        }
        if (not) {
            for (event in eventList) {
                if (event.notifications) {
                    eventListSelected.add(event)
                }

            }
        }
        if (mail) {
            for (event in eventList) {
                if (event.signed) {
                    eventListSelected.add(event)
                }
            }

        }
        return eventListSelected
    }
}