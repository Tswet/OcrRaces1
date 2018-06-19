package com.gmail.mtswetkov.ocrraces.model

import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Participation : Serializable {
    @SerializedName("id")
    val id: Int = 0
    @SerializedName("name")
    val Name: String = ""
    @SerializedName("engName")
    val engName: String = ""
    @SerializedName("shortDescription")
    val shortDescription: String = ""
}