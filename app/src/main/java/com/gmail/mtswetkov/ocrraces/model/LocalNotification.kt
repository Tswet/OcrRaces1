package com.gmail.mtswetkov.ocrraces.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class LocalNotification : Serializable {
    constructor(raceId: Int, notifDate: Date, message: String, raceName: String) {
        this.raceId = raceId
        this.notifDate = notifDate
        this.message = message
        this.raceName = raceName
    }
    constructor(){}

    @SerializedName("raceId")
    var raceId: Int = 0
    @SerializedName("notifDate")
    var notifDate: Date = Date()
    @SerializedName("message")
    var message: String = ""
    @SerializedName("racename")
    var raceName: String = ""
}
