package com.anaandreis.minhapesquisa_trellocloneapp.newProject

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Project(
    val name: String = "",
    val projectDescription: String = "",
    val startDate: Date,
    val endDate: Date,
    val createdBy: String,
    var assignedTo: ArrayList<String> = ArrayList()
): Parcelable {
    // Add this no-argument constructor
    constructor() : this("", "", Date(), Date(), "", ArrayList())

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        Date(parcel.readLong()),
        Date(parcel.readLong()),
        parcel.readString().toString(),
        parcel.createStringArrayList()!!.toCollection(ArrayList())
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(projectDescription)
        parcel.writeString(createdBy)
        parcel.writeLong(startDate.time)
        parcel.writeLong(endDate.time)
        parcel.writeStringList(assignedTo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Project> {
        override fun createFromParcel(parcel: Parcel): Project {
            return Project(parcel)
        }

        override fun newArray(size: Int): Array<Project?> {
            return arrayOfNulls(size)
        }
    }
}