package com.gmail.mtswetkov.ocrraces.model

import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Coordinate :Serializable {
    @SerializedName("id")
    val id: Int = 0
    @SerializedName("name")
    val name : String =""
    @SerializedName("latitude")
    val latitude: String = ""
    @SerializedName("longitude")
    val longitude: String = ""
}