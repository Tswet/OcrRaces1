package com.gmail.mtswetkov.ocrraces.model

import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Currency : Serializable {
    @SerializedName("id") val id: Int = 0
    @SerializedName("name") val Name: String = ""
    @SerializedName("engName") val engName: String = ""
    @SerializedName("code") val code: String = ""
}
