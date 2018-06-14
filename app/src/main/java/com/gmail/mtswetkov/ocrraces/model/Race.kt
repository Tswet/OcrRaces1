package com.gmail.mtswetkov.ocrraces.model

import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import java.util.*

data class Race (
        @NonNull @SerializedName("id") val id: Int,
        @NonNull @SerializedName("name") val name: String,
        @NonNull @SerializedName("icon") val icon: String,
        @NonNull @SerializedName("image") val image: String,
        @NonNull @SerializedName("date") val date: Date,
        @NonNull @SerializedName("shortDescription") val shortDescription: String,
        @NonNull @SerializedName("favourite") val favourite: Boolean,
        @NonNull @SerializedName("contact") val contact: List<Contact>,
        @NonNull @SerializedName("distances") val distances: List<Distance>,
        @NonNull @SerializedName("prices") val prices: List<Price>,
        @NonNull @SerializedName("notifications") val notifications: List<Notification>
)