package com.gmail.mtswetkov.ocrraces.model


import retrofit2.http.GET
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query


interface OcrApi {

    @GET("api/v1/events")
    fun getEvents(@Query("token") token: String,
                  @Query("dbVer") dbVer: Int
    ): Observable<List<Event>>

    @GET("api/v1/notifications/email/unsubscribe")
    fun unsubscribe(@Query("token") token: String,
                    @Query("userEmail") userEmail: String,
                    @Query("eventId") eventId: Int
    ): Observable<Boolean>

    @GET("api/v1/notifications/email/subscribe")
    fun subscribe(@Query("token") token: String,
                    @Query("userEmail") userEmail: String,
                    @Query("eventId") eventId: Int
    ): Observable<Boolean>

    companion object Factory {
        fun create(): OcrApi {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://sportcontest.ru/")
                    .build()

            return retrofit.create(OcrApi::class.java);
        }
    }
}

//val base_url = "http://46.21.249.174:8080/"

//@GET("/wps/wcm/connect/rosstat_ts/yar/ru/races.json")
/*    fun getEvents(
            @Query("token") token : Int,
            @Query("dbVer") dbVer : Int
           // @Query("from") from : String = Date().toString(),
            //@Query("to") to : String = Calendar.getInstance().add(Calendar.MONTH, 6).toString()
    ) : Observable<EventResponce>*/