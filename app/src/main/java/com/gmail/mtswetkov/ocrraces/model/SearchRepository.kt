package com.gmail.mtswetkov.ocrraces.model

import io.reactivex.Observable

class SearchRepository(val apiService: OcrApi) {

    fun getEvent(token: String, dbVer: Int): Observable<List<Event>> {
        return  apiService.getEvents(token = token, dbVer = dbVer)
    }

    fun unsubscribe(token: String,userEmail: String, eventId: Int): Observable<Boolean> {
        return  apiService.unsubscribe(token = token, userEmail= userEmail , eventId = eventId)
    }

    fun subscribe(token: String,userEmail: String, eventId: Int): Observable<Boolean> {
        return  apiService.subscribe(token = token, userEmail= userEmail , eventId = eventId)
    }
}