package com.gmail.mtswetkov.ocrraces.model

import com.google.gson.annotations.SerializedName
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
    var notifications: List<Notification>? = null
}


/*
data class Race (
        @NonNull @SerializedName("id") val id: Int,
        @NonNull @SerializedName("name") val name: String,
        @NonNull @SerializedName("icon") val icon: String,
        @NonNull @SerializedName("image") val image: String,
        @NonNull @SerializedName("date") val date: Date,
        @NonNull @SerializedName("shortDescription") val shortDescription: String,
        @NonNull @SerializedName("favourite") val favourite: Boolean,
        @NonNull @SerializedName("contact") val contact: Contact,
        @NonNull @SerializedName("distances") val distances: List<Distance>,
        @NonNull @SerializedName("prices") val prices: List<Price>,
        @NonNull @SerializedName("notifications") val notifications: List<Notification>
)*/
