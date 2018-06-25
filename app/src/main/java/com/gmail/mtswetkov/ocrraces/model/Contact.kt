package com.gmail.mtswetkov.ocrraces.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Contact : Serializable {
    @SerializedName("id")
    val id: Int = 0
    @SerializedName("email")
    val email: String = ""
    @SerializedName("phone")
    val phone: String = ""
    @SerializedName("shortDescription")
    val shortDescription: String = ""
    @SerializedName("site")
    val site: String = ""
    @SerializedName("country")
    val country: Country? = null
    @SerializedName("city")
    val city: City? = null
    @SerializedName("coordinate")
    val coordinate: Coordinate? = null
    @SerializedName("socialNetworks")
    val socialNetworks: List<SocialNetwork>? = null

}