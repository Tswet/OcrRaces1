package com.gmail.mtswetkov.ocrraces.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Price : Serializable {
    @SerializedName("id")
    val id: Int = 0
    @SerializedName("amount")
    val amount: Double = 0.0
    @SerializedName("currency")
    val currency: Currency? = Currency()
    @SerializedName("participationFormat")
    val participationFormat: ParticipationFormat? = ParticipationFormat()
    @SerializedName("shortDescription")
    val shortDescription: String = ""
}
