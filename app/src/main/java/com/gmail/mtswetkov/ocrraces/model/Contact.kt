package com.gmail.mtswetkov.ocrraces.model

import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName

data class Contact (
        @NonNull @SerializedName("id") val id: Int,
        @NonNull @SerializedName("email") val email: String,
        @NonNull @SerializedName("phone") val phone: String,
        @NonNull @SerializedName("shortDescription") val shortDescription: String,
        @NonNull @SerializedName("site") val site: String,
        @NonNull @SerializedName("country") val country: Country,
        @NonNull @SerializedName("city") val city: City,
        @NonNull @SerializedName("socialNetworks") val socialNetworks: List<SocialNetwork>
)