package com.gmail.mtswetkov.ocrraces.model

import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName

data class City (
        @NonNull @SerializedName("id") val id: Int,
        @NonNull @SerializedName("name") val Name: String,
        @NonNull @SerializedName("engName") val engName: String,
        @NonNull @SerializedName("latitude") val latitude: String,
        @NonNull @SerializedName("longitude") val longitude: String
)