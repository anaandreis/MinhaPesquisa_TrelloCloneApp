package com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data

import android.os.Parcel
import android.os.Parcelable

data class Warning (
    val warning: String = "",
    val author: String= "",
    val userId: String= ""
): Parcelable {
    // Add this no-argument constructor
    constructor() : this("", "", "")

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(warning)
        parcel.writeString(author)
        parcel.writeString(userId)
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
