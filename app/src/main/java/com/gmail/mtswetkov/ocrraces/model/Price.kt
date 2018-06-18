package com.gmail.mtswetkov.ocrraces.model

import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName

data class Price (
        @NonNull @SerializedName("id") val id: Int,
        @NonNull @SerializedName("amount") val amount: Double,
        @NonNull @SerializedName("currency") val currency: Currency,
        @NonNull @SerializedName("participation") val participation: Participation
)
