package com.gmail.mtswetkov.ocrraces.model

import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import java.util.*

data class Notification (
        @NonNull @SerializedName("id") val id: Int,
        @NonNull @SerializedName("type") val type: String,
        @NonNull @SerializedName("date") val date: Date,
        @NonNull @SerializedName("active") val active: Boolean
)