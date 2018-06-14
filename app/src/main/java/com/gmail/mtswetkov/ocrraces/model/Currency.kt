package com.gmail.mtswetkov.ocrraces.model

import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName

data class Currency (
        @NonNull @SerializedName("id") val id: Int,
        @NonNull @SerializedName("name") val Name: String,
        @NonNull @SerializedName("engName") val engName: String,
        @NonNull @SerializedName("code") val code: String
)