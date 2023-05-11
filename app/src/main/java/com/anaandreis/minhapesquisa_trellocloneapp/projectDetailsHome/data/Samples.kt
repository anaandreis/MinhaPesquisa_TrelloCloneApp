package com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.util.Date

data class Samples(
    val annotation: String = "",
    val date: Date,
    val author: String = "",
    var projectId: String? = ""
) : Parcelable {

    // Add this no-argument constructor
    @RequiresApi(Build.VERSION_CODES.O)
    constructor() : this("", Date(),"", "")

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        Date(parcel.readLong()),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(annotation)
        parcel.writeLong(date.time)
        parcel.writeString(author)
        parcel.writeString(projectId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Samples> {
        override fun createFromParcel(parcel: Parcel): Samples {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Samples(parcel)
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        }

        override fun newArray(size: Int): Array<Samples?> {
            return arrayOfNulls(size)
        }
    }
}
