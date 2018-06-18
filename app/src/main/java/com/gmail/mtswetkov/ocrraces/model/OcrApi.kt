package com.gmail.mtswetkov.ocrraces.model


import retrofit2.http.GET
import io.reactivex.Observable

interface OcrApi {

    @GET("/wps/wcm/connect/rosstat_ts/yar/ru/races.json")
    fun getRaces() : Observable<RaceResponce>

}