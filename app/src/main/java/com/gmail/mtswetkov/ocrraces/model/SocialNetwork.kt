package com.gmail.mtswetkov.ocrraces.model

import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName

data class SocialNetwork (
        @NonNull @SerializedName("id") val id: Int,
        @NonNull @SerializedName("name") val name: String,
        @NonNull @SerializedName("engName") val engName: String,
        @NonNull @SerializedName("link") val link: String,
        @NonNull @SerializedName("icon") val icon: String
)