package com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDateTime
import java.util.Date

data class Tasks (
    val task: String = "",
    val Date: Date,
    val responsable: String= "",
    var projectId: String= ""
): Parcelable {
    // Add this no-argument constructor
    constructor() : this("", Date(),"", "")

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        Date(parcel.readLong()),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(task)
        parcel.writeLong(Date.time)
        parcel.writeString(responsable)
        parcel.writeString(projectId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Warning> {
        override fun createFromParcel(parcel: Parcel): Warning {
            return Warning(parcel)
        }

        override fun newArray(size: Int): Array<Warning?> {
            return arrayOfNulls(size)
        }
    }
}
