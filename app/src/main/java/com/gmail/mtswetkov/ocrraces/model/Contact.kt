package com.gmail.mtswetkov.ocrraces.model

import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName

import java.io.Serializable

class Contact : Serializable {

    @SerializedName("id")
    val id: Int = 0
    @SerializedName("name")
    val name: String = ""
    @SerializedName("address")
    val address: String = ""
    @SerializedName("email")
    val email: String = ""
    @SerializedName("phone")
    val phone: String = ""
    @SerializedName("shortDescription")
    val shortDescription: String = ""
    @SerializedName("site")
    val site: String = ""
    @SerializedName("country")
    val country: Country? = Country()
    @SerializedName("city")
    val city: City? = City()
    @SerializedName("coordinate")
    var coordinate: Coordinate = Coordinate()
    @SerializedName("socialNetworks")
    val socialNetworks: List<SocialNetwork>? = mutableListOf()


}