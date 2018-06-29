package com.gmail.mtswetkov.ocrraces.model

import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import java.util.*


class Race() : Serializable {
    @SerializedName("id")
    val id: Int = 0
    @SerializedName("name")
    val name: String = ""
    @SerializedName("icon")
    val icon: String = ""
    @SerializedName("image")
    val image: String = ""
    @SerializedName("date")
    val date: Date? = null
    @SerializedName("shortDescription")
    val shortDescription: String = ""
    @SerializedName("favourite")
    var favourite: Boolean = false
    @SerializedName("contact")
    val contact: Contact? = null
    @SerializedName("distances")
    val distances: List<Distance>? = null
    @SerializedName("prices")
    val prices: List<Price>? = null
    @SerializedName("notifications")
    var notifications: MutableList<Notification>? = null
/*    @SerializedName("favourite")
    var notificationActive: Boolean = false*/
}

