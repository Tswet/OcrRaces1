package com.gmail.mtswetkov.ocrraces.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

class City : Serializable {
    @SerializedName("id")
    val id: Int = 0
    @SerializedName("name")
    val name: String = ""
    @SerializedName("engName")
    val engName: String = ""
/*    @SerializedName("latitude")
    val latitude: String = ""
    @SerializedName("longitude")
    val longitude: String = ""*/
}