package com.example.retrofitsample.Model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Holiday(
    val id :Long,
    @Json(name = "holidayName")
    val name:String,
    val date : String
) :Parcelable
