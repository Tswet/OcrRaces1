package com.gmail.mtswetkov.ocrraces.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Distance :Serializable {
    @SerializedName("id")
    val id: Int = 0
    @SerializedName("value")
    val value: Double = 0.0
    @SerializedName("measure")
    val measure: Measure? = Measure()
}