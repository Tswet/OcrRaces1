package com.gmail.mtswetkov.ocrraces.model

import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import java.util.*

class Event : Serializable {
    @SerializedName("id")
    val id: Int = 0
    @SerializedName("name")
    val name: String = ""
    @SerializedName("engName")
    val engName: String = ""
    @SerializedName("icon")
    val icon: String = ""
    @SerializedName("image")
    val image: String = ""
    @SerializedName("date")
    val date: Date? = null
    @SerializedName("shortDescription")
    val shortDescription: String = ""
    @SerializedName("fullDescription")
    val fullDescription: String = ""
    @SerializedName("contact")
    val contact: Contact? = Contact()
    @SerializedName("distances")
    val distances: List<Distance>? = mutableListOf()
    @SerializedName("eventType")
    val eventType: EventType? = EventType()
    @SerializedName("prices")
    val prices: List<Price>? = mutableListOf()
    @SerializedName("favourite")
    var favourite: Boolean = false
    @SerializedName("notifications")
    var notifications: Boolean = false
    @SerializedName("signed")
    var signed: Boolean = false

}


