package com.gmail.mtswetkov.ocrraces.model

import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SocialNetwork: Serializable {
    @SerializedName("id")
    val id: Int = 0
    @SerializedName("name")
    val name: String = ""
    @SerializedName("engName")
    val engName: String = ""
    @SerializedName("link")
    val link: String = ""
    @SerializedName("icon")
    val icon: String = ""
}