package com.gmail.mtswetkov.ocrraces.model

import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*
import java.time.LocalDateTime

class Notification : Serializable {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("type")
    var type: String = ""
    @SerializedName("date")
    var date: String = ""
    @SerializedName("active")
    var active: Boolean = false
}