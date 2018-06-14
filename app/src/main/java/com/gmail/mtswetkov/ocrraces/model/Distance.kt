package com.gmail.mtswetkov.ocrraces.model

import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName

data class Distance (
        @NonNull @SerializedName("id") val id: Int,
        @NonNull @SerializedName("value") val value: Double,
        @NonNull @SerializedName("measure") val measure: String
)