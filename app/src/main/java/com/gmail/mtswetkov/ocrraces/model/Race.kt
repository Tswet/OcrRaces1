package com.gmail.mtswetkov.ocrraces.model

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*


class Race() : Serializable{
    @NonNull
    @SerializedName("id")
    val id: Int = 0
    @NonNull
    @SerializedName("name")
    val name: String = ""
    @NonNull
    @SerializedName("icon")
    val icon: String = ""
    @NonNull
    @SerializedName("image")
    val image: String = ""
    @NonNull
    @SerializedName("date")
    val date: Date? = null
    @NonNull
    @SerializedName("shortDescription")
    val shortDescription: String = ""
    @NonNull
    @SerializedName("favourite")
    val favourite: Boolean = false
    @NonNull
    @SerializedName("contact")
    val contact: Contact? = null
    @NonNull
    @SerializedName("distances")
    val distances: List<Distance>? = null
    @NonNull
    @SerializedName("prices")
    val prices: List<Price>? = null
    @NonNull
    @SerializedName("notifications")
    val notifications: List<Notification>? = null

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
