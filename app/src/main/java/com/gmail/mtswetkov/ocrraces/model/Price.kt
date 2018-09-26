package com.gmail.mtswetkov.ocrraces.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Price : Serializable {
    @SerializedName("id")
    val id: Int = 0
    @SerializedName("amount")
    val amount: Double = 0.0
    @SerializedName("currency")
    val currency: Currency? = null
    @SerializedName("participationFormat")
    val participationFormat: participationFormat? = null
    @SerializedName("shortDescription")
    val shortDescription: String = ""
}
