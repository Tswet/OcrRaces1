package com.gmail.mtswetkov.ocrraces.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Subscribe : Serializable {
    constructor(id : Int, email: String){
        this.id = id
        this.email = email
    }
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("email")
    var email: String = ""
}