package com.gmail.mtswetkov.ocrraces.model

import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*
import java.time.LocalDateTime

class Notification : Serializable {
    @SerializedName("id")
    val id: Int = 0
    @SerializedName("type")
    val type: String = ""
    @SerializedName("date")
    val date: String = ""
    @SerializedName("active")
    val active: Boolean = false
}