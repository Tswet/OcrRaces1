package com.gmail.mtswetkov.ocrraces.model

import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

data class Country (
        @NonNull @SerializedName("id") val id: Int,
        @NotNull @SerializedName("name") val site: String,
        @NotNull @SerializedName("engName") val name: String
)
