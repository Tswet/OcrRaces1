package com.gmail.mtswetkov.ocrraces.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Country : Serializable {
    @SerializedName("id")
    val id: Int = 0
    @SerializedName("name")
    val site: String = ""
    @SerializedName("engName")
    val name: String = ""
}
